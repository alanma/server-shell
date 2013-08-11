package net.ilx.server.shell.core.server.spring.sshd.console.common.terminal;

import net.ilx.server.shell.core.server.spring.sshd.console.common.KEYS;

public class VT320TerminalTypeMappings extends TerminalTypeMappings {

	public VT320TerminalTypeMappings() {
		super();
        BACKSPACE = 8;
        DEL = 127;
	}

	@Override
	public void setKeypadMappings() {
		escapesToKey.put("[H", KEYS.HOME);
		escapesToKey.put("[F", KEYS.END);
        escapesToKey.put("[5~", KEYS.PGUP); //$NON-NLS-1$
        escapesToKey.put("[6~", KEYS.PGDN); //$NON-NLS-1$
        escapesToKey.put("[2~", KEYS.INS); //$NON-NLS-1$
        escapesToKey.put("[3~", KEYS.DEL); //$NON-NLS-1$

	}
}
