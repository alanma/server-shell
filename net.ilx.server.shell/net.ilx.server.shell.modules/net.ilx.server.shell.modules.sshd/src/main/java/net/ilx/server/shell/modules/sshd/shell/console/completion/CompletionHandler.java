package net.ilx.server.shell.modules.sshd.shell.console.completion;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import net.ilx.server.shell.modules.sshd.shell.command.CommandProcessor;
import net.ilx.server.shell.modules.sshd.shell.command.CommandSession;
import net.ilx.server.shell.modules.sshd.shell.console.completion.common.Completer;
import net.ilx.server.shell.modules.sshd.shell.console.completion.common.completers.CommandNamesCompleter;
import net.ilx.server.shell.modules.sshd.shell.console.completion.common.completers.FileNamesCompleter;
import net.ilx.server.shell.modules.sshd.shell.console.completion.common.completers.VariableNamesCompleter;
import net.ilx.server.shell.modules.sshd.shell.console.ssh.SshSession;

/**
 * This class aggregates the different types of completers - variable, command and
 * file completers. It also searches for registered custom completers and if available
 * uses them too. It call all completers and finally returns the completion candidates
 * returned from all of them.
 *
 */
public class CompletionHandler {
	private CommandSession session;
	Set<Completer> completers;
	private static final String FILE = "file";
	private static final char VARIABLE_PREFIX = '$';

	public CompletionHandler(CommandSession session) {
		this.session = session;
		completers = new HashSet<Completer>();
	}

	public Map<String, Integer> getCandidates(byte[] buf, int cursor) {
        String currentInput = new String(buf);
        String currentToken = CommandLineParser.getCurrentToken(currentInput, cursor);
        if (currentToken ==  null){
        	return new HashMap<String, Integer>();
        }
        if (currentToken.contains(FILE) == true) {
        	completers.add(new FileNamesCompleter());
        }else{
         	if ((cursor - currentToken.length() > 0) && (buf[cursor - currentToken.length() - 1] == VARIABLE_PREFIX)){
        		completers.add(new VariableNamesCompleter(session));
        	} else {
        		completers.add(new CommandNamesCompleter(session));
        		completers.add(new FileNamesCompleter());
        	}
        }
        lookupCustomCompleters();
		Map<String, Integer> candidates = new TreeMap<String, Integer>();
		for (Completer completer : completers) {
			candidates.putAll(completer.getCandidates(currentInput, cursor));
		}

		return candidates;
	}

	private void lookupCustomCompleters (){
		// retrieve completers from session ...
		CommandProcessor commandProcessor = (CommandProcessor) session.get(SshSession.COMMAND_PROCESSOR);
		Completer.class.isAssignableFrom(commandProcessor.getClass());
		if (commandProcessor instanceof Completer) {
			Completer completer = (Completer) commandProcessor;
			completers.add(completer);
		}
	}

}
