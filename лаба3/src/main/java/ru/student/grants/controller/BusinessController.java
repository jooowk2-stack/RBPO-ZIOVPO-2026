package ru.student.grants.controller;

import ru.student.grants.model.*;
import ru.student.grants.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/business")
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @PostMapping("/submit-application")
    public ResponseEntity<ProjectApplication> submitApplication(
            @RequestParam Long competitionId,
            @RequestParam Long authorId,
            @RequestParam Double requestedAmount) {
        return ResponseEntity.ok(businessService.submitApplication(competitionId, authorId, requestedAmount));
    }

    @PostMapping("/add-review")
    public ResponseEntity<ExpertReview> addReview(
            @RequestParam Long applicationId,
            @RequestParam Long expertId,
            @RequestParam Integer score,
            @RequestParam String comment) {
        return ResponseEntity.ok(businessService.addExpertReview(applicationId, expertId, score, comment));
    }

    @PostMapping("/funding-decision")
    public ResponseEntity<FundingDecision> makeDecision(
            @RequestParam Long applicationId,
            @RequestParam Double approvedAmount,
            @RequestParam String status) {
        return ResponseEntity.ok(businessService.makeFundingDecision(applicationId, approvedAmount, status));
    }

    @PatchMapping("/close-competition/{id}")
    public ResponseEntity<GrantCompetition> closeCompetition(@PathVariable Long id) {
        return ResponseEntity.ok(businessService.closeCompetition(id));
    }

    @PostMapping("/audit-budget/{competitionId}")
    public ResponseEntity<String> auditBudget(@PathVariable Long competitionId) {
        return ResponseEntity.ok(businessService.auditAndRejectExcessiveApplications(competitionId));
    }
}