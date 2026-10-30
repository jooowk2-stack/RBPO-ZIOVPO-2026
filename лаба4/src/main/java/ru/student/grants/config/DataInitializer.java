package ru.student.grants.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.student.grants.model.Role;
import ru.student.grants.model.User;
import ru.student.grants.repository.UserRepository;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Создаем админа, если его еще нет
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = User.builder()
                        .username("admin")
                        .email("admin@grants.ru")
                        .password(passwordEncoder.encode("admin123"))
                        .roles(Set.of(Role.ROLE_ADMIN))
                        .build();
                userRepository.save(admin);
            }

            // Создаем эксперта, если его еще нет
            if (userRepository.findByUsername("expert").isEmpty()) {
                User expert = User.builder()
                        .username("expert")
                        .email("expert@grants.ru")
                        .password(passwordEncoder.encode("expert123"))
                        .roles(Set.of(Role.ROLE_EXPERT))
                        .build();
                userRepository.save(expert);
            }

            // Создаем заявителя, если его еще нет
            if (userRepository.findByUsername("applicant").isEmpty()) {
                User applicant = User.builder()
                        .username("applicant")
                        .email("applicant@grants.ru")
                        .password(passwordEncoder.encode("applicant123"))
                        .roles(Set.of(Role.ROLE_APPLICANT))
                        .build();
                userRepository.save(applicant);
            }
        };
    }
}