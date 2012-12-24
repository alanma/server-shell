package net.ilx.actor.server.commands;

import static net.ilx.actor.server.util.StringUtils.LINE_SEPARATOR;

import java.util.Set;

import org.apache.sshd.SshServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.ExitShellRequest;
import org.springframework.shell.core.SimpleParser;
import org.springframework.shell.core.annotation.CliCommand;

public class ServerCommands implements CommandMarker {

	@Autowired
	private SimpleParser parser;

	@Autowired
	private SshServer sshServer;

	@CliCommand("help")
	public String help() {
		StringBuilder sb = new StringBuilder();

		sb.append("Available commands are:").append(LINE_SEPARATOR);
		Set<String> availableCommands = parser.getEveryCommand();
		for (String command : availableCommands) {
			sb.append("\t").append(command).append(LINE_SEPARATOR);
		}

		return sb.toString();
	}


	@CliCommand("exit")
	public ExitShellRequest exit() {
		return ExitShellRequest.NORMAL_EXIT;
	}

}
