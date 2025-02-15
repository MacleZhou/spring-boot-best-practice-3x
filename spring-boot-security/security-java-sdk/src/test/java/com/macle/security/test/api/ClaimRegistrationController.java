package com.macle.security.test.api;

import com.macle.security.test.model.ClaimRequest;
import com.macle.security.test.service.ClaimRegistrationService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("/claim-registration")
public class ClaimRegistrationController {

    @Resource
    private ClaimRegistrationService claimRegistrationService;

    /**
     * 此方法需要权限控制，在RBAC中通过手动添加功能点配置如下
     * function=registerDraft
     * functionGroup=registration
     * expression = "execution(public com.macle.security.test.api.ClaimRegistrationController.saveDraft(..))"
     * accessControl = true
     * preDataAuthority = false
     * postDataAuthority = false
     * */
    @PostMapping("/save-draft")
    public ClaimRequest saveDraft(ClaimRequest claimRequest){
        return claimRegistrationService.registerClaim(claimRequest);
    }
}
