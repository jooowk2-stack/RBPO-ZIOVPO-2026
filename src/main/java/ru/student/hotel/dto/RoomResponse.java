package ru.student.hotel.dto;

import ru.student.hotel.model.RoomStatus;
import ru.student.hotel.model.RoomType;
import java.math.BigDecimal;

public record RoomResponse(Long id, String number, RoomType type, Integer capacity,
                           BigDecimal pricePerNight, RoomStatus status) { }
