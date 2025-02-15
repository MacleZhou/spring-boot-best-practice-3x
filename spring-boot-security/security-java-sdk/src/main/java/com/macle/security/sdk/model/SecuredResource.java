package com.macle.security.sdk.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
public class SecuredResource {
    private String id;
    private String description;
    private ResourceType type;
    private boolean accessControl;
    private boolean preDataAuthority;
    private boolean postDataAuthority;
    private String permissionId;
}
