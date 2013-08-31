package net.ilx.server.shell.modules.sshd.shell.console.completion.common;

import java.util.Map;

/**
 * This is the interface for providing tab completion.
 */
public interface Completer {
	/**
	 * Returns the possible candidates for completion for the passed string.
	 * 
	 * @param buffer text to be completed
	 * @param cursor current position in the text
	 * @return map of candidate completions, and on which position in the buffer starts the completion
	 */
	public Map<String, Integer> getCandidates(String buffer, int cursor);
}
