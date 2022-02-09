package org.oapen.dashboard.api.entities;

import java.io.Serializable;
import java.time.YearMonth;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class EventMonthlyCountsPerCountryRow implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String country;
	private String countryCode;
	private YearMonth yearMonth;
	private Map<YearMonth,Integer> monthTotals;
	private Integer total;
}
