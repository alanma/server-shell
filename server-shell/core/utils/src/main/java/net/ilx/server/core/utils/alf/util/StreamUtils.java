package net.ilx.server.core.utils.alf.util;

import java.io.Closeable;
import java.io.IOException;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;


abstract public class StreamUtils {

	private static final Logger LOG = Logger.getLogger(StreamUtils.class);

	public static void close(final Closeable closeable) {
		try {
		    if (null != closeable) {
		        closeable.close();
		    }
		} catch (IOException e) {
			LOG.error("Unable to close Closeable resource.", e);
		}
	}

	public static void close(final XMLStreamReader reader) {
		try {
		    if (null != reader) {
		        reader.close();
		    }
		} catch (XMLStreamException e) {
			LOG.error("Unable to close XMLStreamReader resource.", e);
		}
	}

	public static void close(XMLEventReader reader) {
		try {
		    if (null != reader) {
		        reader.close();
		    }
		} catch (Throwable e) {
			LOG.error("Unable to close XMLEventReader resource.", e);
		}
	}
}
