package com.example.rbpo.controller;

import com.example.rbpo.model.FundingDecision;
import com.example.rbpo.service.FundingDecisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/funding-decisions")
public class FundingDecisionController {

    @Autowired
    private FundingDecisionService service;

    @GetMapping
    public ResponseEntity<List<FundingDecision>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FundingDecision> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
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