package ru.student.hotel.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import ru.student.hotel.model.RoomStatus;
import ru.student.hotel.model.RoomType;
import java.math.BigDecimal;

public record RoomRequest(
        @NotBlank(message = "Укажите номер комнаты")
        @Size(max = 12, message = "Номер комнаты должен быть не длиннее 12 символов")
        @Pattern(regexp = "[A-Za-z0-9-]+", message = "Номер комнаты может содержать латинские буквы, цифры и дефис")
        String number,
        @NotNull(message = "Укажите тип номера") RoomType type,
        @NotNull(message = "Укажите вместимость") @Min(value = 1, message = "Вместимость должна быть не меньше 1") Integer capacity,
        @NotNull(message = "Укажите цену за ночь") @DecimalMin(value = "0.01", message = "Цена должна быть больше нуля") BigDecimal pricePerNight,
        RoomStatus status
) { }
