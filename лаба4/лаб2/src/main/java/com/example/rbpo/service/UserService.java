package com.example.rbpo.service;

import com.example.rbpo.model.User;
import com.example.rbpo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public List<User> findAll() {
        return repository.findAll();
    }

    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    public User create(User user) {
        return repository.save(user);
    }

    public User update(Long id, User updatedData) {
        return repository.findById(id).map(user -> {
            user.setName(updatedData.getName());
            user.setEmail(updatedData.getEmail());
            return repository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}