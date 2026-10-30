package com.example.rbpo.service;

import com.example.rbpo.model.ExpertReview;
import com.example.rbpo.repository.ExpertReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpertReviewService {

    @Autowired
    private ExpertReviewRepository repository;

    public List<ExpertReview> findAll() {
        return repository.findAll();
    }

    public Optional<ExpertReview> findById(Long id) {
        return repository.findById(id);
    }

    public ExpertReview create(ExpertReview review) {
        return repository.save(review);
    }

    public ExpertReview update(Long id, ExpertReview updatedData) {
        return repository.findById(id).map(review -> {
            review.setApplication(updatedData.getApplication());
            review.setExpert(updatedData.getExpert());
            review.setScore(updatedData.getScore());
            review.setComment(updatedData.getComment());
            return repository.save(review);
        }).orElseThrow(() -> new RuntimeException("ExpertReview not found with id " + id));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}