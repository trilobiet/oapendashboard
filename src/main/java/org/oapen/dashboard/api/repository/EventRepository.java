package org.oapen.dashboard.api.repository;

import java.util.List;

import org.oapen.dashboard.api.entities.Event;
import org.oapen.dashboard.api.entities.EventMonthlyCountsPerCountryRow;
import org.oapen.dashboard.api.entities.EventMonthlyCountsPerItemRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class EventRepository {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;	
	

	@Cacheable(value="eventCountPerLocationCache", key="#args")
	public List<Event> eventCountPerLocation(
			EventCountPerLocationArguments args) {
		
		List<Event> lst = jdbcTemplate.query(
			"call event_count_per_location(?,?,?, ?,?,?,?, ?,?,?,?)", 
				new BeanPropertyRowMapper<>(Event.class),
				new Object[] {
					args.getStartMonth().atDay(1),
					args.getEndMonth().atDay(1),
					args.getDecimals(),
					
					args.getPublisherIds(),
					args.getFunderIds(),
					args.getItemId(),
					args.getItemType(),
					
					args.getCountryCode(),
					args.getLatitude(),
					args.getLongitude(),
					args.getRadius()
				}
			);
		
		return lst;
	}
	

	@Cacheable(value="eventCountPerCountryCache", key="#args")
	public List<EventMonthlyCountsPerCountryRow> getEventCountPerCountry(EventMonthlyCountsPerCountryArguments args) {
		
		List<EventMonthlyCountsPerCountryRow> lst = jdbcTemplate.query(
			"call month_totals_per_country(?,?,?, ?,?)", 
			new EventMonthlyCountsPerCountryRowMapper(),
			new Object[] {
				args.getMonth().atDay(1),
				args.getPublisherIds(),
				args.getFunderIds(),
				
				args.getItemId(),
				args.getItemType()
			}
		);
		
		return lst;
	}
	
	
    @Cacheable(value="eventCountPerItemCache", key="#args")
	public List<EventMonthlyCountsPerItemRow> getEventCountPerItem(EventMonthlyCountsPerItemArguments args) {
		
		List<EventMonthlyCountsPerItemRow> lst = jdbcTemplate.query(
			"call month_totals_per_item(?,?,?, ?,?,?, ?,?,?)", 
			new EventMonthlyCountsPerItemRowMapper(),
			new Object[] {
				args.getMonth().atDay(1), 
				args.getPublisherIds(), 
				args.getFunderIds(), 
				
				args.getCountryCode(), 
				args.getItemId(), 
				args.getItemType(),
				
				args.getLatitude(),
				args.getLongitude(),
				args.getRadius()	
			}
		);
		
		return lst;
	}

	
}
