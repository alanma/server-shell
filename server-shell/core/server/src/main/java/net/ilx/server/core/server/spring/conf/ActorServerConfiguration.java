package net.ilx.server.core.server.spring.conf;


import net.ilx.server.core.server.spring.components.AApplication;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.spring31.properties.EncryptablePropertySourcesPlaceholderConfigurer;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

@Configuration("AlfConfiguration")
@Import({SshConfiguration.class})
public class ActorServerConfiguration implements EnvironmentAware {

	private Environment env;

	@Bean
	public AApplication application() {
		int shutdownTimeout = env.getProperty("net.ilx.server.core.server.spring.components.AApplication.shutdownTimeout", Integer.class);
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
