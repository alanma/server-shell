package net.ilx.server.shell.modules.sshd.shell.console.ssh;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;

import net.ilx.server.shell.modules.sshd.shell.command.CommandProcessor;
import net.ilx.server.shell.modules.sshd.shell.command.CommandSession;
import net.ilx.server.shell.modules.sshd.shell.console.common.ConsoleInputHandler;
import net.ilx.server.shell.modules.sshd.shell.console.common.ConsoleInputScanner;
import net.ilx.server.shell.modules.sshd.shell.console.common.ConsoleInputStream;
import net.ilx.server.shell.modules.sshd.shell.console.common.ConsoleOutputStream;
import net.ilx.server.shell.modules.sshd.shell.console.common.KEYS;
import net.ilx.server.shell.modules.sshd.shell.console.common.terminal.TerminalTypeMappings;

/**
 * This class manages a ssh connection. It is responsible for wrapping the original io streams
 * from the socket, and starting a CommandSession to execute commands from the ssh.
 *
 */
public class SshSession extends Thread implements Closeable {
	private CommandProcessor processor;
	private SshShell sshShell;
	private InputStream in;
	private OutputStream out;
	private TerminalTypeMappings currentMappings;
	private Map<String, KEYS> currentEscapesToKey;

	private static final String PROMPT = "prompt";
    private static final String OSGI_PROMPT = "osgi> ";
    private static final String SCOPE = "SCOPE";
    private static final String EQUINOX_SCOPE = "equinox:*";
    private static final String INPUT_SCANNER = "INPUT_SCANNER";
    private static final String SSH_INPUT_SCANNER = "SSH_INPUT_SCANNER";
    public static final String COMMAND_PROCESSOR = "COMMAND_PROCESSOR";
    private static final String USER_STORAGE_PROPERTY_NAME = "osgi.console.ssh.useDefaultSecureStorage";
    private static final String DEFAULT_USER = "equinox";
    private static final String CLOSEABLE = "CLOSEABLE";
	private static final int ADD_USER_COUNTER_LIMIT = 2;

	public SshSession(CommandProcessor processor, SshShell sshShell, InputStream in, OutputStream out, TerminalTypeMappings currentMappings, Map<String, KEYS> currentExcapesToKey) {
		this.processor = processor;
		this.sshShell = sshShell;
		this.in = in;
		this.out = out;
		this.currentMappings = currentMappings;
		this.currentEscapesToKey = currentExcapesToKey;
	}

	public void run() {
		ConsoleInputStream input = new ConsoleInputStream();
		ConsoleOutputStream outp = new ConsoleOutputStream(out);
		// TODO ilx: why ssh input handler AND console input handler?
		SshInputHandler inputHandler = new SshInputHandler(in, input, outp);
		inputHandler.getScanner().setBackspace(currentMappings.getBackspace());
		inputHandler.getScanner().setDel(currentMappings.getDel());
		inputHandler.getScanner().setCurrentEscapesToKey(currentEscapesToKey);
		inputHandler.getScanner().setEscapes(currentMappings.getEscapes());
		inputHandler.start();

		ConsoleInputStream inp = new ConsoleInputStream();
		ConsoleInputHandler consoleInputHandler = new ConsoleInputHandler(input, inp, outp);
		consoleInputHandler.getScanner().setBackspace(currentMappings.getBackspace());
		consoleInputHandler.getScanner().setDel(currentMappings.getDel());
		consoleInputHandler.getScanner().setCurrentEscapesToKey(currentEscapesToKey);
		consoleInputHandler.getScanner().setEscapes(currentMappings.getEscapes());
// ilx	((ConsoleInputScanner)consoleInputHandler.getScanner()).setContext(context);
		consoleInputHandler.start();

		final PrintStream output = new PrintStream(outp);

		final CommandSession session = processor.createSession(inp, output, output);
		session.put(SCOPE, EQUINOX_SCOPE);
		session.put(PROMPT, OSGI_PROMPT);
		session.put(INPUT_SCANNER, consoleInputHandler.getScanner());
		session.put(SSH_INPUT_SCANNER, inputHandler.getScanner());
		session.put(COMMAND_PROCESSOR, this.processor);
		// Store this closeable object in the session, so that the disconnect command can close it
		session.put(CLOSEABLE, this);
		((ConsoleInputScanner)consoleInputHandler.getScanner()).setSession(session);

		try {
			// TODO ilx: gosh command?!!!
			// session.execute("gosh --login --noshutdown");
			console(session);
			close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

	}


	private Object console(CommandSession session)
    {
        Console console = new Console(session);
        console.run();
        return null;
    }


	public void close() throws IOException {
		this.interrupt();
		sshShell.removeSession(this);
	}

}
