package net.ilx.actor.server;

import net.ilx.actor.server.spring.components.AApplication;

import org.jboss.logging.Logger;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public abstract class ActorServer {

	private static final Logger LOG = Logger.getLogger(ActorServer.class);

	private AnnotationConfigApplicationContext ctx = null;
	// private boolean stopped = false;
	static volatile boolean stopped = false;


	public void start() {
		LOG.trace("starting ActorServer");
		registerShutdownHook();
		initLogging();
		startSpring();

		AApplication app = ctx.getBean(AApplication.class);
		app.start();
		loop();

		app.stop();
		LOG.trace("ended ActorServer");
	}

	public void stop() {
		stopped = true;
	}

	private void registerShutdownHook() {
		LOG.trace("registering shutdown hook");
		final Thread mainThread = Thread.currentThread();
		Runtime.getRuntime().addShutdownHook(new ActorServerShutdownHook(mainThread));
		LOG.trace("registered shutdown hook");
	}

	private void loop() {
		while (!isStopped()) {
			LOG.trace("Server loop started");
			try {
				Thread.sleep(5000);
				// accept commands on command thread
				Thread.yield();
			} catch (InterruptedException e) {
				LOG.error("Main thread interrupted. We should exit.", e);
			}
		}
		LOG.trace("Server loop ended");
	}

	private void initLogging() {
		 SLF4JBridgeHandler.removeHandlersForRootLogger();
		 SLF4JBridgeHandler.install();
	}

	protected abstract String[] getPackagesToScan();

	protected abstract Class<?>[] getSpringConfigurations();

	private void startSpring() {
		LOG.trace("starting spring context");
		ctx = new AnnotationConfigApplicationContext();
//		ctx.register(ActorServerConfiguration.class);
//		ctx.register(SshConfiguration.class);
		ctx.scan("net.ilx.actor.server.spring.conf");

		// register additional configurations
		Class<?>[] springConfigurations = getSpringConfigurations();
		for (Class<?> configuration : springConfigurations) {
			ctx.register(configuration);
		}

		// register additional packages to scan
		String[] packagesToScan = getPackagesToScan();
		for (String scannedPackage : packagesToScan) {
			ctx.scan(scannedPackage);
		}

		ctx.refresh();

		LOG.trace("started spring context");
	}



	private boolean isStopped() {
		return stopped;
	}
}