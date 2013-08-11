package net.ilx.server.shell.core.utils.alf.xml.jaxb.adapters;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;

public class DateTimeAdapter extends XmlAdapter<String, Date> {

	private static final Logger LOG = Logger.getLogger(DateTimeAdapter.class);
    private String format;

	public DateTimeAdapter() {
    	this("dd.MM.yyyy HH:mm:ss");
    }

    public DateTimeAdapter(String simpleDateFormat) {
    	this.format = simpleDateFormat;
    }

    @Override
    public String marshal(Date v) throws Exception {
    	String formattedDate = null;
    	try {
			formattedDate = DateFormatUtils.format(v, format);
    	}
    	catch (Throwable t) {
    		String msg = String.format("Unable to marshall object '%s' using pattern '%s'", v, format);
    		LOG.error(msg, t);
    		throw new IllegalStateException(msg, t);
    	}

    	return formattedDate;
    }

    @Override
    public Date unmarshal(String v) throws Exception {
    	Date parseDated = null;
    	try {
			parseDated = DateUtils.parseDate(v, new String[] {format});
    	}
    	catch (Throwable t) {
    		String msg = String.format("Unable to unmarshall value '%s' using pattern '%s'", v, format);
    		LOG.error(msg, t);
    		throw new IllegalStateException(msg, t);
    	}
        return parseDated;
    }
}