package ru.student.hotel.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.student.hotel.dto.BookingResponse;
import ru.student.hotel.dto.ExtendBookingRequest;
import ru.student.hotel.dto.ReservationRequest;
import ru.student.hotel.service.BusinessOperationService;

@RestController
@RequestMapping("/api/operations")
public class BusinessOperationController {
    private final BusinessOperationService service;
    public BusinessOperationController(BusinessOperationService service) { this.service = service; }

    @PostMapping("/reservations")
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse createReservation(@Valid @RequestBody ReservationRequest request) {
        return service.createReservation(request);
    }

    @PostMapping("/bookings/{id}/confirm")
    public BookingResponse confirm(@PathVariable Long id) { return service.confirm(id); }

    @PostMapping("/bookings/{id}/check-in")
    public BookingResponse checkIn(@PathVariable Long id) { return service.checkIn(id); }

    @PostMapping("/bookings/{id}/check-out")
    public BookingResponse checkOut(@PathVariable Long id) { return service.checkOut(id); }

    @PostMapping("/bookings/{id}/cancel")
    public BookingResponse cancel(@PathVariable Long id) { return service.cancel(id); }

    @PostMapping("/bookings/{id}/extend")
    public BookingResponse extend(@PathVariable Long id, @Valid @RequestBody ExtendBookingRequest request) {
        return service.extend(id, request);
    }

    @PostMapping("/rooms/{id}/finish-cleaning")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void finishCleaning(@PathVariable Long id) { service.finishCleaning(id); }
}
