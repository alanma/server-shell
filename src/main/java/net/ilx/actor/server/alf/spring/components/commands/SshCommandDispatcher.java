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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.ExecutionStrategy;
import org.springframework.shell.core.ExitShellRequest;
import org.springframework.shell.core.SimpleParser;
import org.springframework.shell.event.ParseResult;

/**
 * Dispatches ssh commands to the relevant actor.
 *
 * @author ilonca
 */
public class SshCommandDispatcher implements Command, Runnable {

	private static final Logger LOG = Logger.getLogger(SshCommandDispatcher.class);

	private static final AMessages MESSAGES = AMessages.MESSAGES;
	final String LINE_SEPARATOR = System.getProperty("line.separator");

	@Autowired
	private CommandRegistry commandRegistry;

	@Autowired
	private ExecutionStrategy executionStrategy;

	@Autowired
	private SimpleParser simpleCommandParser;

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
		thread = new Thread(this, "DispatcherCommand");
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
				if (!line.trim().isEmpty()) {
					dispatch(line, out);
				}
			}
			this.callback.onExit(0, "executed command");
		} catch (Throwable t) {
			LOG.error(MESSAGES.unexpectedException(t), t);
		}
	}

	private void dispatch(	final String line,
							final OutputStream out)
	{
		LOG.tracev("dispatch started: ''{0}''", line);
		try {
			ParseResult parseResult = simpleCommandParser.parse(line);
			if (null != parseResult) {
				Object result = executionStrategy.execute(parseResult);
				if (result != null) {
					if (result instanceof ExitShellRequest) {
						ExitShellRequest exitShellRequest = (ExitShellRequest) result;
						// Give ProcessManager a chance to close down its
						// threads before the overall OSGi framework is
						// terminated (ROO-1938)
						executionStrategy.terminate();
					} else {
						handleExecutionResult(result, out);
					}
				}
			} else {
				LOG.warnv("command ''{0}''could not be parsed", line);
				String msg = "command not found: " + line;
				out.write(msg.getBytes());
				out.write(LINE_SEPARATOR.getBytes());
				out.flush();
			}
		} catch (Throwable t) {
			// command execution failed
			LOG.error(MESSAGES.unexpectedException(t), t);
		}
		LOG.tracev("dispatch ended: ''{0}''", line);
	}

	protected void handleExecutionResult(	final Object result,
											final OutputStream out) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		if (result instanceof Iterable<?>) {
			for (Object o : (Iterable<?>) result) {
				sb.append(o.toString()).append(LINE_SEPARATOR);
				LOG.info(o.toString());
			}
		} else {
			LOG.info(result.toString());
			sb.append(result.toString()).append(LINE_SEPARATOR);
		}
		out.write(sb.toString().getBytes());
		out.flush();
	}
}