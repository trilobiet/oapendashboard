package org.oapen.dashboard.api.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.oapen.dashboard.security.User;
import org.oapen.dashboard.security.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class UserRepository {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;	

	@Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;	

	private static final String SQL_INSERT = 
			"insert into user (id, name, password, role, irus_id, country_code, lat, lon) "
			+ "values (:id, :name, :password, :role, :irusId, :countryCode, :lat, :lon) ";

	private static final String SQL_UPDATE = 
			"update user set "
			+ "name=:name, password=:password, role=:role, irus_id=:irusId, "
			+ "country_code=:countryCode, lat=:lat, lon=:lon "
			+ "where id=:id ";

	private static final String SQL_DELETE = 
			"delete user where id=:id ";
	
	public List<User> findAll() {
		
		List<User> lst = jdbcTemplate.query(
			"select * from user", 
				new UserRowMapper()
			);
		
		return lst;
	}
	
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
	
	
	public void save(User user) {
		
		if (findById(user.getId()).isPresent())
			update(user);
		else
			insert(user);
	}
	
	
	public void delete(User user) {
		
		Map<String,Object> params = new HashMap<>();
		params.put("id", user.getId());
		jdbcTemplate.update(SQL_DELETE, params);
	}

	private void update(User user) {
		
		SqlParameterSource params = paramsMap(user);
		System.out.println("PMAP: " + params);
		namedJdbcTemplate.update(SQL_UPDATE, params);
	}

	
	private void insert(User user) {
		
		namedJdbcTemplate.update(SQL_INSERT, paramsMap(user));
	}
	
	
	private SqlParameterSource paramsMap(User user) {
		
		return new MapSqlParameterSource()
			.addValue("id", user.getId())
			.addValue("password", user.getPassword())
			.addValue("name", user.getName())
			.addValue("role", user.getRole())
			.addValue("irusId", user.getIrusId())
			.addValue("countryCode", user.getCountryCode())
			.addValue("lat", user.getGeoLocation().getLat())
			.addValue("lon", user.getGeoLocation().getLon());
	}
	
}
