package org.example.mvc;

import java.util.HashMap;
import java.util.Map;

import org.example.mvc.controller.Controller;
import org.example.mvc.controller.HomeController;

public class RequestMappingHandlerMapping {
	// [key] /users [value] UserController
	private Map<String, Controller> mappings = new HashMap<>();

	void init() {
		mappings.put("/", new HomeController());
	}

	// uriPath 와 일치하는 Controller return 하는 메서드
	public Controller findHandler(String uriPath) {
		return mappings.get(uriPath);
	}
}
