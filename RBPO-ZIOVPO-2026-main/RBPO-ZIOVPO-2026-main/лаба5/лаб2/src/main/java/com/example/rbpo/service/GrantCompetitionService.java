package com.example.rbpo.service;

import com.example.rbpo.model.GrantCompetition;
import com.example.rbpo.repository.GrantCompetitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GrantCompetitionService {

    @Autowired
    private GrantCompetitionRepository repository;

    public List<GrantCompetition> findAll() {
        return repository.findAll();
    }

    public Optional<GrantCompetition> findById(Long id) {
        return repository.findById(id);
    }

    public GrantCompetition create(GrantCompetition competition) {
        return repository.save(competition);
    }

    public GrantCompetition update(Long id, GrantCompetition updatedData) {
        return repository.findById(id).map(competition -> {
            competition.setTitle(updatedData.getTitle());
            competition.setDescription(updatedData.getDescription());
            competition.setBudget(updatedData.getBudget());
            competition.setSubmissionStartDate(updatedData.getSubmissionStartDate());
            competition.setSubmissionEndDate(updatedData.getSubmissionEndDate());
            competition.setEvaluationStartDate(updatedData.getEvaluationStartDate());
            competition.setEvaluationEndDate(updatedData.getEvaluationEndDate());
            competition.setStatus(updatedData.getStatus());
            return repository.save(competition);
        }).orElseThrow(() -> new RuntimeException("GrantCompetition not found with id " + id));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}