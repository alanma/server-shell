package net.ilx.server.core.server.spring.conf;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.ilx.server.core.server.spring.components.AApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

@Configuration("AlfConfiguration")
@Import({SshConfiguration.class})
public class ActorServerConfiguration implements EnvironmentAware {
	
	private static final Logger LOG = LoggerFactory.getLogger(ActorServerConfiguration.class);
	
	public static final String DEFAULT_THREAD_POOL = "defaultThreadPool";
	
	private Environment env;

	@Bean
	public ExecutorService defaultThreadPool() {
		LOG.debug("starting actor thread pool (Executor Service)");
	    ExecutorService actorsThreadPool = Executors.newCachedThreadPool();
	    return actorsThreadPool;
	}

	
	@Bean
	public AApplication application() {
		int shutdownTimeout = env.getProperty("net.ilx.server.core.server.spring.components.AApplication.shutdownTimeout", Integer.class, 60);
		return new AApplication(shutdownTimeout);
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.env = environment;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer encryptablePropertySourcePlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
		configurer.setLocalOverride(true);
		configurer.setIgnoreUnresolvablePlaceholders(false);

		return configurer;
	}

}
