package net.ilx.server.core.utils.alf.xml.jaxb.adapters;

import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

public class LenientDateTimeAdapter extends XmlAdapter<String, Date> {

	private static final Logger LOG = Logger.getLogger(LenientDateTimeAdapter.class);
    private String[] formats;
    private String marshallingFormat;

	public LenientDateTimeAdapter() {
    	this("dd.MM.yyyy HH:mm:ss",
    		 "dd.MM.yyyy HH:mm:ss", "dd.MM.yyyy HH:mm:ss,SSS", "dd.MM.yyyy HH:mm:ss,SSSSSS",
    		 "dd-MM-yyyy HH:mm:ss", "dd-MM-yyyy HH:mm:ss,SSSSSS", "dd-MM-yyyy HH:mm:ss,SSS",
    		 "yyyy-MM-dd'T'HH:mm:ss.SSSSS", "yyyy-MM-dd'T'HH:mm:ss.SSS", //2013-04-15T10:01:55.256439
    		 "dd.MM.yyyy", "dd.MM.yyyy.",
    		 "yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd HH:mm:ss:SSS"
		);
    }

    public LenientDateTimeAdapter(String marshallingFormat, String... simpleDateFormats) {
    	Assert.notEmpty(simpleDateFormats);
    	this.formats = simpleDateFormats;
    	this.marshallingFormat = marshallingFormat;
    }

    @Override
    public String marshal(Date v) throws Exception {
    	String formattedDate = null;
    	try {
			formattedDate = DateFormatUtils.format(v, marshallingFormat);
    	}
    	catch (Throwable t) {

    		String msg = String.format("Unable to marshall object '%s' using pattern '%s'", v, marshallingFormat);
    		LOG.error(msg, t);
    		throw new IllegalStateException(msg, t);
    	}

    	return formattedDate;
    }

    private String join(String[] elements, String postfix) {
    	StringBuilder sb = new StringBuilder();
    	for (String element : elements) {
    		sb.append(element);
    		sb.append(postfix);
		}
    	return sb.toString();
    }

    @Override
    public Date unmarshal(String v) throws Exception {
    	Date parseDated = null;
    	try {
			parseDated = DateUtils.parseDate(v, formats);
    	}
    	catch (Throwable t) {
    		String msg = String.format("Unable to unmarshall value '%s' using patterns '%s'", v, join(formats, "; "));
    		LOG.error(msg, t);
    		throw new IllegalStateException(msg, t);
    	}
        return parseDated;
    }
}
