package net.ilx.actor.server.alf.spring.components.commands;

import net.ilx.actor.server.alf.spring.components.registry.AbstractRegistry;

import org.springframework.shell.core.CommandMarker;


public class CommandRegistry extends AbstractRegistry<String, CommandMarker> {

	public void destroy() {
		// remove all commands
		registrar.clear();
	}

}
