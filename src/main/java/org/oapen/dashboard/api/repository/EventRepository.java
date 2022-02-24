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
	

	@Cacheable(value="eventCountPerRegionCache", key="#args")
	public List<Event> eventCountPerRegion(
			EventCountPerRegionArguments args) {
		
		List<Event> lst = jdbcTemplate.query(
			"call event_count_per_region(?,?, ?,?,?,?, ?,?,?,?)", 
				new BeanPropertyRowMapper<>(Event.class),
				new Object[] {
						
					args.getStartMonth().atDay(1),
					args.getEndMonth().atDay(1),
					
					args.getLatitude(),
					args.getLongitude(),
					args.getRadius(),
					args.getCountryCode(),
					
					args.getPublisherIds(),
					args.getFunderIds(),
					args.getItemType(),
					args.getItemId()
				}
			);
		
		return lst;
	}
	

	@Cacheable(value="eventCountPerCountryCache", key="#args")
	public List<EventMonthlyCountsPerCountryRow> getEventCountPerCountry(EventMonthlyCountsPerCountryArguments args) {
		
		List<EventMonthlyCountsPerCountryRow> lst = jdbcTemplate.query(
			// NB we are NOT calling event_count_per_country here!	
			"call month_totals_per_country(?,?,?, ?,?)", 
			new EventMonthlyCountsPerCountryRowMapper(),
			new Object[] {
				args.getMonth().atDay(1),
				args.getPublisherIds(),
				args.getFunderIds(),
				
				args.getItemType(),
				args.getItemId()
			}
		);
		
		return lst;
	}
	
	
    @Cacheable(value="eventCountPerItemCache", key="#args")
	public List<EventMonthlyCountsPerItemRow> getEventCountPerItemForPublisherFunder(EventMonthlyCountsPerItemArguments args) {
		
		List<EventMonthlyCountsPerItemRow> lst = jdbcTemplate.query(
			"call month_totals_per_item_for_pubfun(?,?,?, ?,?,?)", 
			new EventMonthlyCountsPerItemRowMapper(),
			new Object[] {
				args.getMonth().atDay(1), 
				args.getPublisherIds(), 
				args.getFunderIds(), 
				
				args.getCountryCode(), 
				args.getItemType(),
				args.getItemId() 
			}
		);
		
		return lst;
	}

    
    @Cacheable(value="eventCountPerItemCache", key="#args")
	public List<EventMonthlyCountsPerItemRow> getEventCountPerItemForLibrary(EventMonthlyCountsPerItemArguments args) {
		
		List<EventMonthlyCountsPerItemRow> lst = jdbcTemplate.query(
			"call month_totals_per_item_for_library(?,?, ?,?, ?,?)", 
			new EventMonthlyCountsPerItemRowMapper(),
			new Object[] {
					
				args.getMonth().atDay(1),
				args.getLibraryId(),
				
				args.getPublisherIds(),
				args.getFunderIds(), 
				
				args.getItemType(),
				args.getItemId() 
			}
		);
		
		return lst;
	}

    
    @Cacheable(value="eventCountPerItemCache", key="#args")
	public List<EventMonthlyCountsPerItemRow> getEventCountPerItemForRegion(EventMonthlyCountsPerItemArguments args) {
		
		List<EventMonthlyCountsPerItemRow> lst = jdbcTemplate.query(
			"call month_totals_per_item_for_region(?,?,?,?, ?,?, ?,?)", 
			new EventMonthlyCountsPerItemRowMapper(),
			new Object[] {
					
				args.getMonth().atDay(1),
				args.getLatitude(),
				args.getLongitude(),
				args.getRadius(),
				
				args.getPublisherIds(),
				args.getFunderIds(), 
				
				args.getItemType(),
				args.getItemId() 
			}
		);
		
		return lst;
	}
    
    
	
}
