package ru.student.hotel.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomerRequest(
        @NotBlank(message = "Укажите ФИО клиента") @Size(max = 120) String fullName,
        @NotBlank(message = "Укажите email") @Email(message = "Email имеет неверный формат") @Size(max = 180) String email,
        @Size(max = 30, message = "Телефон должен быть не длиннее 30 символов") String phone
) { }
