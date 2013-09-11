package net.ilx.server.shell.modules.jetty.conf;

import net.ilx.server.shell.modules.jetty.commands.JettyCommands;
import net.ilx.server.shell.modules.jetty.commands.JettyDeployCommands;

import org.eclipse.jetty.deploy.DeploymentManager;
import org.eclipse.jetty.server.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JettyConfiguration {

    @Bean(name="jetty")
    public Server jettyServer() {
        Server server = new Server();
        server.addBean(deploymentManager());
        
        return server;
    }
    
    
    @Bean(name="deploymentManager")
    public DeploymentManager deploymentManager() {
    	DeploymentManager dm = new DeploymentManager();
    	return dm;
    }
    
    
    @Bean
    public JettyCommands jettyCommands() {
        return new JettyCommands();
    }
    
    @Bean
    public JettyDeployCommands testCommands() {
        return new JettyDeployCommands();
    }

}
