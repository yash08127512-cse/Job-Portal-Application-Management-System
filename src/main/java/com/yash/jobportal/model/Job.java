package com.yash.jobportal.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Job(
        Long id,
        String title,
        String company,
        String location,
        String jobType,
        BigDecimal salary,
        String description,
        LocalDate postedDate
) {}
