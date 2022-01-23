package org.oapen.dashboard.config;

import org.oapen.dashboard.api.repository.EventRepository;
import org.oapen.dashboard.api.repository.LookupRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
	
	@Bean
	public LookupRepository lookupRepository() {
		return new LookupRepository();
	}

	@Bean
	public EventRepository eventRepository() {
		return new EventRepository();
	}
	
	
}
