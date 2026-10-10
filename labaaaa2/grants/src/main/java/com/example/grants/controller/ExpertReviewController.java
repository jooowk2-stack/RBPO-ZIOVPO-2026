package com.example.grants.controller;

import com.example.grants.model.ExpertReview;
import com.example.grants.service.ExpertReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expert-reviews")
public class ExpertReviewController {

    private final ExpertReviewService service;

    public ExpertReviewController(ExpertReviewService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ExpertReview>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpertReview> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<ExpertReview> create(@RequestBody ExpertReview review) {
        ExpertReview created = service.create(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ExpertReview> update(@PathVariable Long id, @RequestBody ExpertReview review) {
        try {
            ExpertReview updated = service.update(id, review);
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