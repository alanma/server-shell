package net.ilx.actor.server.alf.spring.components.commands.hint;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class HintCapturingHandler extends Handler {
	private StringBuilder sb = new StringBuilder();

	public HintCapturingHandler() {
		super();
	}

	@Override
	public synchronized void publish(final LogRecord record) {
		String msg = record.getMessage();
		sb.append(msg);
	}

	public String getCapturedString() {
		return sb.toString();
	}

	@Override
	public void flush() {
	}

	@Override
	public void close() throws SecurityException {
	}
}
