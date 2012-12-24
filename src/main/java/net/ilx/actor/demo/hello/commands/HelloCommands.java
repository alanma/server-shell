package net.ilx.actor.demo.hello.commands;

import net.ilx.actor.server.spring.components.commands.Command;

import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;

@Command(name = "hello")
public class HelloCommands  implements CommandMarker {

    @CliCommand(value = "hello", help = "prints hello response")
    public String hello(@CliOption(key = "msg", mandatory = true, help = "message")
    final String msg)
    {
        return "Hello " + msg;
    }


}
