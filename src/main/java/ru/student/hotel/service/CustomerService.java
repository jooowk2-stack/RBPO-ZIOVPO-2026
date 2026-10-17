package ru.student.hotel.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.student.hotel.dto.CustomerRequest;
import ru.student.hotel.dto.CustomerResponse;
import ru.student.hotel.exception.BusinessRuleException;
import ru.student.hotel.exception.ResourceNotFoundException;
import ru.student.hotel.model.Customer;
import ru.student.hotel.repository.CustomerRepository;

import java.util.List;

@Service
@Transactional
public class CustomerService {
    private final CustomerRepository customers;

    public CustomerService(CustomerRepository customers) { this.customers = customers; }

    @Transactional(readOnly = true)
    public List<CustomerResponse> findAll() {
        return customers.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public CustomerResponse findById(Long id) { return toResponse(getEntity(id)); }

    public CustomerResponse create(CustomerRequest request) {
        String email = request.email().trim().toLowerCase();
        if (customers.existsByEmailIgnoreCase(email)) {
            throw new BusinessRuleException("Клиент с таким email уже существует.");
        }
        Customer customer = new Customer();
        apply(customer, request, email);
        return toResponse(customers.save(customer));
    }

    public CustomerResponse update(Long id, CustomerRequest request) {
        Customer customer = getEntity(id);
        String email = request.email().trim().toLowerCase();
        if (!email.equalsIgnoreCase(customer.getEmail()) && customers.existsByEmailIgnoreCase(email)) {
            throw new BusinessRuleException("Другой клиент уже использует этот email.");
        }
        apply(customer, request, email);
        return toResponse(customers.save(customer));
    }

    public void delete(Long id) {
        Customer customer = getEntity(id);
        try {
            customers.delete(customer);
            customers.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessRuleException("Нельзя удалить клиента, у которого есть бронирования. Сначала обработайте связанные бронирования.");
        }
    }

    public Customer getEntity(Long id) {
        return customers.findById(id).orElseThrow(() -> new ResourceNotFoundException("Клиент с ID " + id + " не найден."));
    }

    private void apply(Customer customer, CustomerRequest request, String normalizedEmail) {
        customer.setFullName(request.fullName().trim());
        customer.setEmail(normalizedEmail);
        customer.setPhone(request.phone() == null || request.phone().isBlank() ? null : request.phone().trim());
    }

    private CustomerResponse toResponse(Customer c) {
        return new CustomerResponse(c.getId(), c.getFullName(), c.getEmail(), c.getPhone(), c.getCreatedAt());
    }
}
