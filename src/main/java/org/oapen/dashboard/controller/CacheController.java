package org.oapen.dashboard.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/api/cache")
@PreAuthorize("hasAuthority('admin')")
public class CacheController {
	
	@Autowired
	private CacheManager cacheManager;
	
	@GetMapping("/clear") 
	public List<String> clearCache(
		@RequestParam(required=false,name="cache") Optional<String> cacheName
	) {
		
		Collection<String> cacheNames = cacheManager.getCacheNames();
		List<String> cleared = new ArrayList<>();
		
		if (cacheName.isEmpty()) {
			cacheNames.forEach(cname -> {
				cacheManager.getCache(cname).clear();
				cleared.add("cleared:"+cname);
			});
		}
		else if (cacheName.isPresent() && cacheNames.contains(cacheName.get())) { 
			cacheManager.getCache(cacheName.get()).clear();
			cleared.add("cleared:"+cacheName.get());
		}
		else {
			cleared.add("[no matching cache names]");
		}	
		
		return cleared;
	}	

	
	@GetMapping("/list") 
	public Collection<String> listCaches() {
		
		Collection<String> cacheNames = cacheManager.getCacheNames();
		
		return cacheNames;
	}	
	
	
}
