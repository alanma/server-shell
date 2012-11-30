/*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements. See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership. The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied. See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.apache.karaf.shell.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;

import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.CommandSession;
import org.apache.felix.service.command.Converter;
import org.apache.karaf.shell.util.ShellUtil;
import org.apache.karaf.util.StreamUtils;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.SessionAware;
import org.apache.sshd.server.session.ServerSession;

public class ShellCommand implements Command, SessionAware {

    private String command;
    private InputStream in;
    private OutputStream out;
    private OutputStream err;
    private ExitCallback callback;
    private ServerSession session;
    private CommandProcessor commandProcessor;

    public ShellCommand(final CommandProcessor commandProcessor, final String command) {
        this.commandProcessor = commandProcessor;
        this.command = command;
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
	public void setSession(final ServerSession session) {
        this.session = session;
    }

    @Override
	public void start(final Environment env) throws IOException {
        try {
            final CommandSession session = commandProcessor.createSession(in, new PrintStream(out), new PrintStream(err));
            session.put("SCOPE", "shell:osgi:*");
            session.put("APPLICATION", System.getProperty("karaf.name", "root"));
            for (Map.Entry<String,String> e : env.getEnv().entrySet()) {
                session.put(e.getKey(), e.getValue());
            }
            try {
                Object result = session.execute(command);
                if (result != null)
                {
                    session.getConsole().println(session.format(result, Converter.INSPECT));
                }
            } catch (Throwable t) {
                ShellUtil.logException(session, t);
            }
        } catch (Exception e) {
            throw (IOException) new IOException("Unable to start shell").initCause(e);
        } finally {
            StreamUtils.close(in, out, err);
            callback.onExit(0);
        }
    }

    @Override
	public void destroy() {
}

}