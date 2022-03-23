package org.oapen.dashboard.management;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.oapen.dashboard.api.entities.GeoLocation;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @NoArgsConstructor @ToString
@Entity
public class User implements UserDetails, Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private String username, fullname, countryCode, role;
	@Embedded
	private GeoLocation geoLocation;
	
	@Convert(converter = StringSetConverter.class)
	@Column(name = "irus_id",nullable = true)
	private Set<String> irusIds;

    // @JsonProperty(access = Access.WRITE_ONLY) // exclude for display! 
	private String password;
	
	@OneToMany(
		cascade = CascadeType.ALL,
	    orphanRemoval = true, 
	    fetch = FetchType.EAGER
	)
	@JoinColumn(name = "user_id")
	private List<IpRange> ipRanges;
	
	
	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (role.equalsIgnoreCase("admin"))
			return Arrays.asList(new SimpleGrantedAuthority(role));
		else
			return Arrays.asList(new SimpleGrantedAuthority("user"),new SimpleGrantedAuthority(role));
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
