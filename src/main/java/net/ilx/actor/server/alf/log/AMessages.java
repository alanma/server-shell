package net.ilx.actor.server.alf.log;

import org.jboss.logging.Logger.Level;
import org.jboss.logging.Messages;
import org.jboss.logging.annotations.Cause;
import org.jboss.logging.annotations.LogMessage;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.Message.Format;
import org.jboss.logging.annotations.MessageBundle;

@MessageBundle(projectCode="ALF")
public interface AMessages {

	AMessages MESSAGES = Messages.getBundle(AMessages.class);

	@LogMessage(level=Level.INFO)
	@Message(id=1, value="Starting {0}", format=Format.MESSAGE_FORMAT)
	public String starting(String what);

	@LogMessage(level=Level.INFO)
	@Message(id=2, value="Started {0}", format=Format.MESSAGE_FORMAT)
	public String started(String what);


	@LogMessage(level = Level.FATAL)
	@Message(id=500, value="Unexpected error occured.")
	public String unexpectedException(@Cause Throwable cause);


}
