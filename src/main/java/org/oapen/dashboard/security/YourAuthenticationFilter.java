package org.oapen.dashboard.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

// TEST TEST TEST
// @Component // De-comment to activate
public class YourAuthenticationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String xAuth = request.getHeader("Authorization"); //here is your token value
		
		System.out.println("AUTH: " + xAuth);
		System.out.println(request);
	    
		/*
	    SecurityContextHolder.getContext().setAuthentication(auth);
	    */

		filterChain.doFilter(request, response);
		
	}

}
