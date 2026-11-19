package com.yash.jobportal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ApplicationRequest(
        @NotNull Long jobId,
        @NotBlank String candidateName,
        @Email @NotBlank String email,
        @Pattern(regexp = "^[0-9+() -]{8,18}$", message = "Enter a valid phone number") @NotBlank String phone,
        String resumeLink,
        String coverLetter
) {}
