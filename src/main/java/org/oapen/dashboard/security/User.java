package org.oapen.dashboard.security;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import org.oapen.dashboard.api.entities.GeoLocation;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString
public class User implements UserDetails, Serializable {
	
	private static final long serialVersionUID = 1L;

	private String id, irusId, name, countryCode;
	private String role; // TODO enum
	private GeoLocation geoLocation;

    @JsonProperty(access = Access.WRITE_ONLY) // exclude for display! 
	private String password;
	
	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
	}
	
	@Override
	@JsonIgnore
	public String getUsername() {
		return id;
	}
	
	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return true;
	}

}
