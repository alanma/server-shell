package net.ilx.server.core.server.spring.components.commands;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.shell.converters.AvailableCommandsConverter;
import org.springframework.shell.converters.BigDecimalConverter;
import org.springframework.shell.converters.BigIntegerConverter;
import org.springframework.shell.converters.BooleanConverter;
import org.springframework.shell.converters.CharacterConverter;
import org.springframework.shell.converters.DateConverter;
import org.springframework.shell.converters.DoubleConverter;
import org.springframework.shell.converters.EnumConverter;
import org.springframework.shell.converters.FloatConverter;
import org.springframework.shell.converters.IntegerConverter;
import org.springframework.shell.converters.LocaleConverter;
import org.springframework.shell.converters.LongConverter;
import org.springframework.shell.converters.ShortConverter;
import org.springframework.shell.converters.SimpleFileConverter;
import org.springframework.shell.converters.StaticFieldConverterImpl;
import org.springframework.shell.converters.StringConverter;
import org.springframework.shell.core.Converter;

/**
 * Registry of spring shell converters.
 *
 * @author ilonca
 */
public class ConverterRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

	private ResourceLoader resourceLoader;

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		registerInitialConverters(registry);

		// see http://grepcode.com/file/repo1.maven.org/maven2/org.mybatis/mybatis-spring/1.2.0/org/mybatis/spring/annotation/MapperScannerRegistrar.java#MapperScannerRegistrar
		// for a simple example
		AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(ConvertersScan.class.getName()));

		for (Class<?> clazz: annoAttrs.getClassArray("value")) {
			 registerConverter(registry, clazz);
		}

	}

	private void registerConverter(BeanDefinitionRegistry registry, Class<?> converter) {
		RootBeanDefinition rbd = new RootBeanDefinition();
		rbd.setBeanClass(converter);
		registry.registerBeanDefinition(converter.getSimpleName(), rbd);
	}

	private void registerInitialConverters(BeanDefinitionRegistry registry) {
		Set<Class<? extends Converter<?>>> converters = initialConverters();
		for (Class<? extends Converter<?>> converter : converters) {
			registerConverter(registry, converter);
		}
	}

	Set<Class<? extends Converter<?>>> initialConverters() {
		Set<Class<? extends Converter<?>>> converters = new HashSet<Class<? extends Converter<?>>>();
		converters.add(StringConverter.class);
		converters.add(AvailableCommandsConverter.class);
		converters.add(BigDecimalConverter.class);
		converters.add(BigIntegerConverter.class);
		converters.add(BooleanConverter.class);
		converters.add(CharacterConverter.class);
		converters.add(DateConverter.class);
		converters.add(DoubleConverter.class);
		converters.add(EnumConverter.class);
		converters.add(FloatConverter.class);
		converters.add(IntegerConverter.class);
		converters.add(LocaleConverter.class);
		converters.add(LongConverter.class);
		converters.add(ShortConverter.class);
		converters.add(StaticFieldConverterImpl.class);
		converters.add(SimpleFileConverter.class);
		return converters;
	}

}
