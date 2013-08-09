package net.ilx.server.core.server.spring.components.commands.hint;

import net.ilx.server.core.server.spring.sshd.command.CommandSession;

public class SshSessionContextHolder {
	// Class fields
	private static ThreadLocal<CommandSession> sshSessionContextHolder = new ThreadLocal<CommandSession>();


	public static CommandSession getSshSession() {
		return sshSessionContextHolder.get();
	}

	public static void setSshSession(CommandSession session) {
		sshSessionContextHolder.set(session);
	}

	public static void resetSshSession() {
		sshSessionContextHolder.remove();
	}

	private SshSessionContextHolder () {}
}
