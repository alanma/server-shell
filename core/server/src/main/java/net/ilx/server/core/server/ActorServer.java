package net.ilx.server.core.server;

import java.io.IOException;

import net.ilx.server.core.server.spring.components.AApplication;

import org.jboss.logging.Logger;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.ResourcePropertySource;


public abstract class ActorServer {

	private static final Logger LOG = Logger.getLogger(ActorServer.class);

	private AnnotationConfigApplicationContext ctx = null;
	// private boolean stopped = false;
	private volatile boolean stopped = false;


	public void start() {
		LOG.trace("starting ActorServer");
		try {
			registerShutdownHook();
			initLogging();
			startSpring();

			AApplication app = ctx.getBean(AApplication.class);
			app.start();
			loop();

			app.stop();
		}
		catch (Throwable t) {
			LOG.error("Unexpected exception occured. Please report the bug.", t);
		}
		LOG.trace("ended ActorServer");
	}

	public void stop() {
		stopped = true;
	}

	private void registerShutdownHook() {
		LOG.trace("registering shutdown hook");
		final Thread mainThread = Thread.currentThread();
		Runtime.getRuntime().addShutdownHook(new ActorServerShutdownHook(mainThread, this));
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

	protected abstract String[] getPropertySourceResources();

	/**
	 * Use this method to register propertysources that need to be added before others (like JceksPropertySource).
	 *
	 * @return Returns preffered property source to be pre-registered.
	 */
	protected abstract PropertySource<?>[] getPropertySources();

	private void startSpring() throws IOException {
		LOG.trace("starting spring context");
		ctx = new InitializingAnnotationConfigApplicationContext() {
			protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
				// register predefined property sources
				registerPredefinedPropertySources();
			}


			private void registerPredefinedPropertySources() {
				// add predefined property sources
				addPredefinedPropertySources(getPropertySources());

				// add additional property sources
				addAdditionalPropertySources(getPropertySourceResources());
			}

			private void addPredefinedPropertySources(PropertySource<?>[] propertySources) {
				ConfigurableEnvironment environment = this.getEnvironment();
				MutablePropertySources defaultPropertySources = environment.getPropertySources();

				// add property sources in reverse order so that we end up in with the order they are defined
				for (int i = propertySources.length - 1; i >= 0; --i) {
					defaultPropertySources.addFirst(propertySources[i]);
				}
			}

			private void addAdditionalPropertySources(String[] propertySourceResources) {
				ConfigurableEnvironment environment = this.getEnvironment();
				MutablePropertySources propertySources = environment.getPropertySources();

				for (String propertySourceResource : propertySourceResources) {
					ResourcePropertySource propertySource;
					try {
						propertySource = new ResourcePropertySource(propertySourceResource);
					} catch (IOException e) {
						String msg = String.format("Unable to load resource: %s", propertySourceResource);
						throw new IllegalStateException(msg , e);
					}
					propertySources.addLast(propertySource);
				}

			}
		};

//		ctx.register(ActorServerConfiguration.class);
//		ctx.register(SshConfiguration.class);
		ctx.scan("net.ilx.server.core.server.spring.conf");

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