package net.ilx.server.shell.core.server.spring.sshd.console.common;

import java.io.InputStream;
import java.io.OutputStream;


/**
 * This class customizes the generic handler with a concrete content processor,
 * which provides command line editing.
 */
public class ConsoleInputHandler extends InputHandler {
	
    public ConsoleInputHandler(InputStream input, ConsoleInputStream in, OutputStream out) {
        super(input, in, out);
        inputScanner = new ConsoleInputScanner(in, out);
    } 
}
