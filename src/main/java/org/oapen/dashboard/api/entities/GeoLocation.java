package org.oapen.dashboard.api.entities;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString
public class GeoLocation implements Serializable {

	private static final long serialVersionUID = 1L;

	private Double lat;
	private Double lon;
}
