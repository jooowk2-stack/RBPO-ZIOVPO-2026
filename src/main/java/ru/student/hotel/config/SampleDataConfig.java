package ru.student.hotel.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.student.hotel.model.Booking;
import ru.student.hotel.model.BookingStatus;
import ru.student.hotel.model.Customer;
import ru.student.hotel.model.Room;
import ru.student.hotel.model.RoomStatus;
import ru.student.hotel.model.RoomType;
import ru.student.hotel.repository.BookingRepository;
import ru.student.hotel.repository.CustomerRepository;
import ru.student.hotel.repository.RoomRepository;

import java.math.BigDecimal;
import java.util.List;
import java.time.LocalDate;

@Configuration
public class SampleDataConfig {
    @Bean
    ApplicationRunner seedSampleData(CustomerRepository customers, RoomRepository rooms, BookingRepository bookings) {
        return args -> {
            List<SampleCustomer> sampleCustomers = List.of(
                    new SampleCustomer("Анна Смирнова", "anna.smirnova@example.com", "+7-900-111-22-33"),
                    new SampleCustomer("Иван Петров", "ivan.petrov@example.com", "+7-900-222-33-44"),
                    new SampleCustomer("Мария Иванова", "maria.ivanova@example.com", "+7-900-333-44-55")
            );
            for (SampleCustomer sample : sampleCustomers) {
                if (!customers.existsByEmailIgnoreCase(sample.email())) {
                    Customer customer = new Customer();
                    customer.setFullName(sample.name());
                    customer.setEmail(sample.email());
                    customer.setPhone(sample.phone());
                    customers.save(customer);
                }
            }

            List<SampleRoom> sampleRooms = List.of(
                    new SampleRoom("101", RoomType.SINGLE, 1, "75.00"),
                    new SampleRoom("102", RoomType.DOUBLE, 2, "110.00"),
                    new SampleRoom("201", RoomType.DELUXE, 3, "180.00"),
                    new SampleRoom("301", RoomType.SUITE, 4, "260.00")
            );
            for (SampleRoom sample : sampleRooms) {
                if (!rooms.existsByNumberIgnoreCase(sample.number())) {
                    Room room = new Room();
                    room.setNumber(sample.number());
                    room.setType(sample.type());
                    room.setCapacity(sample.capacity());
                    room.setPricePerNight(new BigDecimal(sample.price()));
                    room.setStatus(RoomStatus.AVAILABLE);
                    rooms.save(room);
                }
            }

            // Two non-overlapping confirmed reservations ensure the bookings table has sample records too.
            if (bookings.count() == 0) {
                LocalDate today = LocalDate.now();
                seedBooking(bookings,
                        customers.findByEmailIgnoreCase("anna.smirnova@example.com").orElseThrow(),
                        rooms.findByNumberIgnoreCase("101").orElseThrow(),
                        today.plusDays(30), today.plusDays(32));
                seedBooking(bookings,
                        customers.findByEmailIgnoreCase("ivan.petrov@example.com").orElseThrow(),
                        rooms.findByNumberIgnoreCase("102").orElseThrow(),
                        today.plusDays(40), today.plusDays(43));
            }
        };
    }


    private void seedBooking(BookingRepository bookings, Customer customer, Room room, LocalDate checkIn, LocalDate checkOut) {
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setRoom(room);
        booking.setCheckInDate(checkIn);
        booking.setCheckOutDate(checkOut);
        booking.setGuests(1);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setTotalPrice(room.getPricePerNight().multiply(BigDecimal.valueOf(java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut))));
        bookings.save(booking);
    }

    private record SampleCustomer(String name, String email, String phone) { }
    private record SampleRoom(String number, RoomType type, int capacity, String price) { }
}
