package org.oapen.dashboard.controller;

import java.util.Collections;
import java.util.List;

import org.oapen.dashboard.api.entities.Item;
import org.oapen.dashboard.api.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
@Validated
@RequestMapping("/api")
public class ItemController {
	
	@Autowired
    private ItemRepository itemRepository;
	
    @GetMapping("/find-item")
    public List<Item>findItem(
    	@RequestParam(required=true) String title
    ) {
    	
    	if (title.trim().length() > 2) {
    		return itemRepository.findByTitle(title);
    	}
    	else return Collections.emptyList();
    }

    
    @GetMapping("/find-publisher-item")
    public List<Item>findPublisherItem(
    	@RequestParam(required=true) String title,
    	@RequestParam(required=true) String publishers
    ) {
    	
    	if (title.trim().length() > 2) {
    		return itemRepository.findByTitleForPublisher(title, publishers);
    	}
    	else return Collections.emptyList();
    }
    
    
    @GetMapping("/find-funder-item")
    public List<Item>findFunderItem(
    	@RequestParam(required=true) String title,
    	@RequestParam(required=true) String funders
    ) {
    	
    	if (title.trim().length() > 2) {
    		return itemRepository.findByTitleForFunder(title, funders);
    	}
    	else return Collections.emptyList();
    }

}
