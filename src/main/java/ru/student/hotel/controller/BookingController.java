package ru.student.hotel.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.student.hotel.dto.BookingRequest;
import ru.student.hotel.dto.BookingResponse;
import ru.student.hotel.service.BookingService;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService service;
    public BookingController(BookingService service) { this.service = service; }

    @GetMapping public List<BookingResponse> findAll() { return service.findAll(); }
    @GetMapping("/{id}") public BookingResponse findById(@PathVariable Long id) { return service.findById(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse create(@Valid @RequestBody BookingRequest request) { return service.create(request); }
    @PutMapping("/{id}") public BookingResponse update(@PathVariable Long id, @Valid @RequestBody BookingRequest request) { return service.update(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
