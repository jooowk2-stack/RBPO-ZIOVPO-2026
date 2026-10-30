package com.example.rbpo.repository;
import com.example.rbpo.model.ExpertReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpertReviewRepository extends JpaRepository<ExpertReview, Long> {}