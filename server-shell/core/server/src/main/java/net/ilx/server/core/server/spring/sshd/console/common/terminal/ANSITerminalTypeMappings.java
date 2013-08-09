package net.ilx.server.core.server.spring.sshd.console.common.terminal;

import net.ilx.server.core.server.spring.sshd.console.common.KEYS;

public class ANSITerminalTypeMappings extends TerminalTypeMappings {

	public ANSITerminalTypeMappings() {
		super();
        BACKSPACE = 8;
        DEL = 127;
	}

	public void setKeypadMappings() {
		// ilx: see http://www.isthe.com/chongo/tech/comp/ansi_escapes.html
		// http://www.termsys.demon.co.uk/vtansi.htm
		// https://github.com/fusesource/jansi/blob/master/jansi/src/test/java/org/fusesource/jansi/AnsiConsoleExample2.java
		escapesToKey.put("[1~", KEYS.HOME); //$NON-NLS-1$
        escapesToKey.put("[4~", KEYS.END); //$NON-NLS-1$
        escapesToKey.put("[5~", KEYS.PGUP); //$NON-NLS-1$
        escapesToKey.put("[6~", KEYS.PGDN); //$NON-NLS-1$
        escapesToKey.put("[2~", KEYS.INS); //$NON-NLS-1$
        escapesToKey.put("[3~", KEYS.DEL); //$NON-NLS-1$
        escapesToKey.put("[2J", KEYS.CLEAR_SCREEN); //$NON-NLS-1$
        escapesToKey.put("[H", KEYS.HOME); //$NON-NLS-1$

	}
}
