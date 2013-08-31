package net.ilx.server.shell.modules.sshd.shell.console.completion.common.completers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.ilx.server.shell.modules.sshd.shell.command.CommandSession;
import net.ilx.server.shell.modules.sshd.shell.console.completion.CommandLineParser;
import net.ilx.server.shell.modules.sshd.shell.console.completion.common.Completer;

/**
 * This class provides completion for command names.
 *
 */
public class CommandNamesCompleter implements Completer {
	private CommandSession session;

	private static final String COMMANDS = ".commands";

	public CommandNamesCompleter(CommandSession session) {
		this.session = session;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Integer> getCandidates(String buffer, int cursor) {
		// TODO ilx: get commands from session.... Set<String> commandNames = parser.getEveryCommand();
		Set<String> commandNames = null;

		// CommandSession.get(".commands") returns the names of all registered commands
		if (commandNames == null || commandNames.isEmpty()) {
			commandNames = (Set<String>) session.get(COMMANDS);
		}

		// command names are stored in the session in lower case
		String currentToken = CommandLineParser.getCurrentToken(buffer, cursor).toLowerCase();
		if(currentToken == null || currentToken.equals("")) {
			return new HashMap<String, Integer>();
		}

		if (!currentToken.contains(":")) {
			// the current token does not contain a scope qualifier, so remove scopes from possible candidates
			commandNames = clearScopes(commandNames);
		}
		StringsCompleter completer = new StringsCompleter(commandNames, true);
		return completer.getCandidates(buffer, cursor);
	}

	private Set<String> clearScopes(Set<String> commandNames) {
		Set<String> clearedCommandNames = new HashSet<String>();

		for(String commandName : commandNames) {
			clearedCommandNames.add(commandName.substring(commandName.indexOf(":") + 1));
		}

		return clearedCommandNames;
	}
}
