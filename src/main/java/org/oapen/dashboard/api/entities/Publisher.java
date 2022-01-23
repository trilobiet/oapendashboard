package org.oapen.dashboard.api.entities;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class Publisher implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
}
