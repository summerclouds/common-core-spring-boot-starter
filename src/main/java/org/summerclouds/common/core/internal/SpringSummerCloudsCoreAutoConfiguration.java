package org.summerclouds.common.core.internal;

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.core.env.Environment;
import org.summerclouds.common.core.lang.SummerApplicationLifecycle;
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

// ApplicationContextAware is not working with graalvm
@Configuration
@ConfigurationProperties(prefix = "org.summerclouds.common.core")
public class SpringSummerCloudsCoreAutoConfiguration /* implements ApplicationContextAware */ {

	private ApplicationContext context;
	private static SpringSummerCloudsCoreAutoConfiguration instance;
	
	public SpringSummerCloudsCoreAutoConfiguration() {
		PlainLog.i("Start");
		instance = this;
	}
	
	static SpringSummerCloudsCoreAutoConfiguration get() {
		return instance;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@PostConstruct
	public void setup() {
		PlainLog.i("SETUP");
		// Search and add log appender to the log system
		try {
		    Map<String, Appender> map = context.getBeansOfType(Appender.class);
		    LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		    Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
		    for (Appender appender : map.values()) {
		    	PlainLog.i("add log appender", appender.getClass());
		    	rootLogger.addAppender(appender);
		    }
		} catch (Throwable t) {
			PlainLog.e(t);
		}
		try {
		    Map<String, SummerApplicationLifecycle> map = context.getBeansOfType(SummerApplicationLifecycle.class);
		    for (Entry<String, SummerApplicationLifecycle> entry : map.entrySet() ) {
		    	try {
		    		entry.getValue().onSummerApplicationStart();
				} catch (Throwable t) {
					PlainLog.e("start application {1} failed", entry.getKey(), t);
				}
		    }
		} catch (Throwable t) {
			PlainLog.e(t);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void destroy() {
		PlainLog.i("DESTROY");
		try {
		    Map<String, SummerApplicationLifecycle> map = context.getBeansOfType(SummerApplicationLifecycle.class);
		    for (Entry<String, SummerApplicationLifecycle> entry : map.entrySet() ) {
		    	try {
		    		entry.getValue().onSummerApplicationStop();
				} catch (Throwable t) {
					PlainLog.e("stop application {1} failed", entry.getKey(), t);
				}
		    }
		} catch (Throwable t) {
			PlainLog.e(t);
		}
		try {
			// Search and remove log appender to the log system
		    Map<String, Appender> map = context.getBeansOfType(Appender.class);
		    LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		    Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
		    for (Appender appender : map.values()) {
		    	PlainLog.i("add log appender", appender.getClass());
		    	rootLogger.detachAppender(appender);
		    }
		} catch (Throwable t) {
			PlainLog.e(t);
		}
	}
	
//	@Override
    public void setApplicationContext(ApplicationContext appContext) {
    	PlainLog.i("SET APP CONTEXT", appContext);
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

	public void onApplicationEvent(ApplicationContextEvent event) {
		PlainLog.i("EVENT", event);

		if (event instanceof ContextStartedEvent || event instanceof ContextRefreshedEvent) {
			boolean needSetup = context == null;
			setApplicationContext(event.getApplicationContext());
			if (needSetup) {
				setup();
				// debug output - all beans
				StringBuilder sb = new StringBuilder().append("\n");
				for (String name : MSpring.getContext().getBeanDefinitionNames()) {
					Object bean = MSpring.getContext().getBean(name);
					sb.append(name).append(": ").append(bean.getClass().getCanonicalName()).append("\n");
				}
				PlainLog.i("beans",sb);
			}
			MSpring.setStatus(MSpring.STATUS.STARTED);
		} else if (event instanceof ContextClosedEvent) {
			destroy();
			MSpring.setStatus(MSpring.STATUS.CLOSED);
			setApplicationContext(null);
		} else if (event instanceof ContextStoppedEvent) {
			MSpring.setStatus(MSpring.STATUS.STOPPED);
			setApplicationContext(null); // to be sure
		}
	}
	
}
