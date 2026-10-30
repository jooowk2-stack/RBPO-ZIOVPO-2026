package com.example.rbpo.repository;
import com.example.rbpo.model.FundingDecision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FundingDecisionRepository extends JpaRepository<FundingDecision, Long> {}