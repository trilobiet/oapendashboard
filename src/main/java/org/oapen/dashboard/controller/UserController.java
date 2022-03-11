package org.oapen.dashboard.controller;

import java.util.Optional;

import org.oapen.dashboard.api.repository.UserRepository;
import org.oapen.dashboard.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	
    @GetMapping("/find-user") 
    public Optional<User>findUser( // TODO remove
    	@RequestParam(required=true) String id
    ) {
    	
    	return userRepository.findById(id);
    }

    @GetMapping("/user")
    public User user() {
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	return user;
    }
    
}
