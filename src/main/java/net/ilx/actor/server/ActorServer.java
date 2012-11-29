package net.ilx.actor.server;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import net.ilx.actor.server.alf.AMessageLogger;
import net.ilx.actor.server.alf.AMessages;
import net.ilx.actor.server.alf.spring.AServerConfiguration;
import net.ilx.actor.server.alf.spring.SshConfiguration;

import org.apache.sshd.SshServer;
import org.jboss.logging.Logger;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import fi.jumi.actors.ActorRef;


public class ActorServer {

	private static final AMessageLogger LOG = Logger.getMessageLogger(AMessageLogger.class, ActorServer.class.getPackage().getName());
	// ILI: private static final Logger LOG = Logger.getLogger(AlfClient.class);
	private static final AMessages MESSAGES = AMessages.MESSAGES;

	private static boolean stopped = false;
	private static AnnotationConfigApplicationContext ctx = null;

	public static void main(final String[] args) {
		LOG.info(MESSAGES.starting("main program"));
		initialize();
		ActorRef<Greeter> actor = ctx.getBean("greeter", ActorRef.class );
		ActorRef<Greeter> actor2 = ctx.getBean("greeter", ActorRef.class );
		actor.tell().sayGreeting("World!!!!");
		actor2.tell().sayGreeting("underground!!!!");
		while (!isStopped()) {
			// accept commands on command thread
			Thread.yield();
		}

		stopActors(actor, actor2);
		stopSshServer();

		ExecutorService executorService = ctx.getBean("actorsThreadPool", ExecutorService.class);
		executorService.shutdown();
	}

	private static void stopSshServer() {
		SshServer sshServer = ctx.getBean(SshServer.class);
		try {
			sshServer.stop();
		} catch (InterruptedException e) {
			LOG.unexpectedError(e);
		}
	}

	private static void stopActors(	final ActorRef<Greeter>... actors)
	{
		for (ActorRef<Greeter> actor : actors) {
			actor.tell().stop();
		}
	}

	private static void initialize() {
		initLogging();
		startSpring();
		startSshServer();
		startControl();
	}

	private static void startSshServer() {
		SshServer sshServer = ctx.getBean(SshServer.class);
		try {
			sshServer.start();
		} catch (IOException e) {
			LOG.unexpectedError(e);
		}
	}


	private static void initActorFramework() {

	}

	private static void initLogging() {
		 SLF4JBridgeHandler.removeHandlersForRootLogger();
		 SLF4JBridgeHandler.install();
	}

	private static void startSpring() {
		LOG.info(MESSAGES.starting("spring context"));
		ctx = new AnnotationConfigApplicationContext();
		ctx.register(AServerConfiguration.class);
		ctx.register(SshConfiguration.class);
		ctx.refresh();

		LOG.info(MESSAGES.started("spring context"));
	}

	private static void startControl() {
		LOG.info(MESSAGES.starting("control thread"));

		LOG.info(MESSAGES.started("control thread"));
	}

	private static boolean isStopped() {
		return stopped;
	}

	public interface Greeter {
		// Methods on actor interfaces must return void and not have throws
		// declarations.
		// Any parameters may be used, but immutable ones are strongly
		// encouraged.
		// Actors should always be passed around as ActorRefs.
		void sayGreeting(String name);

		void stop();
	}

}