package com.macle.security.sdk.service;

/**
 * 此接口用于加载所有远程定义的需要安全控制的资源, 对于本地通过@SecuredPoint描述的需要安全控制的资源不需要从远程加载。
 * */

import com.macle.security.sdk.model.SecuredResource;

import java.lang.reflect.Method;
import java.util.List;

public interface ExternalSecuredResourceLoader {

    List<SecuredResource> loadSecuredResources() throws Exception;

    //SecuredResource getSecuredResource(String resourceId) throws Exception;

    //SecuredResource getSecuredResource(Method method) throws Exception;
}
