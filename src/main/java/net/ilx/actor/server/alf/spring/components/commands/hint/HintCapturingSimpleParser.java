package net.ilx.actor.server.alf.spring.components.commands.hint;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.logging.Logger;

import org.springframework.beans.BeanUtils;
import org.springframework.shell.core.Converter;
import org.springframework.shell.core.SimpleParser;
import org.springframework.shell.event.ParseResult;
import org.springframework.shell.support.logging.HandlerUtils;

public class HintCapturingSimpleParser extends SimpleParser {

	private static final org.jboss.logging.Logger LOG = org.jboss.logging.Logger.getLogger(HintCapturingSimpleParser.class);
	private HintDisplayCommand hintDisplayCommand = new HintDisplayCommand();
	private Method displayMethod;

	public HintCapturingSimpleParser(final Set<Converter<?>> converters) {
		displayMethod = BeanUtils.findMethod(HintDisplayCommand.class, "display", String.class, String.class);
		for (Converter<?> converter : converters) {
			this.add(converter);
		}
	}

	@Override
	public ParseResult parse(final String rawInput) {
		Logger logger = HandlerUtils.getLogger(SimpleParser.class);
		HintCapturingHandler handler = new HintCapturingHandler();
		logger.addHandler(handler);

		ParseResult parseResult = null;
		try {
			String capturedString = "";
			try {
				parseResult = super.parse(rawInput);
				if (null == parseResult) {
					// create default parseResult that will invoke display command
					capturedString = handler.getCapturedString();
					parseResult = new ParseResult(displayMethod, hintDisplayCommand, new Object[] {rawInput, capturedString});
				}
			}
			catch (Throwable t) {
				capturedString = t.getMessage();
				LOG.errorv(t, "Parsing of ''{0}'' failed.", rawInput);
				parseResult = new ParseResult(displayMethod, hintDisplayCommand, new Object[] {rawInput, capturedString});
			}
		}
		finally {
			logger.removeHandler(handler);
		}

		return parseResult;
	}

}
