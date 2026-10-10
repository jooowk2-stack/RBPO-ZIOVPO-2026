package com.example.grants.service;

import com.example.grants.model.GrantCompetition;
import com.example.grants.repository.GrantCompetitionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GrantCompetitionService {

    private final GrantCompetitionRepository repository;

    public GrantCompetitionService(GrantCompetitionRepository repository) {
        this.repository = repository;
    }

    public List<GrantCompetition> getAll() {
        return repository.findAll();
    }

    public Optional<GrantCompetition> getById(Long id) {
        return repository.findById(id);
    }

    public GrantCompetition create(GrantCompetition competition) {
        return repository.save(competition);
    }

    public GrantCompetition update(Long id, GrantCompetition updated) {
        GrantCompetition competition = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Competition not found"));

        if (updated.getTitle() != null) competition.setTitle(updated.getTitle());
        if (updated.getBudget() != null) competition.setBudget(updated.getBudget());
        if (updated.getSubmissionDeadline() != null) competition.setSubmissionDeadline(updated.getSubmissionDeadline());
        if (updated.getEvaluationDeadline() != null) competition.setEvaluationDeadline(updated.getEvaluationDeadline());
        if (updated.getStatus() != null) competition.setStatus(updated.getStatus());

        return repository.save(competition);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}