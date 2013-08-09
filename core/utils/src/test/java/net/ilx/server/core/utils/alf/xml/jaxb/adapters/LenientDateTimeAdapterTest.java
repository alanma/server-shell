package net.ilx.server.core.utils.alf.xml.jaxb.adapters;

import static junitparams.JUnitParamsRunner.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Date;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import net.ilx.server.core.utils.alf.xml.jaxb.adapters.LenientDateTimeAdapter;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class LenientDateTimeAdapterTest {

	@Test
	@Parameters(method="unmarshallData")
	public void testUnmarshallWithDefaultFormats(String format, String value) throws Exception {
			LenientDateTimeAdapter adapter = new LenientDateTimeAdapter(format, format);

			Date unmarshalled = adapter.unmarshal(value);
			assertThat(unmarshalled, notNullValue());

			String marshalled = adapter.marshal(unmarshalled);
			assertThat(marshalled, equalTo(value));
	}

	private Object[] unmarshallData() {
		return $(
				$("dd.MM.yyyy HH:mm:ss", "25.12.2013 11:22:33"),
				$("dd.MM.yyyy HH:mm:ss,SSS", "25.12.2013 11:22:33,789"),
//				$("dd.MM.yyyy HH:mm:ss,SSSSSS", "25.12.2013 11:22:33,12345"),
				$("dd-MM-yyyy HH:mm:ss", "25-12-2013 11:22:33"),
//				$("dd-MM-yyyy HH:mm:ss,SSSSSS", "25-12-2013 11:22:33,12345"),
				$("dd-MM-yyyy HH:mm:ss,SSS", "25-12-2013 11:22:33,789")
//				$("yyyy-MM-dd'T'HH:mm:ss.SSSSS", "2013-04-15T10:01:55.256439"),
//				$("yyyy-MM-dd'T'HH:mm:ss.SSS", "2013-04-15T10:01:55.256439")
				);
	}
}
