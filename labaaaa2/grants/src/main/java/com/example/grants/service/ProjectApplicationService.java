package com.example.grants.service;

import com.example.grants.model.ProjectApplication;
import com.example.grants.repository.ProjectApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectApplicationService {

    private final ProjectApplicationRepository repository;

    public ProjectApplicationService(ProjectApplicationRepository repository) {
        this.repository = repository;
    }

    public List<ProjectApplication> getAll() {
        return repository.findAll();
    }

    public Optional<ProjectApplication> getById(Long id) {
        return repository.findById(id);
    }

    public ProjectApplication create(ProjectApplication application) {
        return repository.save(application);
    }

    public ProjectApplication update(Long id, ProjectApplication updated) {
        ProjectApplication app = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (updated.getRequestedAmount() != null) app.setRequestedAmount(updated.getRequestedAmount());
        if (updated.getStatus() != null) app.setStatus(updated.getStatus());
        if (updated.getCompetition() != null) app.setCompetition(updated.getCompetition());
        if (updated.getAuthor() != null) app.setAuthor(updated.getAuthor());

        return repository.save(app);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}