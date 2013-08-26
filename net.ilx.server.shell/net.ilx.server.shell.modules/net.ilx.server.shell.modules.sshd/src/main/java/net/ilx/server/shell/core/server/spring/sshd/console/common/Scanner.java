	package net.ilx.server.shell.core.server.spring.sshd.console.common;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import net.ilx.server.shell.core.server.spring.sshd.console.common.terminal.ANSITerminalTypeMappings;
import net.ilx.server.shell.core.server.spring.sshd.console.common.terminal.SCOTerminalTypeMappings;
import net.ilx.server.shell.core.server.spring.sshd.console.common.terminal.TerminalTypeMappings;
import net.ilx.server.shell.core.server.spring.sshd.console.common.terminal.VT100TerminalTypeMappings;
import net.ilx.server.shell.core.server.spring.sshd.console.common.terminal.VT220TerminalTypeMappings;
import net.ilx.server.shell.core.server.spring.sshd.console.common.terminal.VT320TerminalTypeMappings;

/**
 * A common superclass for content processor for the telnet protocol and for command line editing (processing delete,
 * backspace, arrows, command history, etc.).
 */
public abstract class Scanner {

	private byte BACKSPACE;
	private byte DEL;

	// ASCII control codes: http://academic.evergreen.edu/projects/biophysics/technotes/program/ascii_ctrl.htm
	protected static final byte CTRL_C = 3;
	protected static final byte CTRL_D = 4;

    protected static final byte BS = 8;
    protected static final byte LF = 10;
    protected static final byte CR = 13;
    protected static final byte ESC = 27;
    protected static final byte SPACE = 32;
    protected static final byte MAX_CHAR = 127;
    protected static final String DEFAULT_TTYPE = File.separatorChar == '/' ? "XTERM" : "ANSI";
    // shows if user input should be echoed to the console
    private boolean isEchoEnabled = true;

    protected OutputStream toTelnet;
    protected ConsoleInputStream toShell;
    protected Map<String, KEYS> currentEscapesToKey;
    protected final Map<String, TerminalTypeMappings> supportedEscapeSequences;
    protected String[] escapes;

    public Scanner(ConsoleInputStream toShell, OutputStream toTelnet) {
        this.toShell = toShell;
        this.toTelnet = toTelnet;
        supportedEscapeSequences = new HashMap<String, TerminalTypeMappings> ();
        supportedEscapeSequences.put("ANSI", new ANSITerminalTypeMappings());
        supportedEscapeSequences.put("VT100", new VT100TerminalTypeMappings());
        VT220TerminalTypeMappings vtMappings = new VT220TerminalTypeMappings();
        supportedEscapeSequences.put("VT220", vtMappings);
        supportedEscapeSequences.put("XTERM", vtMappings);
        // TODO ilx: cygwin on windows - default $TERM is XTERM.... backspace won't work
        supportedEscapeSequences.put("VT320", new VT320TerminalTypeMappings());
        supportedEscapeSequences.put("SCO", new SCOTerminalTypeMappings());
    }

    public abstract void scan(int b) throws IOException;

    public void toggleEchoEnabled(boolean isEnabled) {
    	isEchoEnabled = isEnabled;
    }

    protected void echo(int b) throws IOException {
    	if (isEchoEnabled) {
    		toTelnet.write(b);
    	}
    }

    protected void flush() throws IOException {
        toTelnet.flush();
    }

    protected KEYS checkEscape(String possibleEsc) {
        if (currentEscapesToKey.get(possibleEsc) != null) {
            return currentEscapesToKey.get(possibleEsc);
        }

        for (String escape : escapes) {
            if (escape.startsWith(possibleEsc)) {
                return KEYS.UNFINISHED;
            }
        }
        return KEYS.UNKNOWN;
    }

    protected String esc;
    protected boolean isEsc = false;

    protected void startEsc() {
        isEsc = true;
        esc = "";
    }

    protected abstract void scanEsc(final int b) throws IOException;

	public byte getBackspace() {
		return BACKSPACE;
	}

	public void setBackspace(byte backspace) {
		BACKSPACE = backspace;
	}

	public byte getDel() {
		return DEL;
	}

	public void setDel(byte del) {
		DEL = del;
	}

	public Map<String, KEYS> getCurrentEscapesToKey() {
		return currentEscapesToKey;
	}

	public void setCurrentEscapesToKey(Map<String, KEYS> currentEscapesToKey) {
		this.currentEscapesToKey = currentEscapesToKey;
	}

	public String[] getEscapes() {
        if (escapes != null) {
        	String[] copy = new String[escapes.length];
        	System.arraycopy(escapes, 0, copy, 0, escapes.length);
        	return copy;
        } else {
            return null;
        }
	}

	public void setEscapes(String[] escapes) {
        if (escapes != null) {
            this.escapes = new String[escapes.length];
        	System.arraycopy(escapes, 0, this.escapes, 0, escapes.length);
        } else {
            this.escapes = null;
        }
	}

}
