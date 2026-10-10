package com.example.grants.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "project_applications")
@Data
public class ProjectApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Связь с грантовым конкурсом (много заявок могут относиться к одному конкурсу)
    @ManyToOne
    @JoinColumn(name = "competition_id", nullable = false)
    private GrantCompetition competition;

    // Связь с пользователем (автор заявки)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @Column(nullable = false)
    private Double requestedAmount; // Запрашиваемая сумма

    @Column(nullable = false)
    private String status; // Статус заявки (например, "SUBMITTED", "APPROVED")
}