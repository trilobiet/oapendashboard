package org.oapen.dashboard.security;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("customPreAuthorizer")
public class CustomPreAuthorizer {
	
	/**
	 * Depending on the role of a logged in user (publisher, funder or library)
	 * checks if the provided arguments are allowed. 
	 * 
	 * Users who are funders or publishers may only see their own data, 
	 * but extra filters within these data on the other type (publishers for 
	 * funders and v.v.) are allowed.
	 * 
	 * Users who are libraries may never request these data directly, but extra filters
	 * on funders or publishers, added to the geographical boundaries of a library 
	 * (ip or lat/lon) are allowed.  
	 * 
	 * @param publisherId Comma separated list of publishers (usually 1 or 2)
	 * @param funderId Comma separated list of funders (usually 1 or 2)
	 * @return boolean indicating whether the request for the provided arguments is allowed.
	 */
    public boolean authorizeFunderOrPublisherForGlobalData(Optional<String> publisherId, Optional<String> funderId) {
    	
    	boolean granted = false;

    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	List<String> userParties = Arrays.asList(user.getIrusId().split(",", -1));
    	
    	if (user.getRole().equals("publisher")) {
    		
    		List<String> publishers = Arrays.asList(publisherId.orElse("").split(",", -1));
    		granted = 
				publishers.size() == userParties.size()
				&&
				publishers.containsAll(userParties) && userParties.containsAll(publishers);
    				
    	}
    	else if (user.getRole().equals("funder")) {
    		
    		List<String> funders = Arrays.asList(funderId.orElse("").split(",", -1));
    		granted = 
    			funders.size() == userParties.size()
				&&
				funders.containsAll(userParties) && userParties.containsAll(funders);
    	}
    	
    	// System.out.println("->->->->->-> THIS IS WHO YOU ARE: " + user);    	
    	
    	return granted;
    }

}
