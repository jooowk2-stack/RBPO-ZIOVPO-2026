package ru.student.hotel.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.student.hotel.dto.CustomerRequest;
import ru.student.hotel.dto.CustomerResponse;
import ru.student.hotel.service.CustomerService;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService service;
    public CustomerController(CustomerService service) { this.service = service; }

    @GetMapping public List<CustomerResponse> findAll() { return service.findAll(); }
    @GetMapping("/{id}") public CustomerResponse findById(@PathVariable Long id) { return service.findById(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse create(@Valid @RequestBody CustomerRequest request) { return service.create(request); }
    @PutMapping("/{id}") public CustomerResponse update(@PathVariable Long id, @Valid @RequestBody CustomerRequest request) { return service.update(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
