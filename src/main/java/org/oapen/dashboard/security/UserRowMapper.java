package org.oapen.dashboard.security;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.oapen.dashboard.api.entities.GeoLocation;
import org.springframework.jdbc.core.RowMapper;

public class UserRowMapper implements RowMapper<User> {

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		User user = new User();
		
		user.setId(rs.getString("id"));
		user.setPassword(rs.getString("password"));
		user.setName(rs.getString("name"));
		user.setRole(rs.getString("role"));
		user.setIrusId(rs.getString("irus_id"));
		user.setCountryCode(rs.getString("country_code"));
		
		GeoLocation loc = new GeoLocation();
		loc.setLat(rs.getDouble("lat"));
		loc.setLon(rs.getDouble("lon"));
		
		user.setGeoLocation(loc);
		
		return user;
	}

}
