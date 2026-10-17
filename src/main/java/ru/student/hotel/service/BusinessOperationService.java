package ru.student.hotel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.student.hotel.dto.BookingRequest;
import ru.student.hotel.dto.BookingResponse;
import ru.student.hotel.dto.ExtendBookingRequest;
import ru.student.hotel.dto.ReservationRequest;
import ru.student.hotel.exception.BusinessRuleException;
import ru.student.hotel.model.Booking;
import ru.student.hotel.model.BookingStatus;
import ru.student.hotel.model.Room;
import ru.student.hotel.model.RoomStatus;

import java.time.LocalDate;

@Service
@Transactional
public class BusinessOperationService {
    private final BookingService bookings;
    private final RoomService rooms;

    public BusinessOperationService(BookingService bookings, RoomService rooms) {
        this.bookings = bookings;
        this.rooms = rooms;
    }

    // Operation 1: create a reservation after validating the customer, room, capacity and date range.
    public BookingResponse createReservation(ReservationRequest request) {
        return bookings.create(new BookingRequest(request.customerId(), request.roomId(),
                request.checkInDate(), request.checkOutDate(), request.guests()));
    }

    // Operation 2: confirm a reservation while checking the current state of its room.
    public BookingResponse confirm(Long bookingId) {
        Booking booking = bookings.getEntity(bookingId);
        Room room = rooms.getEntityForUpdate(booking.getRoom().getId());
        if (booking.getStatus() != BookingStatus.BOOKED) {
            throw new BusinessRuleException("Подтвердить можно только бронирование со статусом BOOKED.");
        }
        if (room.getStatus() == RoomStatus.MAINTENANCE || room.getStatus() == RoomStatus.CLEANING) {
            throw new BusinessRuleException("Номер нельзя подтвердить: его статус " + room.getStatus() + ".");
        }
        booking.setStatus(BookingStatus.CONFIRMED);
        return bookings.toResponse(booking);
    }

    // Operation 3: change both booking and room state in the same transaction.
    public BookingResponse checkIn(Long bookingId) {
        Booking booking = bookings.getEntity(bookingId);
        Room room = rooms.getEntityForUpdate(booking.getRoom().getId());
        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new BusinessRuleException("Заселить можно только подтверждённое бронирование.");
        }
        LocalDate today = LocalDate.now();
        if (today.isBefore(booking.getCheckInDate()) || !today.isBefore(booking.getCheckOutDate())) {
            throw new BusinessRuleException("Заселение разрешено в период от даты заезда включительно до даты выезда не включительно.");
        }
        if (room.getStatus() != RoomStatus.AVAILABLE) {
            throw new BusinessRuleException("Номер должен иметь статус AVAILABLE для заселения. Сейчас: " + room.getStatus() + ".");
        }
        room.setStatus(RoomStatus.OCCUPIED);
        booking.setStatus(BookingStatus.CHECKED_IN);
        return bookings.toResponse(booking);
    }

    // Operation 4: finish the stay and put the room into cleaning state.
    public BookingResponse checkOut(Long bookingId) {
        Booking booking = bookings.getEntity(bookingId);
        Room room = rooms.getEntityForUpdate(booking.getRoom().getId());
        if (booking.getStatus() != BookingStatus.CHECKED_IN) {
            throw new BusinessRuleException("Выезд разрешён только для фактически заселённого гостя.");
        }
        if (room.getStatus() != RoomStatus.OCCUPIED) {
            throw new BusinessRuleException("Номер должен иметь статус OCCUPIED перед выездом.");
        }
        booking.setStatus(BookingStatus.CHECKED_OUT);
        room.setStatus(RoomStatus.CLEANING);
        return bookings.toResponse(booking);
    }

    // Operation 5: cancellation removes the booking from future availability checks.
    public BookingResponse cancel(Long bookingId) {
        Booking booking = bookings.getEntity(bookingId);
        Room room = rooms.getEntityForUpdate(booking.getRoom().getId());
        if (booking.getStatus() != BookingStatus.BOOKED && booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new BusinessRuleException("Отменить можно только бронирование со статусом BOOKED или CONFIRMED.");
        }
        if (!LocalDate.now().isBefore(booking.getCheckInDate())) {
            throw new BusinessRuleException("В день заезда или после него отменить бронирование нельзя.");
        }
        // Reading the room under lock makes the reservation/room relationship part of this operation.
        if (room.getStatus() == RoomStatus.OCCUPIED && booking.getCheckInDate().equals(LocalDate.now())) {
            throw new BusinessRuleException("Нельзя отменить бронирование: номер уже занят.");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        return bookings.toResponse(booking);
    }

    // Operation 6: extend a stay only if the extra nights do not overlap another active reservation.
    public BookingResponse extend(Long bookingId, ExtendBookingRequest request) {
        Booking booking = bookings.getEntity(bookingId);
        Room room = rooms.getEntityForUpdate(booking.getRoom().getId());
        if (booking.getStatus() != BookingStatus.BOOKED && booking.getStatus() != BookingStatus.CONFIRMED
                && booking.getStatus() != BookingStatus.CHECKED_IN) {
            throw new BusinessRuleException("Продлить можно только активное бронирование.");
        }
        if (!request.newCheckOutDate().isAfter(booking.getCheckOutDate())) {
            throw new BusinessRuleException("Новая дата выезда должна быть позже текущей даты выезда.");
        }
        if (!request.newCheckOutDate().isAfter(LocalDate.now())) {
            throw new BusinessRuleException("Новая дата выезда должна быть в будущем.");
        }
        bookings.ensureNoOverlap(room.getId(), booking.getCheckOutDate(), request.newCheckOutDate(), booking.getId());
        booking.setCheckOutDate(request.newCheckOutDate());
        booking.setTotalPrice(bookings.calculatePrice(room, booking.getCheckInDate(), booking.getCheckOutDate()));
        return bookings.toResponse(booking);
    }

    // Operation 7: allow reception staff to mark a cleaned room as available again.
    public void finishCleaning(Long roomId) {
        Room room = rooms.getEntityForUpdate(roomId);
        if (room.getStatus() != RoomStatus.CLEANING) {
            throw new BusinessRuleException("Завершить уборку можно только для номера со статусом CLEANING.");
        }
        room.setStatus(RoomStatus.AVAILABLE);
    }
}
