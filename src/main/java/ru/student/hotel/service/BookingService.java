package ru.student.hotel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.student.hotel.dto.BookingRequest;
import ru.student.hotel.dto.BookingResponse;
import ru.student.hotel.exception.BusinessRuleException;
import ru.student.hotel.exception.ResourceNotFoundException;
import ru.student.hotel.model.Booking;
import ru.student.hotel.model.BookingStatus;
import ru.student.hotel.model.Customer;
import ru.student.hotel.model.Room;
import ru.student.hotel.model.RoomStatus;
import ru.student.hotel.repository.BookingRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class BookingService {
    private static final List<BookingStatus> BLOCKING_STATUSES =
            List.of(BookingStatus.BOOKED, BookingStatus.CONFIRMED, BookingStatus.CHECKED_IN);

    private final BookingRepository bookings;
    private final CustomerService customers;
    private final RoomService rooms;

    public BookingService(BookingRepository bookings, CustomerService customers, RoomService rooms) {
        this.bookings = bookings;
        this.customers = customers;
        this.rooms = rooms;
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> findAll() {
        return bookings.findAllByOrderByCreatedAtDesc().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public BookingResponse findById(Long id) { return toResponse(getEntity(id)); }

    public BookingResponse create(BookingRequest request) {
        validateDates(request.checkInDate(), request.checkOutDate(), true);
        Customer customer = customers.getEntity(request.customerId());
        Room room = rooms.getEntityForUpdate(request.roomId());
        validateRoomAndGuests(room, request.guests());
        if (room.getStatus() == RoomStatus.MAINTENANCE || room.getStatus() == RoomStatus.CLEANING) {
            throw new BusinessRuleException("Номер " + room.getNumber() + " сейчас нельзя бронировать (статус " + room.getStatus() + ").");
        }
        ensureNoOverlap(room.getId(), request.checkInDate(), request.checkOutDate(), null);

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setRoom(room);
        booking.setCheckInDate(request.checkInDate());
        booking.setCheckOutDate(request.checkOutDate());
        booking.setGuests(request.guests());
        booking.setStatus(BookingStatus.BOOKED);
        booking.setTotalPrice(calculatePrice(room, request.checkInDate(), request.checkOutDate()));
        return toResponse(bookings.save(booking));
    }

    public BookingResponse update(Long id, BookingRequest request) {
        Booking booking = getEntity(id);
        ensureEditable(booking);
        validateDates(request.checkInDate(), request.checkOutDate(), true);
        Customer customer = customers.getEntity(request.customerId());
        Room room = rooms.getEntityForUpdate(request.roomId());
        validateRoomAndGuests(room, request.guests());
        if (room.getStatus() == RoomStatus.MAINTENANCE || room.getStatus() == RoomStatus.CLEANING) {
            throw new BusinessRuleException("Номер " + room.getNumber() + " сейчас нельзя бронировать.");
        }
        ensureNoOverlap(room.getId(), request.checkInDate(), request.checkOutDate(), booking.getId());
        booking.setCustomer(customer);
        booking.setRoom(room);
        booking.setCheckInDate(request.checkInDate());
        booking.setCheckOutDate(request.checkOutDate());
        booking.setGuests(request.guests());
        booking.setTotalPrice(calculatePrice(room, request.checkInDate(), request.checkOutDate()));
        return toResponse(bookings.save(booking));
    }

    public void delete(Long id) {
        Booking booking = getEntity(id);
        if (booking.getStatus() == BookingStatus.CHECKED_IN || booking.getStatus() == BookingStatus.CHECKED_OUT) {
            throw new BusinessRuleException("Нельзя удалить заселённое или завершённое бронирование: сохраните историю проживания.");
        }
        bookings.delete(booking);
    }

    public Booking getEntity(Long id) {
        return bookings.findById(id).orElseThrow(() -> new ResourceNotFoundException("Бронирование с ID " + id + " не найдено."));
    }

    public BookingResponse toResponse(Booking b) {
        return new BookingResponse(
                b.getId(),
                b.getCustomer().getId(), b.getCustomer().getFullName(),
                b.getRoom().getId(), b.getRoom().getNumber(),
                b.getCheckInDate(), b.getCheckOutDate(), b.getGuests(), b.getStatus(), b.getTotalPrice(), b.getCreatedAt()
        );
    }

    public void ensureNoOverlap(Long roomId, LocalDate checkInDate, LocalDate checkOutDate, Long excludedBookingId) {
        boolean overlap = excludedBookingId == null
                ? bookings.existsOverlappingBooking(roomId, checkInDate, checkOutDate, BLOCKING_STATUSES)
                : bookings.existsOverlappingBookingExcluding(roomId, checkInDate, checkOutDate, BLOCKING_STATUSES, excludedBookingId);
        if (overlap) throw new BusinessRuleException("Этот номер уже забронирован на часть выбранного периода.");
    }

    public BigDecimal calculatePrice(Room room, LocalDate start, LocalDate end) {
        long nights = ChronoUnit.DAYS.between(start, end);
        return room.getPricePerNight().multiply(BigDecimal.valueOf(nights)).setScale(2, RoundingMode.HALF_UP);
    }

    public void validateDates(LocalDate start, LocalDate end, boolean requireNonPast) {
        if (start == null || end == null || !end.isAfter(start)) {
            throw new BusinessRuleException("Дата выезда должна быть позже даты заезда.");
        }
        if (requireNonPast && start.isBefore(LocalDate.now())) {
            throw new BusinessRuleException("Нельзя создать или изменить бронирование с датой заезда в прошлом.");
        }
    }

    private void validateRoomAndGuests(Room room, Integer guests) {
        if (guests == null || guests < 1) throw new BusinessRuleException("Количество гостей должно быть не меньше 1.");
        if (guests > room.getCapacity()) {
            throw new BusinessRuleException("Вместимость номера " + room.getNumber() + " — " + room.getCapacity() + ", гостей указано: " + guests + ".");
        }
    }

    private void ensureEditable(Booking booking) {
        if (booking.getStatus() != BookingStatus.BOOKED && booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new BusinessRuleException("Изменять можно только новое или подтверждённое бронирование.");
        }
    }
}
