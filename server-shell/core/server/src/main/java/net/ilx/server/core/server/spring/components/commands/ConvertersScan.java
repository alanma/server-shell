package net.ilx.server.core.server.spring.components.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;
import org.springframework.shell.core.Converter;

/**
 * Makes sure initial converters are registered.
 * This is a workaround for bugs that prevent usage of BeanDefinitionRegistryPostProcessor to register converters in @Configuration annotated class:
 * - https://jira.springsource.org/browse/SPR-7868 - BeanDefinitionRegistryPostProcessor#postProcessBeanDefinitionRegistry will not be invoked
 * - https://jira.springsource.org/browse/SPR-9464
 *
 * @author ilonca
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(ConverterRegistrar.class)
public @interface ConvertersScan {

	/**
	 * Base packages to scan for Converter classes.
	 * Alias for the basePackages() attribute.
	 *
	 * Allows for more concise annotation declarations e.g.:
	 * @ScanConverters("org.my.pkg").
	 */
	Class<? extends Converter<?>>[] value() default {};

	// TODO ilx: scan package for converters?
}
