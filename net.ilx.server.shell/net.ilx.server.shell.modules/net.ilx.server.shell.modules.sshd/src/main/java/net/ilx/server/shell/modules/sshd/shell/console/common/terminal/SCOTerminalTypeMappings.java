package net.ilx.server.shell.modules.sshd.shell.console.common.terminal;

import net.ilx.server.shell.modules.sshd.shell.console.common.KEYS;

public class SCOTerminalTypeMappings extends TerminalTypeMappings {

	public SCOTerminalTypeMappings() {
		super();

		BACKSPACE = -1;
		DEL = 127;
	}

	@Override
	public void setKeypadMappings() {
		escapesToKey.put("[H", KEYS.HOME);
		escapesToKey.put("F", KEYS.END);
		escapesToKey.put("[L", KEYS.INS);
		escapesToKey.put("[I", KEYS.PGUP);
		escapesToKey.put("[G", KEYS.PGDN);
	}
}
