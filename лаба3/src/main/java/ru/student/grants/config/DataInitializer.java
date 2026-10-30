package ru.student.grants.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.student.grants.model.GrantCompetition;
import ru.student.grants.model.User;
import ru.student.grants.repository.GrantCompetitionRepository;
import ru.student.grants.repository.UserRepository;

import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, GrantCompetitionRepository competitionRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                User expert = User.builder()
                        .name("Иван Экспертов")
                        .email("expert@example.com")
                        .build();

                User author = User.builder()
                        .name("Петр Ученый")
                        .email("author@example.com")
                        .build();

                userRepository.save(expert);
                userRepository.save(author);
            }

            if (competitionRepository.count() == 0) {
                GrantCompetition competition = GrantCompetition.builder()
                        .title("ИТ-Грант 2026")
                        .description("Разработка ПО и систем безопасности")
                        .budget(1500000.0)
                        .submissionStartDate(LocalDateTime.now().minusDays(5))
                        .submissionEndDate(LocalDateTime.now().plusDays(30))
                        .evaluationStartDate(LocalDateTime.now().plusDays(31))
                        .evaluationEndDate(LocalDateTime.now().plusDays(60))
                        .status("ACTIVE")
                        .build();

                competitionRepository.save(competition);
            }
        };
    }
}