package org.oapen.dashboard.api.repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.oapen.dashboard.api.entities.Funder;
import org.oapen.dashboard.api.entities.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class LookupRepository {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;	

	
	@Cacheable(value="lookupCacheDaily", key="#root.methodName")
	public LocalDate lastUpdate() {
		
		// This is by far the fastest query to check when updates
		// have been harvested.
		return jdbcTemplate.queryForObject(
			"select max(updated_at) from item", 
			LocalDate.class 
		);
	}
	
	/**
	 * Either lastValidMonth, or lastAvailableMonth, if
	 * it is before lastValidMonth.	 
	 * 
	 * @return
	 */
	@Cacheable(value="lookupCacheDaily", key="#root.methodName")
	public YearMonth lastRequestableMonth() {
		
		YearMonth lastValidMonth = lastValidMonth(lastUpdate(),LocalDate.now());
		YearMonth lastAvailableMonth = lastAvailableMonth();
		
		if (lastAvailableMonth.isBefore(lastValidMonth))
			return lastAvailableMonth;
		else
			return lastValidMonth;
		
	}
	
	
	/**
	 * Return the maximum month for which data could be requested.
	 * This can never be later than the month before the last update timestamp
	 * in the database, one day after all data has been read.
	 * 
	 * @param lastUpdate
	 * @param now
	 * @return
	 */
	@Cacheable(value="lookupCacheDaily", key="#root.methodName")
	public YearMonth lastValidMonth(LocalDate lastUpdate, LocalDate now) {
		
		/* 
		  	example: 
		  	  lastUpdate 2022-01-05
		  	
		  	then:
			  for 2022-01-04 use 2021-11
			  for 2022-01-05 use 2021-11
			  for 2022-01-06 use 2021-12
			  for 2022-01-07 use 2021-12
			  for 2022-01-20 use 2021-12
		*/
		
		LocalDate use = now.minusDays(lastUpdate.getDayOfMonth());
		YearMonth ym = YearMonth.from(use).minusMonths(1);
		
		return ym;
	}	
	
	
	@Cacheable(value="lookupCache", key="#root.methodName")
	public YearMonth firstAvailableMonth() {
		
		LocalDate date = jdbcTemplate.queryForObject(
			"select min(date) from event", 
			LocalDate.class 
		);

		return YearMonth.of(date.getYear(),date.getMonthValue());
	}
	
	
	@Cacheable(value="lookupCacheDaily", key="#root.methodName")
	public YearMonth lastAvailableMonth() {
		
		LocalDate date = jdbcTemplate.queryForObject(
			"select max(date) from event", 
			LocalDate.class 
		);

		return YearMonth.of(date.getYear(),date.getMonthValue());
	}
	

	@Cacheable(value="lookupCacheDaily", key="#root.methodName")
	public List<YearMonth> availableMonths() {
		
		List<LocalDate> lst = jdbcTemplate.queryForList(
			"select distinct(date) from event order by date desc", 
			LocalDate.class 
		);
		
		// do not include month beyond lastRequestableMonth (because of update day)
		LocalDate lrm = lastRequestableMonth().atDay(1);
		
		List<YearMonth> months = lst.stream()
			.filter(date -> date.isBefore(lrm.plusMonths(1)))	
			.map(date -> YearMonth.of(date.getYear(),date.getMonthValue()))
			.collect(Collectors.toList());

		return months;
	}

	
	@Cacheable(value="lookupCache", key="#root.methodName")
	public Map<String, String> listCountries() {
		
		Map<String,String> countries = new LinkedHashMap<>();
		
		jdbcTemplate.query(
			"select distinct country_code, country "
			+ "from event where length(country_code)=2 and length(country) > 0 "
			+ "order by country", 
			(rs,i) -> {
				countries.put(rs.getString("country_code"),rs.getString("country"));
				return null;
			}
		);
		
		return countries;
	}

	
	@Cacheable(value="lookupCache", key="#root.methodName")
	public List<String> listItemTypes() {
		
		return jdbcTemplate.queryForList(
			"select distinct type from item order by type",
			String.class
		);
	}

	
	@Cacheable(value="lookupCache", key="#root.methodName")
	public List<Funder> funders() {
		
		return cleanUpFunders(jdbcTemplate.query(
			"select distinct funder_id as id, funder_name as name "
			+ "from events_all_data where funder_id is not null "
			+ "order by funder_name",
			new BeanPropertyRowMapper<>(Funder.class)
		));
	}
	
	
	@Cacheable(value="lookupCache", key="'funders-pub-'+#publisherId")
	public List<Funder> funders(String publisherIds) {

		return cleanUpFunders(jdbcTemplate.query(
			"select distinct funder_id as id, funder_name as name "
			+ "from events_all_data "
			+ "where funder_id is not null "
			+ "and FIND_IN_SET(publisher_id, ?) "
			+ "order by funder_name",
			new BeanPropertyRowMapper<>(Funder.class),
			new Object[] { publisherIds }
		));
	}	
	
		
	@Cacheable(value="lookupCache", key="#root.methodName")
	public Map<String,String> fundersMap() {
		
		Map<String,String> funders = new LinkedHashMap<>();
		
		jdbcTemplate.query(
			"select distinct funder_id, funder_name "
			+ "from events_all_data where funder_id is not null "
			+ "order by funder_name", 
			(rs,i) -> {
				funders.put(rs.getString("funder_id"),rs.getString("funder_name"));
				return null;
			}
		);
		
		return funders;
	}
	
	
	@Cacheable(value="lookupCache", key="#root.methodName")
	public List<Publisher> publishers() {
		
		return jdbcTemplate.query(
			"select distinct publisher_id as id, publisher_name as name "
			+ "from item where publisher_id is not null and publisher_name is not null "
			+ "order by publisher_name", 
			new BeanPropertyRowMapper<>(Publisher.class)
		);
		
	}

	
	@Cacheable(value="lookupCache", key="'publishers-fund-'+#funderIds")
	public List<Publisher> publishers(String funderIds) {
		
		return jdbcTemplate.query(
			"select distinct publisher_id as id, publisher_name as name "
			+ "from events_all_data "
			+ "where funder_id is not null and publisher_id is not null and publisher_name is not null "
			+ "and FIND_IN_SET(funder_id, ?) order by publisher_name", 
			new BeanPropertyRowMapper<>(Publisher.class),
			new Object[] { funderIds }
		);
		
	}
	
	@Cacheable(value="lookupCache", key="#root.methodName")
	public Map<String,String> publishersMap() {
		
		Map<String,String> publishers = new LinkedHashMap<>();
		
		jdbcTemplate.query(
			"select distinct publisher_id, publisher_name "
			+ "from item where publisher_id is not null "
			+ "order by publisher_name", 
			(rs,i) -> {
				publishers.put(rs.getString("publisher_id"),rs.getString("publisher_name"));
				return null;
			}
		);
		
		return publishers;
	}
	
	
	// Some funders have a uuid in both id and name column. Filter these away.
	private List<Funder> cleanUpFunders(List<Funder> funders) {
		
		return funders.stream()  
		.filter(f -> !isUUID(f.getName()))
		.collect(Collectors.toList());
	}
	
	
	private boolean isUUID(String uuid) {
		
		return uuid.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}"); 
	}
	
}
