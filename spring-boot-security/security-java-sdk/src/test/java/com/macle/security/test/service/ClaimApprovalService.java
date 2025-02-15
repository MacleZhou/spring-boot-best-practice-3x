package com.macle.security.test.service;

import com.macle.security.sdk.annotation.SecuredPoint;
import com.macle.security.test.model.ClaimRequest;

public class ClaimApprovalService {

    @SecuredPoint(permissionId = "approval", accessControl = true, preDataAuthority = false)
    public ClaimRequest approval(ClaimRequest claimRequest){
        return claimRequest;
    }

}
