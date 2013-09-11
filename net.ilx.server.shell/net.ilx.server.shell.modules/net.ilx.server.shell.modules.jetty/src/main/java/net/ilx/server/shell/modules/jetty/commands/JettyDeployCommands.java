package net.ilx.server.shell.modules.jetty.commands;

import java.io.File;

import net.ilx.server.shell.modules.jetty.conf.JettyConfiguration;

import org.eclipse.jetty.deploy.DeploymentManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;

/**
 * 
 * - http://wiki.eclipse.org/Jetty/Howto/Deploy_Web_Applications
 * - http://download.eclipse.org/jetty/stable-7/apidocs/org/eclipse/jetty/deploy/DeploymentManager.html
 * - http://wiki.eclipse.org/Jetty/Feature/Deployment_Manager
 * 
 * - http://wiki.eclipse.org/Jetty/Feature/Hot_Deployment
 * 
 */
public class JettyDeployCommands implements CommandMarker {

	
	@Autowired
	private JettyConfiguration jettyConfiguration;
	
	
    @CliCommand("jetty deploy war")
    public String deployWar(
    		@CliOption(key="file", mandatory=true, help="file path of war to deploy")
    		File war,
    		@CliOption(key="context", help="context root under which this application will be registered")
    		String contextRoot
    		) 
    {
    	DeploymentManager deploymentManager = jettyConfiguration.deploymentManager();
    	
        return "war deployed";
    }

    
    @CliCommand("jetty dm status")
    public String dmStatus() {
    	DeploymentManager deploymentManager = jettyConfiguration.deploymentManager();
    	
    	String state = deploymentManager.getState();
    	
    	return state;
    }
    
}
