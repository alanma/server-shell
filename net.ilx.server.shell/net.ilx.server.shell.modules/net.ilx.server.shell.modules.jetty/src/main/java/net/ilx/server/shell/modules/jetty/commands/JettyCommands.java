package net.ilx.server.shell.modules.jetty.commands;

import net.ilx.server.shell.core.utils.alf.server.util.StringUtils;
import net.ilx.server.shell.modules.jetty.conf.JettyConfiguration;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;

public class JettyCommands implements CommandMarker {
    
    @Autowired
    JettyConfiguration jettyConfiguration;
    
    @CliCommand("jetty start")
    public String start(
            @CliOption(key="port", help="port to runt http server", specifiedDefaultValue="8080",unspecifiedDefaultValue="8080")
            String port)
    {
        String res = "jetty started";
        Server server = jettyConfiguration.jettyServer();
        
        try {
            ServerConnector connector = new ServerConnector(server);
            connector.setPort(Integer.parseInt(port));
            server.addConnector(connector);
            server.start();
        } catch (Exception e) {
            res = e.toString();
        }
        
        return res;
    }
    
        
    @CliCommand("jetty stop")
    public String stop() {
        String res = "jetty stopped";
        Server server = jettyConfiguration.jettyServer();
        try {
            server.stop();
            server.join();
        } catch (Exception e) {
            res = e.toString();
        }
        
        return res;
    }
    
    @CliCommand("jetty status")
    public String status() {
        String res = "unknown";
        Server server = jettyConfiguration.jettyServer();
        try {
            res = server.getState();
        } catch (Exception e) {
            res = e.toString();
        }
        
        return res;
    }
    
    
    
    @CliCommand("jetty info")
    public String info() {
        Server server = jettyConfiguration.jettyServer();
        Connector[] connectors = server.getConnectors();
        StringBuilder sb = new StringBuilder();
        
        sb.append("found ").append(connectors.length).append(" connectors").append(StringUtils.LINE_SEPARATOR);
        for (Connector connector : connectors) {
            sb.append(connector).append(StringUtils.LINE_SEPARATOR);
        }
        return sb.toString();
    }

}
