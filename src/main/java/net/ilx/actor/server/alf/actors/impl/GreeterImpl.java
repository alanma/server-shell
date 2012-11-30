package net.ilx.actor.server.alf.actors.impl;

import net.ilx.actor.server.alf.actors.Greeter;
import net.ilx.actor.server.alf.log.AMessages;

import org.jboss.logging.Logger;


public class GreeterImpl implements Greeter {

	private static final Logger LOG = Logger.getLogger(GreeterImpl.class);
	private static final AMessages MESSAGES = AMessages.MESSAGES;
	{
		LOG.debug(MESSAGES.starting("new Greeter " + this.toString() + " on thread " + Thread.currentThread()));
	}

	@Override
	public void sayGreeting(final String name) {
		LOG.trace("say Greeting invoked in: " + this.toString());
		try {
			LOG.info("Hello " + name + " from " + Thread.currentThread().getName());
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			LOG.error("thread interrupted!", e);
		}
		LOG.trace("say Greeting ended in: " + this.toString());
	}

	@Override
	public void stop() {
		LOG.debug("stopping Greeter " + this.toString() + "on thread " + Thread.currentThread());
	}
}
