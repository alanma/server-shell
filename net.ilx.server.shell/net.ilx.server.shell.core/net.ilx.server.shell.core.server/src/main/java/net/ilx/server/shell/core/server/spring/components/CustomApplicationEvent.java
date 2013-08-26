package net.ilx.server.shell.core.server.spring.components;

import org.springframework.context.ApplicationEvent;

public class CustomApplicationEvent extends ApplicationEvent {

    private ApplicationEventType type;

    public CustomApplicationEvent(final Object source, ApplicationEventType type) {
        super(source);
        this.type = type;
    }

    public ApplicationEventType getType() {
        return type;
    }
}
