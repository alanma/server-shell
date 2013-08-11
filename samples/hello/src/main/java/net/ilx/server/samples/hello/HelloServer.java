package net.ilx.server.samples.hello;

import net.ilx.server.shell.core.server.ActorServer;

import org.springframework.core.env.PropertySource;


public class HelloServer extends ActorServer {

	private static HelloServer server = new HelloServer();

	@Override
	protected String[] getPackagesToScan() {
		return new String[]{};
	}

	@Override
	protected Class<?>[] getSpringConfigurations() {
		return new Class<?>[] {
		};
	}

	@Override
	protected String[] getPropertySourceResources() {
		return new String[] {
				"classpath:/hello.properties"
		};
	}

	@Override
	protected PropertySource<?>[] getPropertySources() {
		return new PropertySource<?>[] {};
	}


	public static void stopClient() {
		server.stop();
	}

	public static void main(final String[] args) {
		Thread t = new Thread ("HelloServer-main") {
			@Override
			public void run() {
				server.start();
			};
		};
		t.start();
	}

}