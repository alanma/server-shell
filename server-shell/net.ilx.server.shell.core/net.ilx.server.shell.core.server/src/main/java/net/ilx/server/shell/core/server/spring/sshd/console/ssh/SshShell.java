package net.ilx.server.shell.core.server.spring.sshd.console.ssh;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ilx.server.shell.core.server.spring.sshd.command.CommandProcessor;
import net.ilx.server.shell.core.server.spring.sshd.console.common.KEYS;
import net.ilx.server.shell.core.server.spring.sshd.console.common.terminal.ANSITerminalTypeMappings;
import net.ilx.server.shell.core.server.spring.sshd.console.common.terminal.SCOTerminalTypeMappings;
import net.ilx.server.shell.core.server.spring.sshd.console.common.terminal.TerminalTypeMappings;
import net.ilx.server.shell.core.server.spring.sshd.console.common.terminal.VT100TerminalTypeMappings;
import net.ilx.server.shell.core.server.spring.sshd.console.common.terminal.VT220TerminalTypeMappings;
import net.ilx.server.shell.core.server.spring.sshd.console.common.terminal.VT320TerminalTypeMappings;

import org.apache.sshd.server.Command;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;

/**
 * This class manages a ssh connection. It is responsible for starting a sessions to execute commands
 * from the ssh. If there are multiple CommandProcessors, a session is started for each of them.
 *
 */
public class SshShell implements Command {

	private List<CommandProcessor> processors;
	private InputStream in;
	private OutputStream out;
	private ExitCallback callback;
	private Map<CommandProcessor, SshSession> commandProcessorToConsoleThreadMap = new HashMap<CommandProcessor, SshSession>();

	private final Map<String, TerminalTypeMappings> supportedEscapeSequences;
	private static final String DEFAULT_TTYPE = File.separatorChar == '/' ? "XTERM" : "ANSI";
	private TerminalTypeMappings currentMappings;
	private Map<String, KEYS> currentEscapesToKey;
    private static final String TERMINAL_PROPERTY = "TERM";

	public SshShell(List<CommandProcessor> processors) {
		this.processors = processors;
		supportedEscapeSequences = new HashMap<String, TerminalTypeMappings> ();
        supportedEscapeSequences.put("ANSI", new ANSITerminalTypeMappings());
        supportedEscapeSequences.put("WINDOWS", new ANSITerminalTypeMappings());
        supportedEscapeSequences.put("VT100", new VT100TerminalTypeMappings());
        VT220TerminalTypeMappings vtMappings = new VT220TerminalTypeMappings();
        supportedEscapeSequences.put("VT220", vtMappings);
        supportedEscapeSequences.put("XTERM", vtMappings);
        supportedEscapeSequences.put("VT320", new VT320TerminalTypeMappings());
        supportedEscapeSequences.put("SCO", new SCOTerminalTypeMappings());

        currentMappings = supportedEscapeSequences.get(DEFAULT_TTYPE);
        currentEscapesToKey = currentMappings.getEscapesToKey();
	}

	public void setInputStream(InputStream in) {
		this.in = in;
	}

	public void setOutputStream(OutputStream out) {
		this.out = out;
	}

	public void setErrorStream(OutputStream err) {
		// do nothing
	}

	public void setExitCallback(ExitCallback callback) {
		this.callback = callback;
	}

	public synchronized void start(Environment env) throws IOException {
		String term = env.getEnv().get(TERMINAL_PROPERTY);
		TerminalTypeMappings mapping = supportedEscapeSequences.get(term.toUpperCase());
		if(mapping != null) {
			currentMappings = mapping;
			currentEscapesToKey = mapping.getEscapesToKey();
		}

		for (CommandProcessor processor : processors) {
			createNewSession(processor);
		}
	}

	public synchronized void addCommandProcessor(CommandProcessor processor) {
		createNewSession(processor);
	}

	public synchronized void removeCommandProcessor(CommandProcessor processor) {
		Thread consoleSession = commandProcessorToConsoleThreadMap.get(processor);
		if (consoleSession != null) {
			consoleSession.interrupt();
		}
	}

	private void createNewSession(CommandProcessor processor) {
		SshSession consoleSession = startNewConsoleSession(processor);
		commandProcessorToConsoleThreadMap.put(processor, consoleSession);
	}

	public void destroy() {
		return;
	}

	public void onExit() {
		if (commandProcessorToConsoleThreadMap.values() != null) {
			for (Thread consoleSession : commandProcessorToConsoleThreadMap.values()) {
				consoleSession.interrupt();
			}
		}
		callback.onExit(0);
	}

	public void removeSession(SshSession session) {
		CommandProcessor processorToRemove = null;
		for (CommandProcessor processor : commandProcessorToConsoleThreadMap.keySet()) {
			if (session.equals(commandProcessorToConsoleThreadMap.get(processor))) {
				processorToRemove = processor;
				break;
			}
		}

		if (processorToRemove != null) {
			commandProcessorToConsoleThreadMap.remove(processorToRemove);
		}

		if (commandProcessorToConsoleThreadMap.size() == 0) {
			onExit();
		}
	}

	private SshSession startNewConsoleSession(CommandProcessor processor) {
		SshSession consoleSession = new SshSession(processor, this, in, out, currentMappings, currentEscapesToKey);
        consoleSession.start();
        return consoleSession;
	}

}
