package org.oapen.dashboard.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * Config for swagger generated API documentation
 * @author acdhirr
 *
 */
// run on same (https) server
@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server URL")})
@Configuration
public class OpenAPIConfig {

}
