package net.ilx.actor.server.spring.components;

import org.springframework.context.ApplicationEvent;

public class ApplicationStoppingEvent extends ApplicationEvent {

	public ApplicationStoppingEvent(final Object source) {
		super(source);
	}

}
