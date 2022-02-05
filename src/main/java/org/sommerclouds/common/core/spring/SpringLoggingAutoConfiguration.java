package org.sommerclouds.common.core.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Configuration
@ConfigurationProperties(prefix = "logging.logstash")
public class SpringLoggingAutoConfiguration {

	
	
}
