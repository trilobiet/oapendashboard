package org.oapen.dashboard.api.repository;

import java.io.Serializable;
import java.time.YearMonth;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@Builder
public class EventCountPerRegionArguments implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private final YearMonth startMonth;
	private final YearMonth endMonth;
	
	private final Double latitude;
	private final Double longitude;
	private final Integer radius;
	private final String countryCode;

	private final String publisherIds;
	private final String funderIds;
	private final String itemType;	
	private final String itemId;

}
