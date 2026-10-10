package com.example.grants.repository;

import com.example.grants.model.ExpertReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpertReviewRepository extends JpaRepository<ExpertReview, Long> {
}