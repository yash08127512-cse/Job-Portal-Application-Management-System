package com.yash.jobportal.controller;

import com.yash.jobportal.dto.ApplicationRequest;
import com.yash.jobportal.model.JobApplication;
import com.yash.jobportal.repository.ApplicationRepository;
import com.yash.jobportal.repository.JobRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin
public class ApplicationController {
    private static final Set<String> ALLOWED_STATUSES =
            Set.of("SUBMITTED", "UNDER_REVIEW", "SHORTLISTED", "REJECTED", "SELECTED");

    private final ApplicationRepository applications;
    private final JobRepository jobs;

    public ApplicationController(ApplicationRepository applications, JobRepository jobs) {
        this.applications = applications;
        this.jobs = jobs;
    }

    @GetMapping
    public List<JobApplication> getApplications(@RequestParam(required = false) String email) {
        return applications.findAll(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JobApplication apply(@Valid @RequestBody ApplicationRequest request) {
        if (jobs.findById(request.jobId()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Selected job does not exist");
        }
        return applications.create(request);
    }

    @PatchMapping("/{id}/status")
    public JobApplication updateStatus(@PathVariable Long id, @RequestParam String status) {
        String normalized = status.trim().toUpperCase();
        if (!ALLOWED_STATUSES.contains(normalized)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid application status");
        }
        if (!applications.updateStatus(id, normalized)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found");
        }
        return applications.findById(id).orElseThrow();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteApplication(@PathVariable Long id) {
        if (!applications.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found");
        }
    }
}
