package com.example.grants.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "grant_competitions")
@Data
public class GrantCompetition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title; // Название конкурса

    @Column(nullable = false)
    private Double budget; // Бюджет

    @Column(nullable = false)
    private LocalDateTime submissionDeadline; // Срок подачи заявок

    @Column(nullable = false)
    private LocalDateTime evaluationDeadline; // Срок оценки

    @Column(nullable = false)
    private String status; // Статус (например, "OPEN", "CLOSED")
}