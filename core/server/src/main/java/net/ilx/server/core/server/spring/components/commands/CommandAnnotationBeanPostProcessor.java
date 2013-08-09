package net.ilx.server.core.server.spring.components.commands;

import java.util.Collection;
import java.util.Map;

import org.jboss.logging.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.Parser;
import org.springframework.shell.core.SimpleParser;

public class CommandAnnotationBeanPostProcessor implements BeanPostProcessor, Ordered, ApplicationContextAware, DisposableBean,
		ApplicationListener<ContextRefreshedEvent>
{

	private static final Logger LOG = Logger.getLogger(CommandAnnotationBeanPostProcessor.class);
	private ApplicationContext applicationContext;
	private CommandRegistry commandRegistry;
	private SimpleParser simpleCommandParser;


	public CommandAnnotationBeanPostProcessor(final CommandRegistry commandRegistry, final SimpleParser simpleParser) {
		this.commandRegistry = commandRegistry;
		this.simpleCommandParser = simpleParser;
	}

	@Override
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		// add commands to the command registry
		// registerAnnotatedCommands();
		registerMarkedCommands();
	}

	private void registerMarkedCommands() {
		Map<String, CommandMarker> commandBeans = applicationContext.getBeansOfType(CommandMarker.class);

		Collection<CommandMarker> commands = commandBeans.values();
		for (CommandMarker command : commands) {
			simpleCommandParser.add(command);
		}

	}

	@Override
	public void destroy() throws Exception {
		if (this.commandRegistry != null) {
			this.commandRegistry.destroy();
		}
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public int getOrder() {
		return LOWEST_PRECEDENCE;
	}

	@Override
	public Object postProcessBeforeInitialization(	final Object bean,
													final String beanName) throws BeansException
	{
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(	final Object bean,
													final String beanName) throws BeansException
	{
		return bean;
	}

}
