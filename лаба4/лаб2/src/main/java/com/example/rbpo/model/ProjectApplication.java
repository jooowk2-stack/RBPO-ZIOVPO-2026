package com.example.rbpo.model;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "competition_id", nullable = false)
    private GrantCompetition competition;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(nullable = false)
    private Double requestedAmount;

    @Column(nullable = false)
    private String status;
}