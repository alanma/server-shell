package net.ilx.actor.server.alf.log;

import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;
import org.jboss.logging.annotations.Cause;
import org.jboss.logging.annotations.LogMessage;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.Message.Format;

@org.jboss.logging.annotations.MessageLogger(projectCode="ALF")
public interface AMessageLogger {

		AMessageLogger LOGGER = Logger.getMessageLogger(AMessageLogger.class, AMessageLogger.class.getPackage().getName());

		@LogMessage(level=Level.ERROR)
		@Message(value="Unexpected error occured")
		void unexpectedError(@Cause Throwable cause);


		@LogMessage(level=Level.INFO)
		@Message(value="{0}", format = Format.MESSAGE_FORMAT)
		void info(String msg);

		@LogMessage(level=Level.TRACE)
		@Message(value="{0}", format = Format.MESSAGE_FORMAT)
		void trace(String msg);
}
