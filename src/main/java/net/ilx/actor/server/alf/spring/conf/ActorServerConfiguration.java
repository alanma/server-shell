package net.ilx.actor.server.alf.spring.conf;


import net.ilx.actor.server.alf.spring.components.AApplication;

import org.jboss.logging.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration("AlfConfiguration")
@Import({SshConfiguration.class, ActorConfiguration.class})
public class ActorServerConfiguration {

	private static final Logger LOG = Logger.getLogger(ActorServerConfiguration.class);

	@Bean
	public AApplication application() {
		return new AApplication();
	}

}
