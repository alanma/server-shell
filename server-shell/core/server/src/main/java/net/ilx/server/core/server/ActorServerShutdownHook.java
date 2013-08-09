package net.ilx.server.core.server;

import org.jboss.logging.Logger;

final class ActorServerShutdownHook extends Thread {
	private final Thread mainThread;
	private final Logger LOG = Logger.getLogger(ActorServerShutdownHook.class);
	private ActorServer actorServer;

	ActorServerShutdownHook(final Thread mainThread, final ActorServer actorServer) {
		this.mainThread = mainThread;
		this.actorServer = actorServer;
	}

	@Override
	public void run() {
		LOG.trace("ShutdownHook started");
	    actorServer.stop();
	    try {
	    	LOG.trace("waiting for main thread to stop");
	    	mainThread.join();
	    	LOG.trace("main thread stopped");
	    }
	    catch(InterruptedException ex) {
	    	LOG.errorv(ex, "ShutdownHook was interrupted!");
	    }
	    LOG.trace("ShutdownHook stopped");
	}
}