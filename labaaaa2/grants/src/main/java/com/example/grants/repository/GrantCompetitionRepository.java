package com.example.grants.repository;

import com.example.grants.model.GrantCompetition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrantCompetitionRepository extends JpaRepository<GrantCompetition, Long> {
}