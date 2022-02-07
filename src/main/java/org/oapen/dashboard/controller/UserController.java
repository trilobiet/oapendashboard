package org.oapen.dashboard.controller;

import java.util.Optional;

import org.oapen.dashboard.api.entities.User;
import org.oapen.dashboard.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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
	
    @GetMapping("/find-user")
    public Optional<User>findUser(
    	@RequestParam(required=true) String id
    ) {
    	
    	return userRepository.findById(id);
    }

}
