package net.ilx.server.core.utils.alf.util;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import net.ilx.server.shell.core.utils.alf.util.XORService;

import org.apache.log4j.Logger;
import org.junit.Test;


public class XORServiceTest {

	private static final Logger LOG = Logger.getLogger(XORServiceTest.class);
	@Test
	public void testEncodeDecode() {
		String data = "%5thyjUK8*";
		String key  = "Sjo9QM3UUi50Mm";
		String encoded = XORService.xorEncode(data, key);
		LOG.info(String.format("Encoded value of '%s' with key '%s' is: '%s'", data, key, encoded));
		assertThat(encoded, notNullValue());
		assertThat(encoded, not(equalTo(data)));

		String decoded = XORService.xorDecode(encoded, key);
		assertThat(decoded, notNullValue());
		assertThat(decoded, equalTo(data));
		LOG.info(String.format("Decoded value of '%s' with key '%s' is: '%s'", encoded, key, decoded));
	}



}
