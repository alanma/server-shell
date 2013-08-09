package net.ilx.server.core.server.spring.sshd.runtime;

/**
 * Thrown if an unknown command is entered into a shell or passed on the command line
 */
public class CommandNotFoundException extends IllegalArgumentException
{
    private final String command;

    public CommandNotFoundException(String command)
    {
        super("Command not found: " + command);
        this.command = command;
    }

    public String getCommand()
    {
        return command;
    }
}