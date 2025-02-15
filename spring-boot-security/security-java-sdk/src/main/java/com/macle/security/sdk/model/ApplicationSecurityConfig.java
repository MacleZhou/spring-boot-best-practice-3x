package com.macle.security.sdk.model;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class ApplicationSecurityConfig {
    private String applicationId;
    private String applicationType;
    private String cacheMethod = "LOCAL";
}
