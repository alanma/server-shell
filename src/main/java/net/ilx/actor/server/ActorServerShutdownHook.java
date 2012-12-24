package net.ilx.actor.server;

import org.jboss.logging.Logger;

final class ActorServerShutdownHook extends Thread {
	private final Thread mainThread;
	private final Logger LOG = Logger.getLogger(ActorServerShutdownHook.class);

	ActorServerShutdownHook(final Thread mainThread) {
		this.mainThread = mainThread;
	}

	@Override
	public void run() {
		LOG.trace("ShutdownHook started");
	    ActorServer.stopped = true;
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