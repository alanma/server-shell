package net.ilx.server.core.server.spring.components.commands;

import net.ilx.server.core.server.spring.components.commands.hint.HintCapturingSimpleParser;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.shell.core.SimpleExecutionStrategy;
import org.springframework.shell.core.SimpleParser;

/**
 *
 * @author ilonca
 *
 * BUGS that prevent this class from using BeanDefinitionRegistryPostProcessor to register converters:
 * - https://jira.springsource.org/browse/SPR-7868 - BeanDefinitionRegistryPostProcessor#postProcessBeanDefinitionRegistry will not be invoked
 * - https://jira.springsource.org/browse/SPR-9464
 */
@Configuration
@ConvertersScan
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
		HintCapturingSimpleParser hintCapturingSimpleParser = new HintCapturingSimpleParser();
		return hintCapturingSimpleParser;
	}

	@Bean
	public SimpleExecutionStrategy simpleExecutionStrategy() {
		return new SimpleExecutionStrategy();
	}

	/**
	 * Shell is required by some converters that are wired into HintCapturingSimpleParser.
	 */
	@Bean
	public SshSpringShell shell() {
		return new SshSpringShell();
	}

}
