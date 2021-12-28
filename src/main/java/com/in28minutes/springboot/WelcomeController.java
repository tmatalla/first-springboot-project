package com.in28minutes.springboot;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.in28minutes.service.WelcomeService;
import com.in28minutes.service.configuration.BasicConfiguration;

@RestController
public class WelcomeController {
	
	@Autowired
	WelcomeService service;
	
	@Autowired
	private BasicConfiguration configuration;

	@RequestMapping("/welcome")
	public String welcome() {
		return service.retrieveWelcome();
	}
	
	@RequestMapping("/dynamic-configuration")
	public Map dynamicConfiguration() {
		// Not the best practice to use a map to store differnt types!
		Map map = new HashMap();
		map.put("message", configuration.getMessage());
		map.put("number", configuration.getNumber());
		map.put("key", configuration.isValue());
		return map;
	}
}