package net.ilx.actor.demo;

import net.ilx.actor.demo.hello.configuration.HelloConfiguration;
import net.ilx.actor.server.ActorServer;

public class DemoClient extends ActorServer {

	private static DemoClient client = new DemoClient();

	@Override
	protected String[] getPackagesToScan() {
		return new String[]{};
	}

	@Override
	protected Class<?>[] getSpringConfigurations() {
		return new Class<?>[]{ HelloConfiguration.class};
	}

	public static void stopClient() {
		client.stop();
	}

	public static void main(final String[] args) {
		Thread t = new Thread ( "DemoClient-main") {
			@Override
			public void run() {
				client.start();
			};
		};
		t.start();
	}
}