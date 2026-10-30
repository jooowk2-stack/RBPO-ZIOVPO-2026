package com.example.rbpo.controller;

import com.example.rbpo.model.ExpertReview;
import com.example.rbpo.service.ExpertReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expert-reviews")
public class ExpertReviewController {

    @Autowired
    private ExpertReviewService service;

    @GetMapping
    public ResponseEntity<List<ExpertReview>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpertReview> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
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