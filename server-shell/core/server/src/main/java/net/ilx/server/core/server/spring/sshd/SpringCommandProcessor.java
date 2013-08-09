package net.ilx.server.core.server.spring.sshd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.ilx.server.core.server.spring.components.commands.hint.SshSessionContextHolder;
import net.ilx.server.core.server.spring.sshd.command.CommandSession;
import net.ilx.server.core.server.spring.sshd.command.Function;
import net.ilx.server.core.server.spring.sshd.console.completion.common.Completer;
import net.ilx.server.core.server.spring.sshd.runtime.CommandProcessorImpl;
import net.ilx.server.core.server.spring.sshd.service.threadio.ThreadIO;

import org.springframework.shell.core.Completion;
import org.springframework.shell.core.ExecutionStrategy;
import org.springframework.shell.core.ExitShellRequest;
import org.springframework.shell.core.SimpleParser;
import org.springframework.shell.event.ParseResult;

public class SpringCommandProcessor extends CommandProcessorImpl implements Completer {

	private SimpleParser simpleCommandParser;
	private ExecutionStrategy executionStrategy;

	public SpringCommandProcessor(ThreadIO threadIO, SimpleParser simpleCommandParser, ExecutionStrategy executionStrategy) {
		super(threadIO);
		this.simpleCommandParser = simpleCommandParser;
		this.executionStrategy = executionStrategy;
	}


	@Override
	public Set<String> getCommands() {
		return simpleCommandParser.getEveryCommand();
	}



	@Override
	public Function getCommand(final String name, final Object path) {
		Function springCommandFunction = new Function() {
			@Override
			public Object execute(CommandSession session, List<Object> arguments) throws Exception {
				String rawInput = buildRawInput(name, arguments);
				// ilx: wrap ssh session around parse?
				ParseResult parseResult = simpleCommandParser.parse(rawInput);

				Object result = null;
				if (null != parseResult) {
					try {
						SshSessionContextHolder.setSshSession(session);
						result = executionStrategy.execute(parseResult);
					}
					finally {
						SshSessionContextHolder.resetSshSession();
					}
					if (result != null) {
						if (result instanceof ExitShellRequest) {
							executionStrategy.terminate();
							char[] quit = {'\n'};
							String quitCommand = new String(quit);
							session.execute(quitCommand);
						}
					}
				}
				return result;
			}

			private String buildRawInput(String name, List<Object> arguments) {
				StringBuilder sb = new StringBuilder();
				sb.append(name);
				for (Object arg : arguments) {
					sb.append(" ").append(arg);
				}
				return sb.toString();
			}
		};
		return springCommandFunction;
	}


	@Override
	public Map<String, Integer> getCandidates(String buffer, int cursor) {
		Map<String,Integer> result = new HashMap<String, Integer>();

		List<Completion> completions = new ArrayList<Completion>();
		if (null == simpleCommandParser) {
			throw new IllegalStateException("SpringCommandProcessor#simpleCommandParser is null. Completion will fail.");
		}
		simpleCommandParser.completeAdvanced(buffer, cursor, completions);
		for (Completion completion : completions) {
			String value = completion.getValue();
			Integer order = Integer.valueOf(completion.getOrder());
			result.put(value, 0);
		}
		return result;
	}

}
