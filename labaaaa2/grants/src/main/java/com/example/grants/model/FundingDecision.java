package com.example.grants.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "funding_decisions")
@Data
public class FundingDecision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ссылка на проектную заявку
    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private ProjectApplication application;

    @Column(nullable = false)
    private Double approvedAmount; // Утверждённая сумма

    @Column(nullable = false)
    private String status; // Статус решения
}