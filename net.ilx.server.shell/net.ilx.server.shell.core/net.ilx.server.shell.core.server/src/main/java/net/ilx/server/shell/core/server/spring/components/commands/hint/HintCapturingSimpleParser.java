package net.ilx.server.shell.core.server.spring.components.commands.hint;

import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.Converter;
import org.springframework.shell.core.SimpleParser;
import org.springframework.shell.event.ParseResult;
import org.springframework.shell.support.logging.HandlerUtils;

public class HintCapturingSimpleParser extends SimpleParser implements InitializingBean {
	// ilx added
	private static final org.jboss.logging.Logger LOG = org.jboss.logging.Logger.getLogger(HintCapturingSimpleParser.class);
	private HintDisplayCommand hintDisplayCommand = new HintDisplayCommand();
	private Method displayMethod;

	@Autowired
	private List<Converter<?>> converters;


	public HintCapturingSimpleParser() {
		displayMethod = BeanUtils.findMethod(HintDisplayCommand.class, "display", String.class, String.class);
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

	public String getHelp(String buffer) {
		Logger logger = HandlerUtils.getLogger(SimpleParser.class);
		HintCapturingHandler handler = new HintCapturingHandler();
		logger.addHandler(handler);
		String help = "????";
		try {
			obtainHelp(buffer);
			help = handler.getCapturedString();
		}
		finally {
			logger.removeHandler(handler);
		}
		return help;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		for (Converter<?> converter : converters) {
			this.add(converter);
		}
	}

}
