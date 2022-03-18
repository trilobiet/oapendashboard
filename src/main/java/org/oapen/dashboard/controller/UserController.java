package org.oapen.dashboard.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.oapen.dashboard.api.repository.UserRepository;
import org.oapen.dashboard.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author acdhirr
 *
 */

@RestController
@Validated
@RequestMapping("/api")
public class UserController {
	
	@Autowired
    private UserRepository userRepository;
	
	@PreAuthorize("hasAuthority('admin')")
    @GetMapping("/find-user") 
    public Optional<User>findUser( 
    	@RequestParam(required=true) String id
    ) {
    	
    	return userRepository.findById(id);
    }

    @GetMapping("/user")
    public User user() {
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	System.out.println(user.getAuthorities());

    	return user;
    }
    
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/users") 
    public List<User> users() {
    	
    	return userRepository.findAll();
    }
    
    
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/save-user")
	public void save(@RequestBody User user) {
    	
    	// Content-type: application/json
    	// Sample request body:
    	// {"id":"6145e100-82b1-11ec-a8a3-0242ac120002","irusId":"","name":"Nord Universitet","countryCode":"NO","role":"library","geoLocation":{"lat":67.288889,"lon":14.560278}}
		
    		userRepository.save(user);
    	System.out.println("SAVED USER: " + user);
	}
	

    
    
    
}
