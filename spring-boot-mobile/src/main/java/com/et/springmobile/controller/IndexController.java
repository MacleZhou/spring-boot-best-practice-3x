package com.et.springmobile.controller;

import java.util.logging.Logger;

import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

	private final static Logger LOGGER = Logger.getLogger(IndexController.class.getName());

	@RequestMapping("/detect")
	@ResponseBody
	public String home(Device device) {
		String msg = null;
		if (device.isMobile()) {
			msg = "mobile";
		} else if (device.isTablet()) {
			msg = "tablet";
		} else {
			msg = "desktop";
		}
		return msg + " device on platform " + device.getDevicePlatform().name();
	}

	@GetMapping("/")
	public String greeting(Device device) {

		String deviceType = "browser";
		String platform = "browser";
		String viewName = "index";

		if (device.isNormal()) {
			deviceType = "browser";
		} else if (device.isMobile()) {
			deviceType = "mobile";
			viewName = "mobile/index";
		} else if (device.isTablet()) {
			deviceType = "tablet";
			viewName = "tablet/index";
		}

		platform = device.getDevicePlatform().name();

		if (platform.equalsIgnoreCase("UNKNOWN")) {
			platform = "browser";
		}

		LOGGER.info("Client Device Type: " + deviceType + ", Platform: " + platform);

		return viewName;
	}
}
