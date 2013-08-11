package net.ilx.server.shell.core.utils.alf.server.spring.beans.factory.support;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;
import org.springframework.util.ClassUtils;

public class ExtendedPropertiesBeanDefinitionReader extends PropertiesBeanDefinitionReader {

	public ExtendedPropertiesBeanDefinitionReader(BeanDefinitionRegistry registry) {
		super(registry);
	}

	public int loadBeanDefinitions(Properties properties, String resourceDescription) throws BeanDefinitionStoreException {
		return loadBeanDefinitions(properties, null, resourceDescription);
	}

	private int loadBeanDefinitions(Properties properties, String prefix, String resourceDescription) {
		Map map = Collections.EMPTY_MAP;
		if (null != properties) {
			map = properties;
		}
		return registerBeanDefinitions(map, prefix, resourceDescription);
	}

	@Override
	protected void registerBeanDefinition(String beanName, Map<?, ?> map, String prefix, String resourceDescription) throws BeansException {
		super.registerBeanDefinition(beanName, map, prefix, resourceDescription);
		BeanDefinition beanDefinition = super.getRegistry().getBeanDefinition(beanName);
		if (ClassUtils.isAssignable(AbstractBeanDefinition.class, beanDefinition.getClass())) {
			AbstractBeanDefinition bd = AbstractBeanDefinition.class.cast(beanDefinition);
			bd.setResourceDescription(resourceDescription);
		}
	}

	public int loadBeanDefinitions(PropertySource<?> propertySource) {
		return loadBeanDefinitions(propertySource, "");
	}

	public int loadBeanDefinitions(PropertySources propertySources, String prefix) {
		int counter = 0;
		for (PropertySource<?> propertySource : propertySources) {
			counter += loadBeanDefinitions(propertySource, prefix);
		}
		return counter;
	}

	public int loadBeanDefinitions(PropertySource<?> propertySource, String prefix) {
		Object source = propertySource.getSource();
		Properties props = null;
		String resourceDescription = "";
		if (Properties.class.isAssignableFrom(source.getClass())) {
			props = (Properties) source;
			resourceDescription = propertySource.getName();
		}
		return loadBeanDefinitions(props, prefix, resourceDescription);
	}

}
