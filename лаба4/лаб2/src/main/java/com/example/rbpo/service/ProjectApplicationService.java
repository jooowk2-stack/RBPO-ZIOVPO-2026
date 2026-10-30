package com.example.rbpo.service;

import com.example.rbpo.model.ProjectApplication;
import com.example.rbpo.repository.ProjectApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectApplicationService {

    @Autowired
    private ProjectApplicationRepository repository;

    public List<ProjectApplication> findAll() {
        return repository.findAll();
    }

    public Optional<ProjectApplication> findById(Long id) {
        return repository.findById(id);
    }

    public ProjectApplication create(ProjectApplication application) {
        return repository.save(application);
    }

    public ProjectApplication update(Long id, ProjectApplication updatedData) {
        return repository.findById(id).map(application -> {
            application.setCompetition(updatedData.getCompetition());
            application.setAuthor(updatedData.getAuthor());
            application.setRequestedAmount(updatedData.getRequestedAmount());
            application.setStatus(updatedData.getStatus());
            return repository.save(application);
        }).orElseThrow(() -> new RuntimeException("ProjectApplication not found with id " + id));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}