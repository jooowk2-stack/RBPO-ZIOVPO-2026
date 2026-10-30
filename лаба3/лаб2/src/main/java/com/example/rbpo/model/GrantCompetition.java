package com.example.rbpo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "grant_competitions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrantCompetition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private Double budget;

    @Column(nullable = false)
    private LocalDateTime submissionStartDate;

    @Column(nullable = false)
    private LocalDateTime submissionEndDate;

    @Column(nullable = false)
    private LocalDateTime evaluationStartDate;

    @Column(nullable = false)
    private LocalDateTime evaluationEndDate;

    @Column(nullable = false)
    private String status;
}