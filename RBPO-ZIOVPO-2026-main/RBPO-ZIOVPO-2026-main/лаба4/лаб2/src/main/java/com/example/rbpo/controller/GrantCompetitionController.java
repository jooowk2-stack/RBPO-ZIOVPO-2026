package com.example.rbpo.controller;

import com.example.rbpo.model.GrantCompetition;
import com.example.rbpo.service.GrantCompetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grant-competitions")
public class GrantCompetitionController {

    @Autowired
    private GrantCompetitionService service;

    @GetMapping
    public ResponseEntity<List<GrantCompetition>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GrantCompetition> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
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