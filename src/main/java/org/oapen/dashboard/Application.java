package org.oapen.dashboard;

import org.springframework.boot.SpringApplication;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		
		// System.getProperties().list(System.out);
		BCryptPasswordEncoder p = new BCryptPasswordEncoder();
		System.out.println("->->-> " + p.encode("i145s8"));
		
		ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
		
		DispatcherServlet dispatcherServlet = (DispatcherServlet)ctx.getBean("dispatcherServlet");
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true); 
	}

}
