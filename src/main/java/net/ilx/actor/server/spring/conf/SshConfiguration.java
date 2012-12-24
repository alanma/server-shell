package net.ilx.actor.server.spring.conf;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import net.ilx.actor.server.spring.sshd.SshCommandDispatcher;

import org.apache.sshd.SshServer;
import org.apache.sshd.common.KeyPairProvider;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.keyprovider.FileKeyPairProvider;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.CommandFactory;
import org.apache.sshd.server.UserAuth;
import org.apache.sshd.server.auth.UserAuthNone;
import org.apache.sshd.server.command.ScpCommandFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.util.ResourceUtils;


@Configuration
public class SshConfiguration {

	private static final Logger LOG = Logger.getLogger(SshConfiguration.class);

	@Autowired
	private Environment env;

	@Bean
	public NamedFactory<UserAuth> userAuthFactoryNone() {
		NamedFactory<UserAuth> auth = new UserAuthNone.Factory();
		return auth;
	}

	@Bean
	public KeyPairProvider keyPairProvider() {
		String hostKeyFileInClasspath = env.getProperty("sshd.hostkey", "classpath:hostkey.pem");
		File file = null;
		FileKeyPairProvider keyProvider = null;
		try {
			file = ResourceUtils.getFile(hostKeyFileInClasspath);
			keyProvider = new FileKeyPairProvider(new String [] { file.getAbsolutePath()} );
		} catch (FileNotFoundException e) {
			throw new IllegalStateException("sshd hostkey MUST be defined in classpath");
		}

		return keyProvider;
	}


	@Bean
	public SshServer sshServer() {
		LOG.trace("configuring ssh server");

		SshServer sshServer = SshServer.setUpDefaultServer();
		sshServer.setPort(env.getProperty("sshd.port", Integer.class, 9797));

		sshServer.setKeyPairProvider(keyPairProvider());

//		sshServer.setShellFactory(echoShellFactory());
		sshServer.setCommandFactory(scpCommandFactory());

		// auth
		List<NamedFactory<UserAuth>> userAuthFactories = new ArrayList<NamedFactory<UserAuth>>();
		userAuthFactories.add(userAuthFactoryNone());
		sshServer.setUserAuthFactories(userAuthFactories);

		LOG.trace("configured ssh server");
		return sshServer;
	}

	@Bean
	public CommandFactory customCommandFactory() {
		CommandFactory commandFactory = new CommandFactory( ) {
			@Override
			public Command createCommand(final String command) {
				Command cmd = dispatcherCommand();
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

	@Bean
	@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public SshCommandDispatcher dispatcherCommand() {
		return new SshCommandDispatcher();
	}
}
