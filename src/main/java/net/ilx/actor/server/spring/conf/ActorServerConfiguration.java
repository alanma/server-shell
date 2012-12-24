package net.ilx.actor.server.spring.conf;


import net.ilx.actor.server.spring.components.AApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

@Configuration("AlfConfiguration")
@Import({SshConfiguration.class, ActorJobConfiguration.class})
public class ActorServerConfiguration {

	@Autowired
	private Environment env;

	@Bean
	public AApplication application() {
		int shutdownTimeout = env.getProperty(this.getClass().getName() + ".shutdownTimeout", Integer.class, 5000);
		return new AApplication(shutdownTimeout);
	}

}
