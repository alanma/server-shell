package net.ilx.server.shell.core.server.spring.sshd.console.completion.common.completers;

import java.util.Map;
import java.util.Set;

import net.ilx.server.shell.core.server.spring.sshd.command.CommandSession;
import net.ilx.server.shell.core.server.spring.sshd.console.completion.common.Completer;

/**
 * This class provides completion for gogo session variables.
 *
 */
public class VariableNamesCompleter implements Completer {

	private CommandSession session;

	public VariableNamesCompleter(CommandSession session) {
		this.session = session;
	}

	public Map<String, Integer> getCandidates(String buffer, int cursor) {
		// CommandSession.get(null) returns the names of all registered varialbes
		@SuppressWarnings("unchecked")
		Set<String> variableNames = (Set<String>) session.get(null);
		StringsCompleter completer = new StringsCompleter(variableNames, false);
		return completer.getCandidates(buffer, cursor);
	}

}
