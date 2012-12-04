package net.ilx.actor.server.alf.spring.conf;

import net.ilx.actor.server.alf.commands.TestCommands;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandConfiguration {

	@Bean
	public TestCommands testCommands() {
		return new TestCommands();
	}
}
