package org.oapen.dashboard.controller;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import org.oapen.dashboard.api.entities.Event;
import org.oapen.dashboard.api.entities.EventMonthlyCountsPerCountryRow;
import org.oapen.dashboard.api.entities.EventMonthlyCountsPerItemRow;
import org.oapen.dashboard.api.repository.EventCountPerLocationArguments;
import org.oapen.dashboard.api.repository.EventMonthlyCountsPerCountryArguments;
import org.oapen.dashboard.api.repository.EventMonthlyCountsPerItemArguments;
import org.oapen.dashboard.api.repository.EventRepository;
import org.oapen.dashboard.api.repository.EventCountPerLocationArguments.EventCountPerLocationArgumentsBuilder;
import org.oapen.dashboard.api.repository.EventMonthlyCountsPerCountryArguments.EventMonthlyCountsPerCountryArgumentsBuilder;
import org.oapen.dashboard.api.repository.EventMonthlyCountsPerItemArguments.EventMonthlyCountsPerItemArgumentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
	
    @GetMapping("/eventcount-per-country")
    public List<EventMonthlyCountsPerCountryRow>	eventCountPerCountry(
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
    	
    	return eventRepository.getEventCountPerItemForLibrary(args.build());
    }
    
    
    
    @GetMapping("/eventcount-per-item-region")
    public List<EventMonthlyCountsPerItemRow> eventCountPerItemForRegion(
    	@Schema(type = "string", format = "yearmonth", example = "2021-12") // swagger doc annotation
    	@RequestParam(required=true) @DateTimeFormat(pattern = "yyyy-MM") @ValidYearMonth YearMonth month,
    	@RequestParam(required=true) Double latitude,
    	@RequestParam(required=true) Double longitude,
    	@RequestParam(required=true) Integer radius,
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
    
    
    
    /*
     * eventsPerFunder, eventsPerPublisher and eventsPerRegion are  
     * convenience methods to combine calls to EventRepository.eventCountPerLocation()
     * with meaningful combinations of arguments. However, EventRepository.eventCountPerLocation()
     * allows for any combination of arguments (wrapped in EventCountPerLocationArguments).      
     */
    
    
    @GetMapping("/events-per-funder")
    public List<Event> eventsPerFunder(
       	@RequestParam(required=true, name="funder-id") String funderId,
       	@Schema(type = "string", format = "yearmonth", example = "2021-12") // swagger doc annotation
    	@RequestParam(required=true) @DateTimeFormat(pattern = "yyyy-MM") @ValidYearMonth YearMonth startmonth,
    	@Schema(type = "string", format = "yearmonth", example = "2021-12") // swagger doc annotation
    	@RequestParam(required=true) @DateTimeFormat(pattern = "yyyy-MM") @ValidYearMonth YearMonth endmonth,
    	@RequestParam(required=false,name="country-code") Optional<String> countryCode,
    	@RequestParam(required=false,name="item-id") Optional<String> itemId,
    	@RequestParam(required=false,name="item-type") Optional<String> itemType,
    	@RequestParam(required=false, defaultValue="0") Integer precision
    ) {	
    	
    	EventCountPerLocationArgumentsBuilder args = EventCountPerLocationArguments
    		.builder()
    			.startMonth(startmonth)
    			.endMonth(endmonth)
    			.funderIds(funderId)
    			.decimals(precision);
    	
    	if (countryCode.isPresent()) args.countryCode(countryCode.get());
    	if (itemId.isPresent()) args.itemId(itemId.get());
    	if (itemType.isPresent()) args.itemType(itemType.get());

    	return eventRepository.eventCountPerLocation(args.build());
    }
    
    
    @GetMapping("/events-per-publisher")
    public List<Event> eventsPerPublisher(
    	@RequestParam(required=true, name="publisher-id") String publisherId,	
    	@Schema(type = "string", format = "yearmonth", example = "2021-12") // swagger doc annotation
    	@RequestParam(required=true) @DateTimeFormat(pattern = "yyyy-MM") @ValidYearMonth YearMonth startmonth,
    	@Schema(type = "string", format = "yearmonth", example = "2021-12") // swagger doc annotation
    	@RequestParam(required=true) @DateTimeFormat(pattern = "yyyy-MM") @ValidYearMonth YearMonth endmonth,
    	@RequestParam(required=false,name="country-code") Optional<String> countryCode,
    	@RequestParam(required=false,name="item-id") Optional<String> itemId,
    	@RequestParam(required=false,name="item-type") Optional<String> itemType,
    	@RequestParam(required=false, defaultValue="0") Integer precision
    ) {	
    	
    	EventCountPerLocationArgumentsBuilder args = EventCountPerLocationArguments
    		.builder()
    			.startMonth(startmonth)
    			.endMonth(endmonth)
    			.publisherIds(publisherId)
    			.decimals(precision);
    	
    	if (countryCode.isPresent()) args.countryCode(countryCode.get());
    	if (itemId.isPresent()) args.itemId(itemId.get());
    	if (itemType.isPresent()) args.itemType(itemType.get());

       	return eventRepository.eventCountPerLocation(args.build());
    }

    
    @GetMapping("/events-per-region")
    public List<Event> eventsPerRegion(
    	@Schema(type = "string", format = "yearmonth", example = "2021-12") // swagger doc annotation
    	@RequestParam(required=true) @DateTimeFormat(pattern = "yyyy-MM") @ValidYearMonth YearMonth startmonth,
    	@Schema(type = "string", format = "yearmonth", example = "2021-12") // swagger doc annotation
    	@RequestParam(required=true) @DateTimeFormat(pattern = "yyyy-MM") @ValidYearMonth YearMonth endmonth,
    	@RequestParam(required=false,name="item-id") Optional<String> itemId,
    	@RequestParam(required=false,name="item-type") Optional<String> itemType,
    	@RequestParam(required=true, name="latitude") Double latitude,
    	@RequestParam(required=true, name="longitude") Double longitude,
    	@RequestParam(required=true, name="radius") Integer radius,
    	@RequestParam(required=false, defaultValue="0") Integer precision
    ) {	
    	
    	EventCountPerLocationArgumentsBuilder args = EventCountPerLocationArguments
    		.builder()
    			.startMonth(startmonth)
    			.endMonth(endmonth)
    			.latitude(latitude)
    			.longitude(longitude)
    			.radius(radius)
    			.decimals(precision);

    	if (itemId.isPresent()) args.itemId(itemId.get());
    	if (itemType.isPresent()) args.itemType(itemType.get());
    	
       	return eventRepository.eventCountPerLocation(args.build());
    }
    
    
    /* TODO Do not throw away!
    @GetMapping("/testcsv")
    public void test() throws Exception {
    	
    	String str = new String(Files.readAllBytes(Paths.get("/home/acdhirr/downloads-per-title.json")));
    	JFlat flatMe = new JFlat(str);

    	//get the 2D representation of JSON document
    	List<Object[]> json2csv = flatMe.json2Sheet().headerSeparator("-").getJsonAsSheet();

    	//write the 2D representation in csv format
    	flatMe.write2csv("/home/acdhirr/downloads-per-title.csv");
    }*/
    
    
}
