package net.ilx.actor.server.util;

import java.io.Closeable;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.jboss.logging.Logger;

abstract public class StreamUtils {

	private static final Logger LOG = Logger.getLogger(StreamUtils.class);

	public static void close(final Closeable closeable) {
		try {
			closeable.close();
		} catch (IOException e) {
			LOG.error("Unable to close resource.", e);
		}
	}

	public static void close(final XMLStreamReader reader) {
		try {
			reader.close();
		} catch (XMLStreamException e) {
			LOG.error("Unable to close resource.", e);
		}
	}
}
