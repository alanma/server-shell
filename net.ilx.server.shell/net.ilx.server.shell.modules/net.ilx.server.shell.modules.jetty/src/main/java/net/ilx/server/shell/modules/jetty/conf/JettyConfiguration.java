package net.ilx.server.shell.modules.jetty.conf;

import net.ilx.server.shell.modules.jetty.commands.JettyCommands;

import org.eclipse.jetty.server.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JettyConfiguration {

    @Bean(name="jetty")
    public Server jettyServer() {
        Server server = new Server();
        return server;
    }
    
    
    @Bean
    public JettyCommands jettyCommands() {
        return new JettyCommands();
    }
}
