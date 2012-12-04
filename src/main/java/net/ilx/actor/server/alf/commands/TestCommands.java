package net.ilx.actor.server.alf.commands;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import net.ilx.actor.server.alf.spring.components.commands.Command;

import org.jboss.logging.Logger;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;

@Command(name = "test")
public class TestCommands implements CommandMarker {

	private static final Logger LOG = Logger.getLogger(TestCommands.class);

	@CliCommand("test")
	public String execute(	@CliOption(key = "arg1", mandatory = true, specifiedDefaultValue = "val1", unspecifiedDefaultValue = "undefined")
							final String arg1,
							@CliOption(key = "arg2", mandatory = false)
							final String arg2)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream out;
		String result = "no result";
		try {
			out = new PrintStream(baos, true, "UTF-8");
			out.append("arg1 = ").append(arg1).append("\n");
			out.append("arg2 = ").append(arg2).append("\n");
			result = baos.toString("UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOG.error("Unexpected exception", e);
		}
		return result;
	}
}
