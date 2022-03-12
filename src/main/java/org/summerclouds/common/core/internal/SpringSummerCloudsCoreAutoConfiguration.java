package org.summerclouds.common.core.internal;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.summerclouds.common.core.log.LogFactory;
import org.summerclouds.common.core.log.PlainLog;
import org.summerclouds.common.core.log.SLF4JFactory;
import org.summerclouds.common.core.log.ThreadConsoleLogAppender;
import org.summerclouds.common.core.node.DefaultNodeFactory;
import org.summerclouds.common.core.node.INodeFactory;
import org.summerclouds.common.core.operation.OperationManager;
import org.summerclouds.common.core.tool.MSpring;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;

@Configuration
@ConfigurationProperties(prefix = "org.summerclouds.common.core")
public class SpringSummerCloudsCoreAutoConfiguration implements ApplicationContextAware {

	private ApplicationContext context;

	public SpringSummerCloudsCoreAutoConfiguration() {
		PlainLog.i("Start SpringSummerCloudsAutoConfiguration");
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostConstruct
	public void setup() {
		
		// Search and add log appender to the log system
	    Map<String, Appender> map = context.getBeansOfType(Appender.class);
	    LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
	    Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
	    for (Appender appender : map.values()) {
	    	PlainLog.i("add log appender", appender.getClass());
	    	rootLogger.addAppender(appender);
	    }
	}

	@Override
    public void setApplicationContext(ApplicationContext appContext) {
		this.context = appContext;
        MSpring.setContext(appContext);
    }
	
	@Autowired
	public void setEnvironment(Environment env) {
		MSpring.setEnvironment(env);
	}

	@Bean
	@ConditionalOnMissingBean
	INodeFactory defaultINodeFactory() {
		return new DefaultNodeFactory();
	}
	
	@Bean
	@ConditionalOnMissingBean
	LogFactory defaultLogFactory() {
		return new SLF4JFactory();
	}
	
	@Bean
	ContextListener springSummerCloudsContextListener() {
		return new ContextListener();
	}
	
	@Bean
	@ConditionalOnProperty(name="org.summerclouds.operations.enabled",havingValue="true")
	OperationManager operationManager() {
		return new OperationManager();
	}
	
	@Bean
	@ConditionalOnProperty(name="org.summerclouds.ThreadConsoleLogAppender.enabled",havingValue="true")
	Appender<ILoggingEvent> threadConsoleLogAppender() {
		return new ThreadConsoleLogAppender();
	}
	
}
