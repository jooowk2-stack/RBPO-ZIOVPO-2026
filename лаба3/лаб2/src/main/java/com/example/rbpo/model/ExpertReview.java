package com.example.rbpo.model;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "application_id", nullable = false)
    private ProjectApplication application;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "expert_id", nullable = false)
    private User expert;

    @Column(nullable = false)
    private Integer score;

    private String comment;
}