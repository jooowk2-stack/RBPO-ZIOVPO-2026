package com.example.rbpo.repository;
import com.example.rbpo.model.GrantCompetition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrantCompetitionRepository extends JpaRepository<GrantCompetition, Long> {}