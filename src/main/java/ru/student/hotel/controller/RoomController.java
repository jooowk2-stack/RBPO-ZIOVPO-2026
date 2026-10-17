package ru.student.hotel.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.student.hotel.dto.RoomRequest;
import ru.student.hotel.dto.RoomResponse;
import ru.student.hotel.service.RoomService;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService service;
    public RoomController(RoomService service) { this.service = service; }

    @GetMapping public List<RoomResponse> findAll() { return service.findAll(); }
    @GetMapping("/{id}") public RoomResponse findById(@PathVariable Long id) { return service.findById(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public RoomResponse create(@Valid @RequestBody RoomRequest request) { return service.create(request); }
    @PutMapping("/{id}") public RoomResponse update(@PathVariable Long id, @Valid @RequestBody RoomRequest request) { return service.update(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
