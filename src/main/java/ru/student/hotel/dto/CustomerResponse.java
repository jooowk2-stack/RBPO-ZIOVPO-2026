package ru.student.hotel.dto;

import java.time.Instant;

public record CustomerResponse(Long id, String fullName, String email, String phone, Instant createdAt) { }
