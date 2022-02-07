package org.oapen.dashboard.api.entities;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String id, irusId, name;
	private String role; // TODO enum
	private GeoLocation geoLocation;

}
