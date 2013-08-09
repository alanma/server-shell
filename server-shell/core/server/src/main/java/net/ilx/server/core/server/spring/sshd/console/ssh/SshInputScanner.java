package net.ilx.server.core.server.spring.sshd.console.ssh;

import java.io.IOException;
import java.io.OutputStream;

import net.ilx.server.core.server.spring.sshd.console.common.ConsoleInputStream;
import net.ilx.server.core.server.spring.sshd.console.common.KEYS;
import net.ilx.server.core.server.spring.sshd.console.common.Scanner;
import net.ilx.server.core.server.spring.sshd.console.common.terminal.TerminalTypeMappings;

/**
 * This class performs preprocessing of the input from the ssh server in order to echo the visible
 * characters back to the console.
 *
 */
public class SshInputScanner extends Scanner {

	public SshInputScanner(ConsoleInputStream toShell, OutputStream toTelnet) {
		super(toShell, toTelnet);
        TerminalTypeMappings currentMapping = supportedEscapeSequences.get(DEFAULT_TTYPE);
    	currentEscapesToKey = currentMapping.getEscapesToKey();
    	escapes = currentMapping.getEscapes();
    	setBackspace(currentMapping.getBackspace());
    	setDel(currentMapping.getDel());
	}

	@Override
	public void scan(int b) throws IOException {
		b &= 0xFF;

		if (isEsc) {
            scanEsc(b);
        } else {
        	switch (b) {
        	case ESC:
        		startEsc();
        		toShell.add(new byte[]{(byte) b});
        		break;
        	default:
        		if (b >= SPACE && b < MAX_CHAR) {
        			echo((byte) b);
        			flush();
        		}
        		toShell.add(new byte[]{(byte) b});
        	}
        }
	}

	@Override
	protected void scanEsc(int b) throws IOException {
		esc += (char) b;
        toShell.add(new byte[]{(byte) b});
        KEYS key = checkEscape(esc);
        if (key == KEYS.UNFINISHED) {
            return;
        }
        if (key == KEYS.UNKNOWN) {
            isEsc = false;
            scan(b);
            return;
        }
        isEsc = false;
	}

}
