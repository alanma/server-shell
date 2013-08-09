/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package net.ilx.server.core.server.spring.sshd.runtime;

import java.util.List;

import net.ilx.server.core.server.spring.sshd.command.CommandSession;
import net.ilx.server.core.server.spring.sshd.command.Function;

public class CommandProxy implements Function
{
    private String function;
    private Object target;

    public CommandProxy(String function)
    {
        this.function = function;
    }

    public CommandProxy(Object target, String function)
    {
        this.function = function;
        this.target = target;
    }

    public Object execute(CommandSession session, List<Object> arguments)
        throws Exception
    {
    	// TODO ilx: match spring service and bind it....
        Function tgt = new Function() {
			@Override
			public Object execute(CommandSession session, List<Object> arguments) throws Exception {
				return "nothing";
			}
		};

		return tgt.execute(session, arguments);
    }
}
