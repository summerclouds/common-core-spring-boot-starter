package org.summerclouds.common.core.internal;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;

public class ContextListener implements ApplicationListener<ApplicationContextEvent> {

	@Override
	public void onApplicationEvent(ApplicationContextEvent event) {
		SpringSummerCloudsCoreAutoConfiguration.get().onApplicationEvent(event);
	}

}
