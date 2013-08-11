package net.ilx.server.core.utils.alf.xml.jaxb.adapters;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import net.ilx.server.shell.core.utils.alf.xml.jaxb.adapters.DateTimeAdapter;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class DateTimeAdapterTest {

	private String format;
	private String textSample;
	private Date javaSample;


	public DateTimeAdapterTest(String format, String textSample, Date javaSample) {
		this.format = format;
		this.textSample = textSample;
		this.javaSample = javaSample;
	}


	private static Date getDate(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day);
		cal = DateUtils.truncate(cal, Calendar.DAY_OF_MONTH);

		return cal.getTime();
	}

	private static Date getDate(int year, int month, int day, int hour, int minute) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month -1, day, hour, minute);
		cal = DateUtils.truncate(cal, Calendar.MINUTE);
		return cal.getTime();
	}

	private static Date getDate(int year, int month, int day, int hour, int minute, int seconds) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month -1, day, hour, minute, seconds);

		cal = DateUtils.truncate(cal, Calendar.SECOND);
		return cal.getTime();
	}

	private static Date getDate(int year, int month, int day, int hour, int minute, int seconds, int milis) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month -1, day, hour, minute, seconds);
		cal.set(Calendar.MILLISECOND, milis);

		cal = DateUtils.truncate(cal, Calendar.MILLISECOND);
		return cal.getTime();
	}

	@Parameters
	public static Collection<Object[]> params() {
		return Arrays.asList(new Object[][] {
			{null, "31.12.2011 13:55:23", getDate(2011, 12, 31, 13, 55, 23)},
			{"dd.MM.yyyy HH:mm:ss", "31.12.2011 13:55:23", getDate(2011, 12, 31, 13, 55, 23)},
			{"dd-MM-yyyy", "31-12-2011",getDate(2011, 12, 31) },
			{"dd-MM-yyyy HH:mm", "31-12-2011 13:55", getDate(2011, 12, 31, 13, 55) },
			{"yyyy-MM-dd HH:mm:ss.SSS", "2011-12-31 13:55:23.528", getDate(2011, 12, 31, 13, 55, 23, 528) }
		});
	}

	@Test
	public void testUnmarshall() throws Exception {
		DateTimeAdapter adapter = new DateTimeAdapter();
		if (null != format) {
			adapter = new DateTimeAdapter(format);
		}
		Date date = adapter.unmarshal(textSample);
		assertThat(date, notNullValue());
		assertThat(date, equalTo(javaSample));
	}

	@Test
	public void testMarshall() throws Exception {
		DateTimeAdapter adapter = new DateTimeAdapter();
		if (null != format) {
			adapter = new DateTimeAdapter(format);
		}

		String text = adapter.marshal(javaSample);
		assertThat(text, notNullValue());
		assertThat(text, equalTo(textSample));
	}



}
