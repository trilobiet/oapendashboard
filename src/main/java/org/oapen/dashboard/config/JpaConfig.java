package org.oapen.dashboard.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
	basePackages = "org.oapen.dashboard"
)
public class JpaConfig {}
