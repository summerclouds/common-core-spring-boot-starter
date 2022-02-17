package org.summerclouds.common.core.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.summerclouds.common.core.log.LogFactory;
import org.summerclouds.common.core.log.SLF4JFactory;
import org.summerclouds.common.core.node.DefaultNodeFactory;
import org.summerclouds.common.core.node.INodeFactory;
import org.summerclouds.common.core.tool.MSpring;
import org.summerclouds.common.core.tracing.ITracing;
import org.summerclouds.common.core.tracing.NoopTracing;

@Configuration
@ConfigurationProperties(prefix = "org.summerclouds.common.core")
public class SpringSummerCloudsCoreAutoConfiguration implements ApplicationContextAware {

	public SpringSummerCloudsCoreAutoConfiguration() {
		System.out.println(">>> Start SpringSummerCloudsAutoConfiguration");
	}
	@Override
    public void setApplicationContext(ApplicationContext appContext) {
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
	ITracing defaultITracing() {
		return new NoopTracing();
	}
	
	@Bean
	@ConditionalOnMissingBean
	LogFactory defaultLogFactory() {
		return new SLF4JFactory();
	}
	
}
