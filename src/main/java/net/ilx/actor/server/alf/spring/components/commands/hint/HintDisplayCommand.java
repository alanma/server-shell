package net.ilx.actor.server.alf.spring.components.commands.hint;

/**
 * Not a real command. Used only to simulate command execution.
 *
 * @author ilonca
 */
public class HintDisplayCommand {

	public String display(final String command, final String hint) {
		return String.format("Execution of command '%s' failed. Hint: %s.", command, hint);
	}
}
