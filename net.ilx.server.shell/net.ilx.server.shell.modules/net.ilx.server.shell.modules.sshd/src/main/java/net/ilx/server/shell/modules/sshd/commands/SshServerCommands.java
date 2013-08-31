package net.ilx.server.shell.modules.sshd.commands;

import java.io.InputStream;
import java.util.Collections;
import java.util.Set;

import net.ilx.server.shell.core.utils.alf.server.util.StringUtils;
import net.ilx.server.shell.modules.sshd.shell.command.CommandSession;
import net.ilx.server.shell.modules.sshd.shell.console.common.ConsoleInputStream;
import net.ilx.server.shell.modules.sshd.shell.hint.SshSessionContextHolder;
import net.ilx.server.shell.modules.sshd.shell.runtime.CommandSessionImpl;

import org.apache.sshd.SshServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.ExitShellRequest;
import org.springframework.shell.core.SimpleParser;
import org.springframework.shell.core.annotation.CliCommand;

public class SshServerCommands implements CommandMarker {

    @Autowired
    private SimpleParser parser;

    @Autowired
    private SshServer sshServer;

    @Autowired
    private Environment env;

    @Autowired
    private ApplicationContext appCtxt;

    @CliCommand("exit")
    public ExitShellRequest exit()
    {
        CommandSession commandSession = SshSessionContextHolder.getSshSession();
        if (null != commandSession) {
            InputStream keyboard = commandSession.getKeyboard();
            if (keyboard instanceof ConsoleInputStream) {
                ConsoleInputStream is = (ConsoleInputStream)keyboard;
                is.add(new byte[] {4});
            }
        }

        return ExitShellRequest.NORMAL_EXIT;
    }


    @SuppressWarnings("unchecked")
//	@CliCommand("var list")
    public String listVars()
    {
        CommandSession commandSession = SshSessionContextHolder.getSshSession();
        Set<String> vars = Collections.emptySet();
        StringBuilder sb = new StringBuilder();
        if (null != commandSession) {
            Object set = commandSession.get(CommandSessionImpl.VARIABLES);
            vars = (Set<String>) set;
            for (String command : vars) {
                Object value = commandSession.get(command);
                sb.append(command).append("=").append(value).append(StringUtils.LINE_SEPARATOR);
            }
        }

        return sb.toString();
    }


}
