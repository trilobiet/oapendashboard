package org.oapen.dashboard.controller;

import java.util.List;
import java.util.Optional;

import org.oapen.dashboard.management.User;
import org.oapen.dashboard.management.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    //private UserRepository userRepository;
	private UserRepository userRepository;
	
	@PreAuthorize("hasAuthority('admin')")
    @GetMapping("/find-user") 
    public Optional<User>findUser( 
    	@RequestParam(required=true) String username
    ) {
    	
    	return userRepository.findByUsername(username);
    }

	/**
	 * Shows your user credentials (accessible to anyone)
	 * 
	 * @return The signed in user
	 */
    @GetMapping("/user")
    public User user() {
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	// System.out.println(user.getAuthorities());
    	return user;
    }
    
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/users") 
    public List<User> users() {
    	
    	return userRepository.findAll();
    }
    
    
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/save-user")
	public User save(@RequestBody User user) {
    	
    	// Content-type: application/json
    	// Sample request body:
    	// {"id":"6145e100-82b1-11ec-a8a3-0242ac120002","irusId":"","name":"Nord Universitet","countryCode":"NO","role":"library","geoLocation":{"lat":67.288889,"lon":14.560278}}
    	
    	// Password cannot be empty. 
    	// When it is not edited it must stay the same and therefore is not included in the form,
    	// in that case fetch the encrypted password from the database and put it back in the user.
    	String newPw = user.getPassword(); 
    	if (newPw == null || newPw.isBlank()) {
    		
    		String username = user.getUsername();
    		Optional<String> oldPw = userRepository.findByUsername(username).map(u -> u.getPassword());
    		if (oldPw.isPresent()) user.setPassword(oldPw.get());
    	}
    	// But when a new password is sent with the form, then encrypt it and put it in the user.
    	else {
    		BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
    		user.setPassword(enc.encode(newPw));
    	}
		
    	user = userRepository.save(user);
    	return user;
	}
	

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/delete-user")
	public void delete(@RequestBody User user) {
    	
    	userRepository.delete(user);
	}

    
    /**
     * Returns OK if the user still has a session
     * 
     * @return
     */
    @GetMapping("/session")
    public String session() {
    	return "OK";
    }
    
    
}
