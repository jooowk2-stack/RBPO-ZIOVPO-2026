package ru.student.hotel.dto;

import ru.student.hotel.model.BookingStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record BookingResponse(
        Long id,
        Long customerId,
        String customerName,
        Long roomId,
        String roomNumber,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        Integer guests,
        BookingStatus status,
        BigDecimal totalPrice,
        Instant createdAt
) { }
