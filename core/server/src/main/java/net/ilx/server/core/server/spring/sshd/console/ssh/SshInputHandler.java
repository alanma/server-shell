package net.ilx.server.core.server.spring.sshd.console.ssh;

import java.io.InputStream;

import net.ilx.server.core.server.spring.sshd.console.common.ConsoleInputStream;
import net.ilx.server.core.server.spring.sshd.console.common.ConsoleOutputStream;
import net.ilx.server.core.server.spring.sshd.console.common.InputHandler;

/**
 * This class customizes the generic handler with a concrete content processor,
 * which provides ssh protocol handling.
 *
 */
public class SshInputHandler extends InputHandler {
	public SshInputHandler(InputStream input, ConsoleInputStream in, ConsoleOutputStream out) {
        super(input, in, out);
        inputScanner = new SshInputScanner(in, out);
    }
}
