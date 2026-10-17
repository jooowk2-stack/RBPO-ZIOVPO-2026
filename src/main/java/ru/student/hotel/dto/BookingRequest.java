package ru.student.hotel.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record BookingRequest(
        @NotNull(message = "Укажите ID клиента") Long customerId,
        @NotNull(message = "Укажите ID номера") Long roomId,
        @NotNull(message = "Укажите дату заезда") LocalDate checkInDate,
        @NotNull(message = "Укажите дату выезда") LocalDate checkOutDate,
        @NotNull(message = "Укажите количество гостей") @Min(value = 1, message = "Количество гостей должно быть не меньше 1") Integer guests
) { }
