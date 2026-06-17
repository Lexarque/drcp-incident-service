package org.acme.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateIncidentDto(
        @NotBlank(message = "Title is required")
        String title,
        String description,
        @NotNull(message = "Latitude is required")
        BigDecimal latitude,
        @NotNull(message = "Longitude is required")
        BigDecimal longitude,
        String address
) {}