package net.ilx.server.shell.core.server.spring.sshd.console.ssh;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.ilx.server.shell.core.server.spring.sshd.command.CommandProcessor;

import org.apache.sshd.common.Factory;
import org.apache.sshd.server.Command;

/**
 *  Shell factory used by the SSH server to create a SSH shell
 *
 */
public class SshShellFactory implements Factory<Command> {

	private List<CommandProcessor> processors;
	private Set<SshShell> shells = new HashSet<SshShell>();

	public SshShellFactory(List<CommandProcessor> processors) {
		this.processors = processors;
	}

	public synchronized Command create() {
		SshShell shell = new SshShell(processors);
		shells.add(shell);
		return shell;
	}

	public synchronized void addCommandProcessor (CommandProcessor processor) {
		processors.add(processor);
		for (SshShell shell : shells) {
			shell.addCommandProcessor(processor);
		}
	}

	public synchronized void removeCommandProcessor (CommandProcessor processor) {
		processors.remove(processor);
		for (SshShell shell : shells) {
			shell.removeCommandProcessor(processor);
		}
	}

	public void exit() {
		for(SshShell shell : shells) {
			shell.onExit();
		}
	}
}
