package net.ilx.actor.server.spring.components.commands;

import java.util.HashSet;
import java.util.Set;

import net.ilx.actor.server.spring.components.commands.hint.HintCapturingSimpleParser;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
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
import org.springframework.shell.core.SimpleExecutionStrategy;
import org.springframework.shell.core.SimpleParser;

@Configuration
public class CommandSupportConfiguration {

	@Bean
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	public CommandAnnotationBeanPostProcessor commandAnnotationPostProcessor() {
		return new CommandAnnotationBeanPostProcessor(commandRegistry(), simpleCommandParser());
	}

	@Bean
	public CommandRegistry commandRegistry() {
		return new CommandRegistry();
	}

	@Bean
	public SimpleParser simpleCommandParser() {
		HintCapturingSimpleParser simpleParser = new HintCapturingSimpleParser(initialConverters());
		return simpleParser;
	}

	@Bean
	public SimpleExecutionStrategy simpleExecutionStrategy() {
		return new SimpleExecutionStrategy();
	}

	@Bean
	Set<Converter<?>> initialConverters() {
		Set<Converter<?>> converters = new HashSet<Converter<?>>();
		converters.add( new StringConverter());
		converters.add( new AvailableCommandsConverter());
		converters.add( new BigDecimalConverter());
		converters.add( new BigIntegerConverter());
		converters.add( new BooleanConverter());
		converters.add( new CharacterConverter());
		converters.add( new DateConverter());
		converters.add( new DoubleConverter());
		converters.add( new EnumConverter());
		converters.add( new FloatConverter());
		converters.add( new IntegerConverter());
		converters.add( new LocaleConverter());
		converters.add( new LongConverter());
		converters.add( new ShortConverter());
		converters.add( new StaticFieldConverterImpl());
		converters.add( new SimpleFileConverter());

		return converters;
	}
}
