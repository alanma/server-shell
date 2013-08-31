package net.ilx.server.shell.modules.sshd.shell.console.common.terminal;

/**
 * For the supported escape sequences, the VT220 and XTERM sequences
 * are one and the same.
 *
 */
public class VT220TerminalTypeMappings extends ANSITerminalTypeMappings {
	
	public VT220TerminalTypeMappings() {
		super();
		
		BACKSPACE = 127;
		DEL = -1;
	}
}
