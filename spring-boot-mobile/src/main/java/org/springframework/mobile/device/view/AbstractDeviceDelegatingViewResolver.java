package org.springframework.mobile.device.view;

import org.springframework.core.Ordered;
import org.springframework.util.Assert;
import org.springframework.web.context.support.WebApplicationObjectSupport;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import jakarta.servlet.ServletContext;
import java.util.Locale;

public abstract class AbstractDeviceDelegatingViewResolver extends WebApplicationObjectSupport implements ViewResolver, Ordered {
    public static final String REDIRECT_URL_PREFIX = "redirect:";
    public static final String FORWARD_URL_PREFIX = "forward:";
    private final ViewResolver delegate;
    private int order = Integer.MAX_VALUE;
    private boolean enableFallback = false;

    protected AbstractDeviceDelegatingViewResolver(ViewResolver delegate) {
        Assert.notNull(delegate, "delegate is required");
        this.delegate = delegate;
    }

    public ViewResolver getViewResolver() {
        return this.delegate;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return this.order;
    }

    public void setEnableFallback(boolean enableFallback) {
        this.enableFallback = enableFallback;
    }

    protected boolean getEnableFallback() {
        return this.enableFallback;
    }

    public View resolveViewName(String viewName, Locale locale) throws Exception {
        String deviceViewName = this.getDeviceViewName(viewName);
        View view = this.delegate.resolveViewName(deviceViewName, locale);
        if (this.enableFallback && view == null) {
            view = this.delegate.resolveViewName(viewName, locale);
        }

        if (this.logger.isDebugEnabled() && view != null) {
            this.logger.debug("Resolved View: " + view.toString());
        }

        return view;
    }

    protected String getDeviceViewName(String viewName) {
        if (viewName.startsWith("redirect:")) {
            return viewName;
        } else {
            return viewName.startsWith("forward:") ? viewName : this.getDeviceViewNameInternal(viewName);
        }
    }

    protected abstract String getDeviceViewNameInternal(String var1);

    protected void initServletContext(ServletContext servletContext) {
        String name = this.delegate.getClass().getName();
        this.getApplicationContext().getAutowireCapableBeanFactory().initializeBean(this.delegate, name);
    }
}
