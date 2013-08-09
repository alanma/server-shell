package net.ilx.server.core.server.spring.components;

import org.springframework.context.ApplicationEvent;

public class ApplicationStoppingEvent extends ApplicationEvent {

	public ApplicationStoppingEvent(final Object source) {
		super(source);
	}

}
