package com.example.rbpo.model;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "application_id", nullable = false)
    private ProjectApplication application;

    @Column(nullable = false)
    private Double approvedAmount;

    @Column(nullable = false)
    private String status;
}