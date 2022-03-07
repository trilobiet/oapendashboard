package org.oapen.dashboard.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiHomeController {
	
	/*
	 * application root (localhost:8080/) will automatically 
	 * be mapped by Spring Boot to static/index.html 
	 * where vue js application resides.  
	 */
	
	
	@GetMapping("/api")
    public String apiHome() {
    	
    	return "API home";
    }
	
}
