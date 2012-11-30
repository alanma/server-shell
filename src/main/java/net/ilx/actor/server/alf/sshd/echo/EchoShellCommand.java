package net.ilx.actor.server.alf.sshd.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.sshd.server.Command;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;

public class EchoShellCommand implements Command, Runnable {
    final String lineSeparator = System.getProperty("line.separator");

    private InputStream in;
    private OutputStream out;
    private OutputStream err;
    private ExitCallback callback;
    private Environment environment;
    private Thread thread;

    public InputStream getIn() {
        return in;
    }

    public OutputStream getOut() {
        return out;
    }

    public OutputStream getErr() {
        return err;
    }

    public Environment getEnvironment() {
        return environment;
    }

    @Override
	public void setInputStream(final InputStream in) {
        this.in = in;
    }

    @Override
	public void setOutputStream(final OutputStream out) {
        this.out = out;
    }

    @Override
	public void setErrorStream(final OutputStream err) {
        this.err = err;
    }

    @Override
	public void setExitCallback(final ExitCallback callback) {
        this.callback = callback;
    }

    @Override
	public void start(final Environment env) throws IOException {
        environment = env;
        thread = new Thread(this, "EchoShell");
        thread.start();
    }

    @Override
	public void destroy() {
        thread.interrupt();
    }

    @Override
	public void run() {
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        try {
            for (;;) {
                String s = r.readLine();
                if (s == null) {
                    return;
                }

                out.write((s + lineSeparator).getBytes());
                out.flush();
                if ("exit".equals(s)) {
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            callback.onExit(0);
        }
    }
}