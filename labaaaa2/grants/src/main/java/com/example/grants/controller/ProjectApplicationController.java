package com.example.grants.controller;

import com.example.grants.model.ProjectApplication;
import com.example.grants.service.ProjectApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project-applications")
public class ProjectApplicationController {

    private final ProjectApplicationService service;

    public ProjectApplicationController(ProjectApplicationService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProjectApplication>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectApplication> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<ProjectApplication> create(@RequestBody ProjectApplication application) {
        ProjectApplication created = service.create(application);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProjectApplication> update(@PathVariable Long id, @RequestBody ProjectApplication application) {
        try {
            ProjectApplication updated = service.update(id, application);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}