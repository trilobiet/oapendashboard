package org.oapen.dashboard.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyErrorController implements ErrorController {
	
	@GetMapping("/error")
    public String someError() {
    	
    	return "invalid path";
    }

}
