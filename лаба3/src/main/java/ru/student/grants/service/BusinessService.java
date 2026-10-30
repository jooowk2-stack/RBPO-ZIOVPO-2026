package ru.student.grants.service;

import ru.student.grants.model.*;
import ru.student.grants.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BusinessService {

    @Autowired
    private GrantCompetitionRepository competitionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectApplicationRepository applicationRepository;
    @Autowired
    private ExpertReviewRepository reviewRepository;
    @Autowired
    private FundingDecisionRepository fundingDecisionRepository;

    // 1. Подача проектной заявки на конкурс (проверка сроков конкурса)
    @Transactional
    public ProjectApplication submitApplication(Long competitionId, Long authorId, Double requestedAmount) {
        GrantCompetition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new RuntimeException("Конкурс не найден"));
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Автор не найден"));

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(competition.getSubmissionStartDate()) || now.isAfter(competition.getSubmissionEndDate())) {
            throw new IllegalStateException("Сроки подачи заявок на этот конкурс завершены или еще не начались.");
        }

        ProjectApplication application = ProjectApplication.builder()
                .competition(competition)
                .author(author)
                .requestedAmount(requestedAmount)
                .status("PENDING")
                .build();

        return applicationRepository.save(application);
    }

    // 2. Добавление экспертной оценки заявки
    @Transactional
    public ExpertReview addExpertReview(Long applicationId, Long expertId, Integer score, String comment) {
        ProjectApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Заявка не найдена"));
        User expert = userRepository.findById(expertId)
                .orElseThrow(() -> new RuntimeException("Эксперт не найден"));

        ExpertReview review = ExpertReview.builder()
                .application(application)
                .expert(expert)
                .score(score)
                .comment(comment)
                .build();

        return reviewRepository.save(review);
    }

    // 3. Вынесение решения о финансировании
    @Transactional
    public FundingDecision makeFundingDecision(Long applicationId, Double approvedAmount, String status) {
        ProjectApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Заявка не найдена"));

        FundingDecision decision = FundingDecision.builder()
                .application(application)
                .approvedAmount(approvedAmount)
                .status(status)
                .build();

        application.setStatus(status.equals("APPROVED") ? "FUNDED" : "REJECTED");
        applicationRepository.save(application);

        return fundingDecisionRepository.save(decision);
    }

    // 4. Закрытие грантового конкурса
    @Transactional
    public GrantCompetition closeCompetition(Long competitionId) {
        GrantCompetition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new RuntimeException("Конкурс не найден"));

        competition.setStatus("CLOSED");
        return competitionRepository.save(competition);
    }

    // 5. Аудит и автоматическое отклонение заявок, превышающих бюджет конкурса
    @Transactional
    public String auditAndRejectExcessiveApplications(Long competitionId) {
        GrantCompetition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new RuntimeException("Конкурс не найден"));

        var applications = applicationRepository.findAll().stream()
                .filter(app -> app.getCompetition().getId().equals(competitionId))
                .toList();

        int rejectedCount = 0;
        for (ProjectApplication app : applications) {
            if (app.getRequestedAmount() > competition.getBudget()) {
                app.setStatus("REJECTED_EXCESS_BUDGET");
                applicationRepository.save(app);
                rejectedCount++;
            }
        }
        return "Проверка завершена. Отклонено заявок из-за превышения бюджета: " + rejectedCount;
    }

    // 6. Получение списка всех конкурсов
    public List<GrantCompetition> getAllCompetitions() {
        return competitionRepository.findAll();
    }
}