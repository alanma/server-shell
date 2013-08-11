/*
 * Copyright 2002-2008 Martin Ahrer.
 *
 * $Id$
 */
package net.ilx.server.shell.modules.jolokia.beans;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import net.ilx.server.shell.core.utils.alf.util.IterableEnumeration;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(objectName = "log4j:name=Logging", description = "Logging", log = true, logFile = "jmx.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "logging", persistName = "logging")
public class Logging {
	@ManagedOperation(description = "Set the logging level for a category")
	@ManagedOperationParameters( { @ManagedOperationParameter(name = "category", description = "Logger category"),
			@ManagedOperationParameter(name = "level", description = "Logging level") })
	public void setLoggerLevel(String category, String level) {
		LogManager.getLogger(category).setLevel(Level.toLevel(level));
	}

	@ManagedOperation(description = "Get the logging level for a category")
	@ManagedOperationParameters( { @ManagedOperationParameter(name = "category", description = "Logger category") })
	public String getLoggerLevel(String category) {
		return LogManager.getLogger(category).getLevel().toString();
	}

	@ManagedOperation(description = "Get currently defined loggers")
	public String[] getLoggers() {
		Enumeration currentLoggersEnum = LogManager.getLoggerRepository().getCurrentLoggers();
		IterableEnumeration<Logger> iterableEnumeration = new IterableEnumeration<Logger>(currentLoggersEnum);

		ArrayList<String> loggers = new ArrayList<String>();
		for (Logger logger : iterableEnumeration) {
			loggers.add(logger.getName());
		}
		Collections.sort(loggers);
		return loggers.toArray(new String[0]);
	}
}