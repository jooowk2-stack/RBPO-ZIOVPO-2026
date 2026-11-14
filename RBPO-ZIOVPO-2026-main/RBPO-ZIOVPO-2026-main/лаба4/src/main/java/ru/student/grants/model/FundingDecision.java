package ru.student.grants.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "funding_decisions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundingDecision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "application_id")
    private ProjectApplication application;

    private Double approvedAmount;
    private String status; // APPROVED, REJECTED
}