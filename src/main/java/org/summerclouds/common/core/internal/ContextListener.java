package org.summerclouds.common.core.internal;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.summerclouds.common.core.tool.MSpring;

public class ContextListener implements ApplicationListener<ApplicationContextEvent> {

	@Override
	public void onApplicationEvent(ApplicationContextEvent event) {
		if (event instanceof ContextStartedEvent) {
			MSpring.setStatus(MSpring.STATUS.STARTED);
		} else if (event instanceof ContextRefreshedEvent) {
			MSpring.setStatus(MSpring.STATUS.STARTED);
		} else if (event instanceof ContextClosedEvent) {
			MSpring.setStatus(MSpring.STATUS.CLOSED);
		} else if (event instanceof ContextStoppedEvent) {
			MSpring.setStatus(MSpring.STATUS.STOPPED);
		}
	}

}
