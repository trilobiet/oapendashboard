package org.oapen.dashboard.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity( // need this to use @PreAuthorize on methods
  prePostEnabled = true, 
  securedEnabled = true, 
  jsr250Enabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService uds;
	
	/*@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**");
	}*/	
	

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth
			.userDetailsService(uds)
			.passwordEncoder(NoOpPasswordEncoder.getInstance()); // TODO
		
		System.out.println("UDS " + uds);
		
	}
	

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
			.authorizeRequests()
				.anyRequest().authenticated()
				//.antMatchers("/api/**").authenticated()
				.and()
			.formLogin()
				.loginPage("/login")
				//.usernameParameter("email")
				.permitAll()
				.and()
			.logout()
			;
		
		http.csrf().disable();
	}
	
		
	
	
}
