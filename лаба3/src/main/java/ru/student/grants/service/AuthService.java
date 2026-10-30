package ru.student.grants.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.student.grants.dto.RegisterRequest;
import ru.student.grants.exception.BusinessRuleException;
import ru.student.grants.model.Role;
import ru.student.grants.model.User;
import ru.student.grants.repository.UserRepository;

import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessRuleException("Пользователь с таким именем уже занят");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessRuleException("Email уже используется");
        }

        // Проверка надежности пароля (мин. 8 символов, наличие цифры и буквы)
        if (request.getPassword() == null || request.getPassword().length() < 8 ||
                !request.getPassword().matches(".*\\d.*") || !request.getPassword().matches(".*[a-zA-Z].*")) {
            throw new BusinessRuleException("Пароль слишком простой. Нужно минимум 8 символов, латинские буквы и цифры.");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // Хэширование через BCrypt
                .roles(Set.of(Role.ROLE_APPLICANT)) // Строго базовая роль, повышение привилегий заблокировано
                .build();

        userRepository.save(user);
    }
}