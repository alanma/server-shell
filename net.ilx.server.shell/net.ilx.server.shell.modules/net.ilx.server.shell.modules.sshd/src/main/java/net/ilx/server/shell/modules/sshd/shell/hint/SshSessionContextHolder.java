package net.ilx.server.shell.modules.sshd.shell.hint;

import net.ilx.server.shell.modules.sshd.shell.command.CommandSession;

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
