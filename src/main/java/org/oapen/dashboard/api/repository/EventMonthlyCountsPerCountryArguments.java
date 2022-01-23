package org.oapen.dashboard.api.repository;

import java.io.Serializable;
import java.time.YearMonth;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@Builder
public class EventMonthlyCountsPerCountryArguments implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private final YearMonth month;
	private final String publisherIds;
	private final String funderIds;
	private final String itemId;
	private final String itemType;
	
}
