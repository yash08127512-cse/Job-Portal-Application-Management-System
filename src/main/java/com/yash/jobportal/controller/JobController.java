package com.yash.jobportal.controller;

import com.yash.jobportal.dto.JobRequest;
import com.yash.jobportal.model.Job;
import com.yash.jobportal.repository.JobRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin
public class JobController {
    private final JobRepository jobs;

    public JobController(JobRepository jobs) {
        this.jobs = jobs;
    }

    @GetMapping
    public List<Job> getJobs(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "") String location,
            @RequestParam(defaultValue = "") String jobType) {
        return jobs.findAll(keyword.trim(), location.trim(), jobType.trim());
    }

    @GetMapping("/{id}")
    public Job getJob(@PathVariable Long id) {
        return jobs.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Job createJob(@Valid @RequestBody JobRequest request) {
        return jobs.create(request);
    }

    @PutMapping("/{id}")
    public Job updateJob(@PathVariable Long id, @Valid @RequestBody JobRequest request) {
        if (!jobs.update(id, request)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found");
        }
        return getJob(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteJob(@PathVariable Long id) {
        if (!jobs.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found");
        }
    }
}
