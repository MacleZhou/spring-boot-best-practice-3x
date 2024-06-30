package org.springframework.mobile.device.site;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class SitePreferenceHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    public SitePreferenceHandlerMethodArgumentResolver() {
    }

    public boolean supportsParameter(MethodParameter parameter) {
        return SitePreference.class.isAssignableFrom(parameter.getParameterType());
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest request, WebDataBinderFactory binderFactory) throws Exception {
        return SitePreferenceUtils.getCurrentSitePreference(request);
    }
}
