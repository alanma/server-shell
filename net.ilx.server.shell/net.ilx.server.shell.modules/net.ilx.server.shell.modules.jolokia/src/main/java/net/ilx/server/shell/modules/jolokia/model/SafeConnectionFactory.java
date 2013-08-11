package net.ilx.server.shell.modules.jolokia.model;

public interface SafeConnectionFactory {
	String getVersion();

	String getQueueManager();

	String getPort();

	String getHostName();

	String getChannel();
}
