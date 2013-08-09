package net.ilx.server.core.server.spring.conf;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import net.ilx.server.core.server.spring.components.commands.CommandSupportConfiguration;
import net.ilx.server.core.server.spring.components.commands.EnableCommands;
import net.ilx.server.core.server.spring.sshd.SpringCommandProcessor;
import net.ilx.server.core.server.spring.sshd.SshCommandDispatcher;
import net.ilx.server.core.server.spring.sshd.command.CommandProcessor;
import net.ilx.server.core.server.spring.sshd.console.ssh.SshShellFactory;
import net.ilx.server.core.server.spring.sshd.runtime.threadio.ThreadIOImpl;
import net.ilx.server.core.server.spring.sshd.service.threadio.ThreadIO;

import org.apache.sshd.SshServer;
import org.apache.sshd.common.KeyPairProvider;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.keyprovider.FileKeyPairProvider;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.CommandFactory;
import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.UserAuth;
import org.apache.sshd.server.auth.UserAuthNone;
import org.apache.sshd.server.auth.UserAuthPassword;
import org.apache.sshd.server.command.ScpCommandFactory;
import org.apache.sshd.server.session.ServerSession;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.shell.core.SimpleExecutionStrategy;
import org.springframework.shell.core.SimpleParser;
import org.springframework.util.ResourceUtils;


@Configuration
@EnableCommands
public class SshConfiguration {

	private static final Logger LOG = Logger.getLogger(SshConfiguration.class);

	@Autowired
	private Environment env;

	@Autowired
	private CommandSupportConfiguration commandSupportConfiguration;

	@Bean
	public NamedFactory<UserAuth> userAuthFactoryNone() {
		NamedFactory<UserAuth> auth = new UserAuthNone.Factory();
		return auth;
	}

	@Bean
	public NamedFactory<UserAuth> userAuthFactoryPassword() {
		NamedFactory<UserAuth> auth = new UserAuthPassword.Factory();
		return auth;
	}

	@Bean
	public KeyPairProvider keyPairProvider() {
		String hostKeyFileInClasspath = env.getProperty("sshd.hostkey");
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
		// ilx: see https://github.com/eclipse/rt.equinox.bundles/blob/master/bundles/org.eclipse.equinox.console.ssh/src/org/eclipse/equinox/console/ssh/SshServ.java
		LOG.trace("configuring ssh server");

		SshServer sshServer = SshServer.setUpDefaultServer();
		sshServer.setPort(env.getProperty("sshd.port", Integer.class));

		sshServer.setKeyPairProvider(keyPairProvider());

		sshServer.setShellFactory(sshShellFactory());
		sshServer.setCommandFactory(scpCommandFactory());

		// auth
		List<NamedFactory<UserAuth>> userAuthFactories = new ArrayList<NamedFactory<UserAuth>>();
//		userAuthFactories.add(userAuthFactoryPassword());
		userAuthFactories.add(userAuthFactoryNone());
		sshServer.setUserAuthFactories(userAuthFactories);

		PasswordAuthenticator passwordAuthenticator = new PasswordAuthenticator() {
            public boolean authenticate(String username, String password, ServerSession session) {
            	return (username != null && username.equals(password));
            }
        };
		sshServer.setPasswordAuthenticator(passwordAuthenticator);

		LOG.trace("configured ssh server");
		return sshServer;
	}



	@Bean
	public SshShellFactory sshShellFactory() {
		List<CommandProcessor> sshCommandProcessors = commandProcessors();
		SshShellFactory sshShellFactory = new SshShellFactory(sshCommandProcessors);
		return sshShellFactory;
	}

	public List<CommandProcessor> commandProcessors() {
		List<CommandProcessor> processors = new ArrayList<CommandProcessor>();
		CommandProcessor springCommandProcessor = springCommandProcessor();

		processors.add(springCommandProcessor);
		return processors;
	}

//	@Bean
//	@Scope(proxyMode=ScopedProxyMode.INTERFACES, value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public CommandProcessor springCommandProcessor() {
		ThreadIO tio = new ThreadIOImpl();
		SimpleParser simpleCommandParser = commandSupportConfiguration.simpleCommandParser();
		SimpleExecutionStrategy executionStrategy = commandSupportConfiguration.simpleExecutionStrategy();
		CommandProcessor commandProcessor = new SpringCommandProcessor(tio, simpleCommandParser, executionStrategy);
		return commandProcessor;
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
