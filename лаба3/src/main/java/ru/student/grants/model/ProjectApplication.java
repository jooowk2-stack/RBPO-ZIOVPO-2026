package ru.student.grants.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "project_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "competition_id")
    private GrantCompetition competition;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    private Double requestedAmount;
    private String status; // PENDING, FUNDED, REJECTED, REJECTED_EXCESS_BUDGET
}