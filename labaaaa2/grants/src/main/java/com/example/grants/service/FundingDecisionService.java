package com.example.grants.service;

import com.example.grants.model.FundingDecision;
import com.example.grants.repository.FundingDecisionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FundingDecisionService {

    private final FundingDecisionRepository repository;

    public FundingDecisionService(FundingDecisionRepository repository) {
        this.repository = repository;
    }

    public List<FundingDecision> getAll() {
        return repository.findAll();
    }

    public Optional<FundingDecision> getById(Long id) {
        return repository.findById(id);
    }

    public FundingDecision create(FundingDecision decision) {
        return repository.save(decision);
    }

    public FundingDecision update(Long id, FundingDecision updated) {
        FundingDecision decision = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Decision not found"));

        if (updated.getApprovedAmount() != null) decision.setApprovedAmount(updated.getApprovedAmount());
        if (updated.getStatus() != null) decision.setStatus(updated.getStatus());
        if (updated.getApplication() != null) decision.setApplication(updated.getApplication());

        return repository.save(decision);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}