package net.ilx.actor.server.alf.spring.components.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import net.ilx.actor.server.alf.log.AMessages;

import org.apache.karaf.util.StreamUtils;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.jboss.logging.Logger;

public class DefaultCommand implements Command, Runnable {

	private static final Logger LOG = Logger.getLogger(DefaultCommand.class);

	private static final AMessages MESSAGES = AMessages.MESSAGES;
    final String lineSeparator = System.getProperty("line.separator");

    private InputStream in;
    private OutputStream out;
    private OutputStream err;
    private ExitCallback callback;
    private Environment environment;

	private Thread thread;

	@Override
	public void setInputStream(final InputStream in) {
		this.in = in;
	}

	@Override
	public void setOutputStream(final OutputStream out) {
		this.out = out;
	}

	@Override
	public void setErrorStream(final OutputStream err) {
		this.err = err;
	}

	@Override
	public void setExitCallback(final ExitCallback callback) {
		this.callback = callback;
	}

	@Override
	public void start(final Environment env) throws IOException {
		thread = new Thread(this, "default command");
		thread.start();
	}


	@Override
	public void destroy() {
		thread.interrupt();
		StreamUtils.close(in);
		StreamUtils.close(out);
		StreamUtils.close(err);
	}

	@Override
	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while (null != (line = br.readLine())) {
				out.write("something received: ".getBytes());
				out.write(line.getBytes());
				out.write(lineSeparator.getBytes());
			}
			out.flush();
			this.callback.onExit(0, "executed command");
		}
		catch(Throwable t) {
			LOG.error(MESSAGES.unexpectedException(t));
		}
	}
}