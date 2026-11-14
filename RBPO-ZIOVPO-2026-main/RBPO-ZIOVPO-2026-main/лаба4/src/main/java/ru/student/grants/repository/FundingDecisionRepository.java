package ru.student.grants.repository;
import ru.student.grants.model.FundingDecision;
import org.springframework.data.jpa.repository.JpaRepository;
public interface FundingDecisionRepository extends JpaRepository<FundingDecision, Long> {}