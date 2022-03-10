package org.oapen.dashboard.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

// TEST TEST TEST
@Component
public class YourAuthenticationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String xAuth = request.getHeader("Authorization"); //here is your token value
		
		System.out.println("AUTH: " + xAuth);
		System.out.println(request);
	    
		/*
		//Place here your redis checks, get Authentication and so on
	    SecurityContextHolder.getContext().setAuthentication(auth);
	    */
		
		//SecurityContextHolder.getContext().setAuthentication(null);

		filterChain.doFilter(request, response);
		
	}

}
