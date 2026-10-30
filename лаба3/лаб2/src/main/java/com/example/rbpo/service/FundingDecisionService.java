package com.example.rbpo.service;

import com.example.rbpo.model.FundingDecision;
import com.example.rbpo.repository.FundingDecisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FundingDecisionService {

    @Autowired
    private FundingDecisionRepository repository;

    public List<FundingDecision> findAll() {
        return repository.findAll();
    }

    public Optional<FundingDecision> findById(Long id) {
        return repository.findById(id);
    }

    public FundingDecision create(FundingDecision decision) {
        return repository.save(decision);
    }

    public FundingDecision update(Long id, FundingDecision updatedData) {
        return repository.findById(id).map(decision -> {
            decision.setApplication(updatedData.getApplication());
            decision.setApprovedAmount(updatedData.getApprovedAmount());
            decision.setStatus(updatedData.getStatus());
            return repository.save(decision);
        }).orElseThrow(() -> new RuntimeException("FundingDecision not found with id " + id));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}