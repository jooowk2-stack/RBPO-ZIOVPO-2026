package com.example.grants.repository;

import com.example.grants.model.FundingDecision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FundingDecisionRepository extends JpaRepository<FundingDecision, Long> {
}