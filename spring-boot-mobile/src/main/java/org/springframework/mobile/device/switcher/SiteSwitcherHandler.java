package org.springframework.mobile.device.switcher;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface SiteSwitcherHandler {
    boolean handleSiteSwitch(HttpServletRequest var1, HttpServletResponse var2) throws IOException;
}
