package net.ilx.actor.server.alf.spring;

import net.ilx.actor.server.alf.AMessages;

import org.apache.sshd.SshServer;
import org.apache.sshd.server.shell.ProcessShellFactory;
import org.jboss.logging.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SshConfiguration {

	private static final Logger LOG = Logger.getLogger(SshConfiguration.class);
	private static final AMessages MESSAGES = AMessages.MESSAGES;

	@Bean
	public SshServer sshServer() {
		LOG.trace(MESSAGES.starting("ssh server"));

		SshServer sshServer = SshServer.setUpDefaultServer();
		sshServer.setPort(9797);
//		sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider("hostkey.ser"));
//		sshd.setShellFactory(new ProcessShellFactory(new String[] { "/bin/sh", "-i", "-l" });
		sshServer.setShellFactory(new ProcessShellFactory(new String[] { "c:/WINDOWS/system32/cmd.exe" }));

		LOG.trace(MESSAGES.started("ssh server"));
		return sshServer;
	}
}
