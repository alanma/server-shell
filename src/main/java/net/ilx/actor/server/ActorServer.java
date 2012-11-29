package net.ilx.actor.server;

import fi.jumi.actors.ActorRef;
import fi.jumi.actors.ActorThread;

import java.util.concurrent.ExecutorService;

import net.ilx.actor.server.alf.AMessageLogger;
import net.ilx.actor.server.alf.AMessages;
import net.ilx.actor.server.alf.spring.AServerConfiguration;

import org.jboss.logging.Logger;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class ActorServer {

	private static final AMessageLogger LOG = Logger.getMessageLogger(AMessageLogger.class, ActorServer.class.getPackage().getName());
	// ILI: private static final Logger LOG = Logger.getLogger(AlfClient.class);
	private static final AMessages MESSAGES = AMessages.MESSAGES;

	private static boolean stopped = true;
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

		}

		ActorThread actorThread = ctx.getBean(ActorThread.class);
		actorThread.stop();

		ExecutorService executorService = ctx.getBean("actorsThreadPool", ExecutorService.class);
		executorService.shutdown();
	}

	private static void initialize() {
		initLogging();
		startSpring();
		startSsh();
		startControl();
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
		ctx.refresh();

		LOG.info(MESSAGES.started("spring context"));
	}

	private static void startControl() {
		LOG.info(MESSAGES.starting("control thread"));

		LOG.info(MESSAGES.started("control thread"));
	}

	private static void startSsh() {
		LOG.info(MESSAGES.starting("ssh daemon"));

		LOG.info(MESSAGES.started("ssh daemon"));
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
	}

}