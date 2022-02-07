package org.oapen.dashboard.api.repository;

import java.util.Optional;

import org.oapen.dashboard.api.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserRepository {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;	
	
	// Do not cache
	public Optional<User> findById(String id) {
		
		try {
			User user = jdbcTemplate.queryForObject(
				"select * from user where id = ?",
				new UserRowMapper(),
				new Object[] { id }
			);
		
			return Optional.ofNullable(user);
		}
		catch(EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}
}
