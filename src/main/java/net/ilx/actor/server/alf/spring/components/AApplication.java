package net.ilx.actor.server.alf.spring.components;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import net.ilx.actor.server.alf.actors.Greeter;
import net.ilx.actor.server.alf.log.AMessageLogger;
import net.ilx.actor.server.alf.log.AMessages;

import org.apache.sshd.SshServer;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import fi.jumi.actors.ActorRef;

@Component
public class AApplication {
	private static final AMessageLogger LOG = Logger.getMessageLogger(AMessageLogger.class, AApplication.class.getPackage().getName());
	// ILI: private static final Logger LOG = Logger.getLogger(AlfClient.class);
	private static final AMessages MESSAGES = AMessages.MESSAGES;

	private static boolean stopped = false;

	@Autowired
	@Qualifier("greeter")
	private ActorRef<Greeter> actor;

	@Autowired
	@Qualifier("greeter")
	private ActorRef<Greeter> actor2;

	@Autowired
	@Qualifier("actorsThreadPool")
	private ExecutorService executorService;

	@Autowired
	private SshServer sshServer;

	public void start() {
		LOG.info(MESSAGES.starting("main program"));
		initialize();
		actor.tell().sayGreeting("World!!!!");
		actor2.tell().sayGreeting("underground!!!!");
	}

	private void initialize() {
		startSshServer();
		startControl();
	}

	private void startSshServer() {
		try {
			LOG.trace(MESSAGES.starting("ssh server"));
			sshServer.start();
			LOG.trace(MESSAGES.started("ssh server"));
		} catch (IOException e) {
			LOG.unexpectedError(e);
		}
	}

	private static void startControl() {
		LOG.info(MESSAGES.starting("control thread"));

		LOG.info(MESSAGES.started("control thread"));
	}

	public void stop() {
		stopActors(actor, actor2);
		stopSshServer();
		executorService.shutdown();
	}

	private void stopSshServer() {
		try {
			sshServer.stop();
		} catch (InterruptedException e) {
			LOG.unexpectedError(e);
		}
	}

	private void stopActors(final ActorRef<Greeter>... actors) {
		for (ActorRef<Greeter> actor : actors) {
			actor.tell().stop();
		}
	}
}
