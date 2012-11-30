package net.ilx.actor.server.alf.spring.conf;

import java.util.ArrayList;
import java.util.List;

import net.ilx.actor.server.alf.log.AMessages;
import net.ilx.actor.server.alf.spring.components.commands.DefaultCommand;
import net.ilx.actor.server.alf.sshd.echo.EchoShellFactory;

import org.apache.sshd.SshServer;
import org.apache.sshd.common.Factory;
import org.apache.sshd.common.KeyPairProvider;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.CommandFactory;
import org.apache.sshd.server.UserAuth;
import org.apache.sshd.server.auth.UserAuthNone;
import org.apache.sshd.server.command.ScpCommandFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.shell.ProcessShellFactory;
import org.jboss.logging.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SshConfiguration {

	private static final Logger LOG = Logger.getLogger(SshConfiguration.class);
	private static final AMessages MESSAGES = AMessages.MESSAGES;

	@Bean
	public NamedFactory<UserAuth> userAuthFactoryNone() {
		NamedFactory<UserAuth> auth = new UserAuthNone.Factory();
		return auth;
	}

	@Bean
	public KeyPairProvider keyPairProvider() {
		// new SimpleGeneratorHostKeyProvider("hostkey.ser"));
		return new SimpleGeneratorHostKeyProvider();
	}


	@Bean
	public SshServer sshServer() {
		LOG.trace(MESSAGES.starting("ssh server"));

		SshServer sshServer = SshServer.setUpDefaultServer();
		sshServer.setPort(9797);

		sshServer.setKeyPairProvider(keyPairProvider());

//		sshServer.setShellFactory(echoShellFactory());
		sshServer.setCommandFactory(scpCommandFactory());
//		sshServer.setShellFactory(windowsCmdShellFactory());

		// auth
		List<NamedFactory<UserAuth>> userAuthFactories = new ArrayList<NamedFactory<UserAuth>>();
		userAuthFactories.add(userAuthFactoryNone());
		sshServer.setUserAuthFactories(userAuthFactories);

		LOG.trace(MESSAGES.started("ssh server"));
		return sshServer;
	}

	@Bean
	public Factory<Command> echoShellFactory() {
		EchoShellFactory echoShellFactory = new EchoShellFactory();
		return echoShellFactory;
	}

	@Bean
	public Factory<Command> windowsCmdShellFactory() {
		ProcessShellFactory shellFactory = new ProcessShellFactory(new String[] { "c:/WINDOWS/system32/cmd.exe" });
		return shellFactory;
	}

	@Bean
	public CommandFactory customCommandFactory() {
		CommandFactory commandFactory = new CommandFactory( ) {
			@Override
			public Command createCommand(final String command) {
				Command cmd = new DefaultCommand();
				return cmd;
			}
		};

		return commandFactory;
	}

	@Bean
	public CommandFactory scpCommandFactory() {
		ScpCommandFactory scpCommandFactory = new ScpCommandFactory(customCommandFactory());

		return scpCommandFactory;
	}
}
