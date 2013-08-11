package net.ilx.server.shell.core.server.spring.components;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import net.ilx.server.shell.core.server.spring.conf.ActorServerConfiguration;

import org.apache.sshd.SshServer;
import org.jboss.logging.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AApplication implements ApplicationContextAware {

	private static final Logger LOG = Logger.getLogger(AApplication.class);

	private static boolean stopped = false;

	@Autowired
	@Qualifier(ActorServerConfiguration.DEFAULT_THREAD_POOL)
	private ExecutorService executorService;

	@Autowired
	private SshServer sshServer;

	private ConfigurableApplicationContext applicationContext;

	private int shutdownTimetout;

	public AApplication(final int shutdownTimetout) {
		this.shutdownTimetout = shutdownTimetout;
	}

	public void start() {
		LOG.info("starting main program");
		initialize();
	}

	private void initialize() {
		startSshServer();
		startControl();
	}

	private void startSshServer() {
		try {
			LOG.trace("starting ssh server");
			sshServer.start();
			LOG.trace("started ssh server");
		} catch (IOException e) {
			LOG.error("Unexpected error occurred.", e);
		}
	}

	private static void startControl() {
		LOG.info("starting control thread");

		LOG.info("started control thread");
	}

	public void stop() {
		stopSshServer();

		ApplicationStoppingEvent stopEvent = new ApplicationStoppingEvent(this);
		applicationContext.publishEvent(stopEvent);

		shutdownAndAwaitTermination(executorService);

		applicationContext.stop();
	}

	void shutdownAndAwaitTermination(final ExecutorService pool) {
		pool.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!pool.awaitTermination(shutdownTimetout, TimeUnit.SECONDS)) {
				pool.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!pool.awaitTermination(shutdownTimetout, TimeUnit.SECONDS)) {
					LOG.errorv("Executor service thread pool did not terminate.");
				}
			}
		} catch (InterruptedException ie) {
			LOG.errorv(ie, "Executor service thread interrupted while waiting for termination.");
			// (Re-)Cancel if current thread also interrupted
			pool.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}

	private void stopSshServer() {
		LOG.trace("stopping SSH server");
		try {
			sshServer.stop();
		} catch (InterruptedException e) {
			LOG.error("Unexpected error occurred.", e);
		}
		LOG.trace("stopped SSH server");
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = (ConfigurableApplicationContext) applicationContext;
	}
}
