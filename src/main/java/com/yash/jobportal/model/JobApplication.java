package com.yash.jobportal.model;

import java.time.LocalDateTime;

public record JobApplication(
        Long id,
        Long jobId,
        String jobTitle,
        String company,
        String candidateName,
        String email,
        String phone,
        String resumeLink,
        String coverLetter,
        String status,
        LocalDateTime appliedAt
) {}
