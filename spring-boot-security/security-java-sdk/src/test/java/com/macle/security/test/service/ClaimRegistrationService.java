package com.macle.security.test.service;

import com.macle.security.test.model.ClaimRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service("claimService")
public class ClaimRegistrationService {
    public ClaimRequest registerClaim(ClaimRequest claimRequest) {
        claimRequest.setStatus("REGISTERED");
        claimRequest.setProcessTime(LocalDateTime.now());
        return claimRequest;
    }
}
