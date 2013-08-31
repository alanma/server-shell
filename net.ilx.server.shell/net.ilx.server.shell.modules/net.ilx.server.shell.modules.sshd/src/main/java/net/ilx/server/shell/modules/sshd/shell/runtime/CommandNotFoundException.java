package net.ilx.server.shell.modules.sshd.shell.runtime;

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