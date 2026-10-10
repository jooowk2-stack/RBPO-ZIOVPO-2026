package com.example.grants.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "expert_reviews")
@Data
public class ExpertReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ссылка на проектную заявку
    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private ProjectApplication application;

    // Ссылка на эксперта (пользователя)
    @ManyToOne
    @JoinColumn(name = "expert_id", nullable = false)
    private User expert;

    @Column(nullable = false)
    private Integer score; // Балл

    @Column
    private String comment; // Комментарий
}