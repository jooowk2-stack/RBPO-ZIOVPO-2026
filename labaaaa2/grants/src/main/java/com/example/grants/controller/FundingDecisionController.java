package com.example.grants.controller;

import com.example.grants.model.FundingDecision;
import com.example.grants.service.FundingDecisionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/funding-decisions")
public class FundingDecisionController {

    private final FundingDecisionService service;

    public FundingDecisionController(FundingDecisionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<FundingDecision>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FundingDecision> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<FundingDecision> create(@RequestBody FundingDecision decision) {
        FundingDecision created = service.create(decision);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FundingDecision> update(@PathVariable Long id, @RequestBody FundingDecision decision) {
        try {
            FundingDecision updated = service.update(id, decision);
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