package ru.student.grants.model;

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

    private String title;
    private String description;
    private Double budget;

    private LocalDateTime submissionStartDate;
    private LocalDateTime submissionEndDate;
    private LocalDateTime evaluationStartDate;
    private LocalDateTime evaluationEndDate;

    private String status; // ACTIVE, CLOSED
}