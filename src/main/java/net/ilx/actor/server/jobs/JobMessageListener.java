package net.ilx.actor.server.jobs;


import java.util.concurrent.Executor;

import org.jboss.logging.Logger;

import fi.jumi.actors.listeners.MessageListener;

public class JobMessageListener implements MessageListener {

	private static final Logger LOG = Logger.getLogger(JobMessageListener.class);

	private JobRegistry jobRegistry;

	public JobMessageListener(final JobRegistry jobRegistry) {
		this.jobRegistry = jobRegistry;
	}

	@Override
	public void onMessageSent(final Object message) {
		LOG.trace("onMessageSent");

	}

	@Override
	public void onProcessingStarted(final Object actor,
									final Object message)
	{
		LOG.trace("processing started");
	}

	@Override
	public void onProcessingFinished() {
		LOG.trace("processing finished");

	}

	@Override
	public Executor getListenedExecutor(final Executor realExecutor) {
		return realExecutor;
	}

}
