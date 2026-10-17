package ru.student.hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.student.hotel.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmailIgnoreCase(String email);
    java.util.Optional<Customer> findByEmailIgnoreCase(String email);
}
