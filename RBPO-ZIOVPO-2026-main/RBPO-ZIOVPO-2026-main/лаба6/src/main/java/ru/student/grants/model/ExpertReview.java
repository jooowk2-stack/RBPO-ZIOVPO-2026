package ru.student.grants.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "expert_reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpertReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "application_id")
    private ProjectApplication application;

    @ManyToOne
    @JoinColumn(name = "expert_id")
    private User expert;

    private Integer score;
    private String comment;
}