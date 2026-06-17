package org.acme.dto;

import java.math.BigDecimal;

public record UpdateIncidentDto(
        String title,
        String description,
        BigDecimal latitude,
        BigDecimal longitude,
        String address,
        String status
) {}