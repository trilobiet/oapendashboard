package org.oapen.dashboard.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.oapen.dashboard.api.entities.Funder;
import org.oapen.dashboard.api.entities.Publisher;
import org.oapen.dashboard.api.repository.LookupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author acdhirr
 *
 */

@RestController
@RequestMapping("/api")
public class LookupController {
	
	@Autowired
    private LookupRepository lookupRepository;

    @GetMapping("/lastupdate")
    public LocalDate lastUpdate() {
    	
    	return lookupRepository.lastUpdate();
    }

    @GetMapping("/lastrequestablemonth")
    public YearMonth lastRequestableMonth() {
    	
    	return lookupRepository.lastRequestableMonth();
    }
    
	@GetMapping("/countries")
    public Map<String, String> countries() {
    	
    	return lookupRepository.listCountries();
    }

    @GetMapping("/months")
    public List<YearMonth> availableMonths() {
    	
    	return lookupRepository.availableMonths();
    }

    @GetMapping("/firstmonth")
    public YearMonth firstAvailableMonths() {
    	
    	return lookupRepository.firstAvailableMonth();
    }

    @GetMapping("/lastmonth")
    public YearMonth lastAvailableMonths() {
    	
    	return lookupRepository.lastAvailableMonth();
    }
    
    @GetMapping("/itemtypes")
    public List<String> itemTypes() {
    	
    	return lookupRepository.listItemTypes();
    }
        
    @GetMapping("/funders")
    public List<Funder> funders(
    	@RequestParam(required=false,name="publisher-id") Optional<String> publisherId	
    ) {
    	
    	if (publisherId.isPresent()) 
    		return lookupRepository.funders(publisherId.get());
    	else 
    		return lookupRepository.funders();
    }

    @GetMapping("/publishers")
    public List<Publisher> publishers(
    	@RequestParam(required=false,name="funder-id") Optional<String> funderId
    ) {
    	
    	if (funderId.isPresent())
    		return lookupRepository.publishers(funderId.get());
    	else	
    		return lookupRepository.publishers();
    }
    
}
