package ru.student.hotel.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ExtendBookingRequest(
        @NotNull(message = "Укажите новую дату выезда") LocalDate newCheckOutDate
) { }
