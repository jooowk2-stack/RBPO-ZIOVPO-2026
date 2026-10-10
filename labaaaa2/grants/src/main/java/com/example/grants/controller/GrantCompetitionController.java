package com.example.grants.controller;

import com.example.grants.model.GrantCompetition;
import com.example.grants.service.GrantCompetitionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grant-competitions")
public class GrantCompetitionController {

    private final GrantCompetitionService service;

    public GrantCompetitionController(GrantCompetitionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<GrantCompetition>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GrantCompetition> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<GrantCompetition> create(@RequestBody GrantCompetition competition) {
        GrantCompetition created = service.create(competition);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GrantCompetition> update(@PathVariable Long id, @RequestBody GrantCompetition competition) {
        try {
            GrantCompetition updated = service.update(id, competition);
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