package com.macle.security.test.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ClaimRequest {
    private String uniqueNumber;
    private String claimant;
    private String sex;
    private LocalDate birthDate;
    private String identityNumber;
    private String identityType;
    private LocalDate identityExpiryDate;
    private String claimReason;
    private String claimType;
    private BigDecimal claimAmount;
    private BigDecimal payableAmount;
    private String status;
    private LocalDateTime processTime;
}
