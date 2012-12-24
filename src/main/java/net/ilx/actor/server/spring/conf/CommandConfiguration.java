package net.ilx.actor.server.spring.conf;

import net.ilx.actor.server.commands.ServerCommands;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandConfiguration {

	@Bean
	public ServerCommands serverCommands() {
		return new ServerCommands();
	}
}
