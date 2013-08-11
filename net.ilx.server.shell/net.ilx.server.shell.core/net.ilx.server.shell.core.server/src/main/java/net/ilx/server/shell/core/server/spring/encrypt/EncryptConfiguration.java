package net.ilx.server.shell.core.server.spring.encrypt;

import java.util.Properties;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.jasypt.spring31.properties.EncryptablePropertiesPropertySource;
import org.jasypt.spring31.properties.EncryptablePropertySourcesPlaceholderConfigurer;
import org.jasypt.util.text.TextEncryptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

/**
 * Must NOT implement interfaces Ordered NOR PriorityOrdered so that ConfigurationClassPostProcessor gets a chance to run before this registrypostprocessor.
 *
 * @author ilonca
 *
 */
@Configuration
public class EncryptConfiguration implements  BeanDefinitionRegistryPostProcessor, EnvironmentAware
{

	private Environment environment;

	@Bean
	public static StringEncryptor stringEncryptor() {
		StandardPBEStringEncryptor stringEncryptor = new StandardPBEStringEncryptor();
//		stringEncryptor.setPassword("password");
		EnvironmentStringPBEConfig pbeConfig = new EnvironmentStringPBEConfig();
		pbeConfig.setPasswordEnvName("PBE_PASSWORD");
		try {
			pbeConfig.getPassword();
		}
		catch (Throwable t) {
			throw new IllegalStateException("Application PBE_PASSWORD must be configured. Please place PBE_PASSWORD into process environment.", t);
		}

		stringEncryptor.setConfig(pbeConfig);
		return stringEncryptor;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer encryptablePropertySourcePlaceholderConfigurer() {
		StringEncryptor stringEncryptor = stringEncryptor();
		EncryptablePropertySourcesPlaceholderConfigurer configurer = new EncryptablePropertySourcesPlaceholderConfigurer(stringEncryptor);
		configurer.setLocalOverride(true);
		configurer.setIgnoreUnresolvablePlaceholders(true);

		return configurer;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		replaceExistingPropertySources(this.environment, stringEncryptor(), null);
	}

	private void replaceExistingPropertySources(Environment p_environment, StringEncryptor stringEncryptor, TextEncryptor textEncryptor) {
		if (p_environment instanceof ConfigurableEnvironment) {
    		ConfigurableEnvironment env = (ConfigurableEnvironment) p_environment;

    		MutablePropertySources registeredPropertySources = env.getPropertySources();
    		for (PropertySource<?> oldPropertySource : registeredPropertySources) {
				 Object source = oldPropertySource.getSource();
				 PropertySource<?> newSource = oldPropertySource;
				 if (source instanceof Properties) {
					String name = oldPropertySource.getName() + "-encryptable";
					Properties props = (Properties) source;
					if (null != stringEncryptor)  {
						newSource = new EncryptablePropertiesPropertySource(name, props, stringEncryptor);
					}
					else {
						newSource = new EncryptablePropertiesPropertySource(name, props, textEncryptor);
					}
				 }
				 env.getPropertySources().replace(oldPropertySource.getName(), newSource);
			}
    	}
	}

	// getOrder method MUST NOT OVERRIDE ANY OF THE METHODS IN Ordered or PriorityOrdered interfaces
	public int getOrder() {
		// make sure we get chance to configure beans registry AFTER ConfigurationClassPostProcessor (has PriorityOrdered.HIGHEST_PRECEDENCE)
		// but before other BeanDefinitionRegistryPostProcessors
		// you might need to fine tune this
		return PriorityOrdered.HIGHEST_PRECEDENCE + 1;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

}
