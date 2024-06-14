package org.oapen.dashboard.controller;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.oapen.dashboard.api.entities.Event;
import org.oapen.dashboard.api.entities.EventMonthlyCountsPerCountryRow;
import org.oapen.dashboard.api.entities.EventMonthlyCountsPerItemRow;
import org.oapen.dashboard.api.repository.CacheEventLogger;
import org.oapen.dashboard.api.repository.EventCountPerRegionArguments;
import org.oapen.dashboard.api.repository.EventCountPerRegionArguments.EventCountPerRegionArgumentsBuilder;
import org.oapen.dashboard.api.repository.EventMonthlyCountsPerCountryArguments;
import org.oapen.dashboard.api.repository.EventMonthlyCountsPerCountryArguments.EventMonthlyCountsPerCountryArgumentsBuilder;
import org.oapen.dashboard.api.repository.EventMonthlyCountsPerItemArguments;
import org.oapen.dashboard.api.repository.EventMonthlyCountsPerItemArguments.EventMonthlyCountsPerItemArgumentsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.oapen.dashboard.api.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 
 * @author acdhirr
 *
 */

@RestController
@Validated
@RequestMapping("/api")
public class EventController {
	
	@Autowired
    private EventRepository eventRepository;
	
	private static final Logger logger = 
			LoggerFactory.getLogger(EventController.class);

	
	// Access checking
	@PreAuthorize("@customPreAuthorizer.authorizeFunderOrPublisherForGlobalData(#publisherId,#funderId)")
    @GetMapping("/eventcount-per-country")
    public List<EventMonthlyCountsPerCountryRow> eventCountPerCountry(
    	@Schema(type = "string", format = "yearmonth", example = "2021-12") // swagger doc annotation	
    	@RequestParam(required=true) @DateTimeFormat(pattern = "yyyy-MM") @ValidYearMonth YearMonth month,
    	@RequestParam(required=false,name="publisher-id") Optional<String> publisherId,
    	@RequestParam(required=false,name="funder-id") Optional<String> funderId,
    	@RequestParam(required=false,name="item-id") Optional<String> itemId,
    	@RequestParam(required=false,name="item-type") Optional<String> itemType
    ) {
    	
    	EventMonthlyCountsPerCountryArgumentsBuilder args = 
    			EventMonthlyCountsPerCountryArguments.builder().month(month);
    	
    	if (publisherId.isPresent()) args.publisherIds(publisherId.get());
    	if (funderId.isPresent()) args.funderIds(funderId.get());
    	if (itemId.isPresent()) args.itemId(itemId.get());
    	if (itemType.isPresent()) args.itemType(itemType.get());
    	
    	return eventRepository.getEventCountPerCountry(args.build());
    }
    
    
	// Access checking
	@PreAuthorize("@customPreAuthorizer.authorizeFunderOrPublisherForGlobalData(#publisherId,#funderId)")
    @GetMapping("/eventcount-per-item-publisherfunder")
    public List<EventMonthlyCountsPerItemRow> eventCountPerItem(
    	@Schema(type = "string", format = "yearmonth", example = "2021-12") // swagger doc annotation
    	@RequestParam(required=true) @DateTimeFormat(pattern = "yyyy-MM") @ValidYearMonth YearMonth month,
    	@RequestParam(required=false,name="publisher-id") Optional<String> publisherId,
    	@RequestParam(required=false,name="funder-id") Optional<String> funderId,
    	@RequestParam(required=false,name="country-code") Optional<String> countryCode,
    	@RequestParam(required=false,name="item-id") Optional<String> itemId,
    	@RequestParam(required=false,name="item-type") Optional<String> itemType
    ) {
    	
    	EventMonthlyCountsPerItemArgumentsBuilder args = 
    			EventMonthlyCountsPerItemArguments.builder().month(month);
    	
    	if (publisherId.isPresent()) args.publisherIds(publisherId.get());
    	if (funderId.isPresent()) args.funderIds(funderId.get());
    	if (countryCode.isPresent()) args.countryCode(countryCode.get());
    	if (itemId.isPresent()) args.itemId(itemId.get());
    	if (itemType.isPresent()) args.itemType(itemType.get());
    	
    	return eventRepository.getEventCountPerItemForPublisherFunder(args.build());
    }
    
    
    @GetMapping("/eventcount-per-region")
    public List<Event> eventCountPerRegion(
    	@Schema(type = "string", format = "yearmonth", example = "2021-12") // swagger doc annotation
    	@RequestParam(required=true) @DateTimeFormat(pattern = "yyyy-MM") @ValidYearMonth YearMonth startmonth,
    	@Schema(type = "string", format = "yearmonth", example = "2021-12") // swagger doc annotation
    	@RequestParam(required=true) @DateTimeFormat(pattern = "yyyy-MM") @ValidYearMonth YearMonth endmonth,
    	@RequestParam(required=true) Double latitude,
    	@RequestParam(required=true) Double longitude,
    	@RequestParam(required=true) @Min(10) @Max(500) Integer radius,
    	@RequestParam(required=false,name="country-code") Optional<String> countryCode,
    	@RequestParam(required=false,name="publisher-id") Optional<String> publisherId,
    	@RequestParam(required=false,name="funder-id") Optional<String> funderId,
    	@RequestParam(required=false,name="item-id") Optional<String> itemId,
    	@RequestParam(required=false,name="item-type") Optional<String> itemType
    ) {	
    	
    	EventCountPerRegionArgumentsBuilder args = EventCountPerRegionArguments
    		.builder()
    			.startMonth(startmonth)
    			.endMonth(endmonth)
    			.latitude(latitude)
    			.longitude(longitude)
    			.radius(radius);

    	if (countryCode.isPresent()) args.countryCode(countryCode.get());
    	if (publisherId.isPresent()) args.publisherIds(publisherId.get());
    	if (funderId.isPresent()) args.funderIds(funderId.get());
    	if (itemId.isPresent()) args.itemId(itemId.get());
    	if (itemType.isPresent()) args.itemType(itemType.get());
    	
       	return eventRepository.eventCountPerRegion(args.build());
    }        
    
    
    @GetMapping("/eventcount-per-item-library")
    public List<EventMonthlyCountsPerItemRow> eventCountPerItemForLibrary(
    	@Schema(type = "string", format = "yearmonth", example = "2021-12") // swagger doc annotation
    	@RequestParam(required=true) @DateTimeFormat(pattern = "yyyy-MM") @ValidYearMonth YearMonth month,
    	@RequestParam(required=true,name="library-id") String libraryId,
    	@RequestParam(required=false,name="publisher-id") Optional<String> publisherId,
    	@RequestParam(required=false,name="funder-id") Optional<String> funderId,
    	@RequestParam(required=false,name="item-id") Optional<String> itemId,
    	@RequestParam(required=false,name="item-type") Optional<String> itemType
    ) {
    	
    	EventMonthlyCountsPerItemArgumentsBuilder args = 
    		EventMonthlyCountsPerItemArguments.builder()
    			.month(month).libraryId(libraryId);
    	
    	if (publisherId.isPresent()) args.publisherIds(publisherId.get());
    	if (funderId.isPresent()) args.funderIds(funderId.get());
    	if (itemId.isPresent()) args.itemId(itemId.get());
    	if (itemType.isPresent()) args.itemType(itemType.get());
    	
    	System.out.println("STARTING LIBRARY");
    	
    	List<EventMonthlyCountsPerItemRow> q = eventRepository.getEventCountPerItemForLibrary(args.build());
    	
    	logger.info("Event count per library (ip address matching) found " + q.size() + " for library " + libraryId);
    	
    	return q;
    }
    
    
    
    @GetMapping("/eventcount-per-item-region")
    public List<EventMonthlyCountsPerItemRow> eventCountPerItemForRegion(
    	@Schema(type = "string", format = "yearmonth", example = "2021-12") // swagger doc annotation
    	@RequestParam(required=true) @DateTimeFormat(pattern = "yyyy-MM") @ValidYearMonth YearMonth month,
    	@RequestParam(required=true) Double latitude,
    	@RequestParam(required=true) Double longitude,
    	@RequestParam(required=true) @Min(10) @Max(500) Integer radius,
    	@RequestParam(required=false,name="publisher-id") Optional<String> publisherId,
    	@RequestParam(required=false,name="funder-id") Optional<String> funderId,
    	@RequestParam(required=false,name="item-id") Optional<String> itemId,
    	@RequestParam(required=false,name="item-type") Optional<String> itemType
    ) {
    	
    	EventMonthlyCountsPerItemArgumentsBuilder args = 
    		EventMonthlyCountsPerItemArguments.builder()
    			.month(month).latitude(latitude).longitude(longitude).radius(radius);
    	
    	if (publisherId.isPresent()) args.publisherIds(publisherId.get());
    	if (funderId.isPresent()) args.funderIds(funderId.get());
    	if (itemId.isPresent()) args.itemId(itemId.get());
    	if (itemType.isPresent()) args.itemType(itemType.get());
    	
    	return eventRepository.getEventCountPerItemForRegion(args.build());
    }
    
    
}
