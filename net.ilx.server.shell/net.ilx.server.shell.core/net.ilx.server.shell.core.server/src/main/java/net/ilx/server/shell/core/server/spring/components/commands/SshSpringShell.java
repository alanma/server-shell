package net.ilx.server.shell.core.server.spring.components.commands;

import java.io.File;
import java.util.logging.Level;

import org.springframework.shell.core.ExitShellRequest;
import org.springframework.shell.core.Shell;
import org.springframework.shell.event.ShellStatus;
import org.springframework.shell.event.ShellStatusListener;

public class SshSpringShell implements Shell {

	@Override
	public void addShellStatusListener(ShellStatusListener shellStatusListener) {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeShellStatusListener(ShellStatusListener shellStatusListener) {
		// TODO Auto-generated method stub
	}

	@Override
	public ShellStatus getShellStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getShellPrompt() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void promptLoop() {
		// TODO Auto-generated method stub
	}

	@Override
	public ExitShellRequest getExitShellRequest() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean executeCommand(String line) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setDevelopmentMode(boolean developmentMode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void flash(Level level, String message, String slot) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDevelopmentMode() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setPromptPath(String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPromptPath(String path, boolean overrideStyle) {
		// TODO Auto-generated method stub

	}

	@Override
	public File getHome() {
		String home = System.getProperty("user.dir");
		if (null == home) {
			home = System.getProperty("user.home");
		}
		if (null == home) {
			home = System.getProperty("java.home");
		}
		if (null == home) {
			throw new IllegalStateException("Unable to determine shell home directory.");
		}
		return new File(home);
	}

}
