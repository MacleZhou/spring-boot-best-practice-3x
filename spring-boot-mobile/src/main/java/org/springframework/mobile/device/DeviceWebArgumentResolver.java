package org.springframework.mobile.device;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

public class DeviceWebArgumentResolver implements WebArgumentResolver {
    public DeviceWebArgumentResolver() {
    }

    public Object resolveArgument(MethodParameter param, NativeWebRequest request) throws Exception {
        return Device.class.isAssignableFrom(param.getParameterType()) ? DeviceUtils.getCurrentDevice(request) : WebArgumentResolver.UNRESOLVED;
    }
}