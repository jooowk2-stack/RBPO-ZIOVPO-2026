package ru.student.hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.student.hotel.model.Booking;
import ru.student.hotel.model.BookingStatus;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByOrderByCreatedAtDesc();

    @Query("select (count(b) > 0) from Booking b " +
           "where b.room.id = :roomId and b.status in :statuses " +
           "and b.checkInDate < :checkOutDate and b.checkOutDate > :checkInDate")
    boolean existsOverlappingBooking(@Param("roomId") Long roomId,
                                     @Param("checkInDate") LocalDate checkInDate,
                                     @Param("checkOutDate") LocalDate checkOutDate,
                                     @Param("statuses") Collection<BookingStatus> statuses);

    @Query("select (count(b) > 0) from Booking b " +
           "where b.room.id = :roomId and b.id <> :excludedId and b.status in :statuses " +
           "and b.checkInDate < :checkOutDate and b.checkOutDate > :checkInDate")
    boolean existsOverlappingBookingExcluding(@Param("roomId") Long roomId,
                                              @Param("checkInDate") LocalDate checkInDate,
                                              @Param("checkOutDate") LocalDate checkOutDate,
                                              @Param("statuses") Collection<BookingStatus> statuses,
                                              @Param("excludedId") Long excludedId);
}
