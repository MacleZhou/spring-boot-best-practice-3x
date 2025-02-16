package com.macle.security.sdk.config;

import com.macle.security.sdk.service.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
@Getter
public class SecurityConfig {
    @Value("${secured.application.id}")
    private String applicationId;

    @Value("${secured.application.type}")
    private String applicationType;

    @Value("${secured.resource.cache}")
    private String cacheMethod = "LOCAL";

    @Value("${secured.resource.loader}")
    private String securedResourceLoader;

    @Value("${secured.resource.file}")
    private String securedResourceFile;

    @Value("${secured.user.permission.loader}")
    private String userPermissionLoader;

    @Value("${secured.user.permission.folder}")
    private String userPermissionFolder;

    @Bean
    public ExternalSecuredResourceLoader externalSecuredResourceLoader() throws ClassNotFoundException {
        if(StringUtils.isEmpty(securedResourceLoader)){
            return new EmptySecuredResoruceLoader();
        }
        else {
            Class clazz = Class.forName(securedResourceLoader);
            if(clazz == LocalFileSecuredResourceLoader.class){
                return new LocalFileSecuredResourceLoader();
            }
            else {
                return new RemoteSecuredResourceLoader();
            }
        }
    }

    @Bean
    public UserPermissionLoader userPermissionLoader() throws ClassNotFoundException {
            Class clazz = Class.forName(userPermissionLoader);
            if(clazz == LocalFileUserPermissionLoader.class){
                return new LocalFileUserPermissionLoader();
            }
            else {
                return new RemoteUserPermissionLoader();
            }
    }
}
