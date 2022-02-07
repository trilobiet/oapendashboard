package org.oapen.dashboard.api.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.oapen.dashboard.api.entities.GeoLocation;
import org.oapen.dashboard.api.entities.User;
import org.springframework.jdbc.core.RowMapper;

public class UserRowMapper implements RowMapper<User> {

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		User user = new User();
		
		user.setId(rs.getString("id"));
		user.setIrusId(rs.getString("irusId"));
		user.setRole(rs.getString("role"));
		user.setName(rs.getString("name"));
		
		GeoLocation loc = new GeoLocation();
		loc.setLat(rs.getDouble("lat"));
		loc.setLon(rs.getDouble("lon"));
		
		user.setGeoLocation(loc);
		
		return user;
	}

}
