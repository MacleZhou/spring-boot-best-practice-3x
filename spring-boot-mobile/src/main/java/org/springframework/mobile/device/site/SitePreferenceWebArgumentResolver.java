package org.springframework.mobile.device.site;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

public class SitePreferenceWebArgumentResolver implements WebArgumentResolver {
    public SitePreferenceWebArgumentResolver() {
    }

    public Object resolveArgument(MethodParameter param, NativeWebRequest request) throws Exception {
        return SitePreference.class.isAssignableFrom(param.getParameterType()) ? SitePreferenceUtils.getCurrentSitePreference(request) : WebArgumentResolver.UNRESOLVED;
    }
}
