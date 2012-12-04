package net.ilx.actor.server;

import net.ilx.actor.server.alf.log.AMessageLogger;
import net.ilx.actor.server.alf.log.AMessages;
import net.ilx.actor.server.alf.spring.components.AApplication;

import org.jboss.logging.Logger;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class ActorServer {

	private static final AMessageLogger LOG = Logger.getMessageLogger(AMessageLogger.class, ActorServer.class.getPackage().getName());
	private static final AMessages MESSAGES = AMessages.MESSAGES;

	private static AnnotationConfigApplicationContext ctx = null;
	private static boolean stopped = false;

	public static void main(final String[] args) {
		LOG.info(MESSAGES.starting("main program"));
		initLogging();
		startSpring();

		AApplication app = ctx.getBean(AApplication.class);
		app.start();
		loop();

		app.stop();
	}

	private static void loop() {
		while (!isStopped()) {
			LOG.trace("Server loop started");
			try {
				Thread.sleep(5000);
				// accept commands on command thread
				Thread.yield();
			} catch (InterruptedException e) {
				LOG.unexpectedError(e);
			}
		}
	}

	private static void initLogging() {
		 SLF4JBridgeHandler.removeHandlersForRootLogger();
		 SLF4JBridgeHandler.install();
	}

	private static void startSpring() {
		LOG.info(MESSAGES.starting("spring context"));
		ctx = new AnnotationConfigApplicationContext();
//		ctx.register(ActorServerConfiguration.class);
//		ctx.register(SshConfiguration.class);
		ctx.scan("net.ilx.actor.server.alf.spring.conf");
		ctx.refresh();

		LOG.info(MESSAGES.started("spring context"));
	}

	private static boolean isStopped() {
		return stopped;
	}
}