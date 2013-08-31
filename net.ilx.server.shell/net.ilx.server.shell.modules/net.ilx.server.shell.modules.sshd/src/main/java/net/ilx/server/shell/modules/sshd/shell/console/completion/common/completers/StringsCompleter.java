package net.ilx.server.shell.modules.sshd.shell.console.completion.common.completers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.ilx.server.shell.modules.sshd.shell.console.completion.CommandLineParser;
import net.ilx.server.shell.modules.sshd.shell.console.completion.common.Completer;

/**
 * This class provides completion for arbitrary strings
 *
 */
public class StringsCompleter implements Completer {

	private Set<String> strings;
	private boolean isCaseSensitive;

	public StringsCompleter(Set<String> strings, boolean isCaseSensitive) {
		this.strings = strings;
		this.isCaseSensitive = isCaseSensitive;
	}

	public Map<String, Integer> getCandidates(String buffer, int cursor) {
		String currentToken = CommandLineParser.getCurrentToken(buffer, cursor);
		if (currentToken == null) {
			return new HashMap<String, Integer>();
		}
		if (!isCaseSensitive) {
			currentToken = currentToken.toLowerCase();
		}

		int startIndex = cursor - currentToken.length();

		// if currentToken is empty string, then there is nothing to complete
		// the only exception is if the previous character is $, which signifies
		// that a variable name is expected; in this case all strings will be
		// returned as candidates
		if(currentToken.equals("") && buffer.charAt(startIndex - 1) != '$') {
			return new HashMap<String, Integer>();
		}

		Map<String, Integer> result = new HashMap<String, Integer>();

		for(String candidate : strings) {
			if (isCaseSensitive) {
				if (candidate.startsWith(currentToken)) {
					result.put(candidate, startIndex);
				}
			} else {
				if (candidate.toLowerCase().startsWith(currentToken)) {
					result.put(candidate, startIndex);
				}
			}
		}

		return result;
	}

}
