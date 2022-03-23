package org.oapen.dashboard.security;

import java.util.Optional;

import org.oapen.dashboard.management.User;
import org.oapen.dashboard.management.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
	
	// private UserRepository userRepo;
	@Autowired
	private UserRepository userRepository;
	
	/*
	@Autowired
	public MyUserDetailsService(UserJpaRepo userRepo) {
		this.userRepo = userRepo;
	}*/


	@Override
	public UserDetails loadUserByUsername(String username) {
		
		Optional<User> user = userRepository.findByUsername(username);
		System.out.println("USER: " + user);
		return user.orElseThrow(() ->  new UsernameNotFoundException("User '" + username + "' not found"));
	}

}
