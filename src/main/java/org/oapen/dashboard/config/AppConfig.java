package org.oapen.dashboard.config;

import org.oapen.dashboard.api.repository.EventRepository;
import org.oapen.dashboard.api.repository.ItemRepository;
import org.oapen.dashboard.api.repository.LookupRepository;
import org.oapen.dashboard.api.repository.UserRepository;
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
	
	@Bean
	public ItemRepository itemRepository() {
		return new ItemRepository();
	}
	
	@Bean
	public UserRepository userRepository() {
		return new UserRepository();
	}
}
