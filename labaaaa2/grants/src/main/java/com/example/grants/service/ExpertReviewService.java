package com.example.grants.service;

import com.example.grants.model.ExpertReview;
import com.example.grants.repository.ExpertReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpertReviewService {

    private final ExpertReviewRepository repository;

    public ExpertReviewService(ExpertReviewRepository repository) {
        this.repository = repository;
    }

    public List<ExpertReview> getAll() {
        return repository.findAll();
    }

    public Optional<ExpertReview> getById(Long id) {
        return repository.findById(id);
    }

    public ExpertReview create(ExpertReview review) {
        return repository.save(review);
    }

    public ExpertReview update(Long id, ExpertReview updated) {
        ExpertReview review = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (updated.getScore() != null) review.setScore(updated.getScore());
        if (updated.getComment() != null) review.setComment(updated.getComment());
        if (updated.getApplication() != null) review.setApplication(updated.getApplication());
        if (updated.getExpert() != null) review.setExpert(updated.getExpert());

        return repository.save(review);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}