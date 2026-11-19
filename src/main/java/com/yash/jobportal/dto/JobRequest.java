package com.yash.jobportal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record JobRequest(
        @NotBlank String title,
        @NotBlank String company,
        @NotBlank String location,
        @NotBlank String jobType,
        @NotNull BigDecimal salary,
        @NotBlank String description
) {}
