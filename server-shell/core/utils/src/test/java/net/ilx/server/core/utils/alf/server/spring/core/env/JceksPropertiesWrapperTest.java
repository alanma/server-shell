package net.ilx.server.core.utils.alf.server.spring.core.env;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Properties;

import net.ilx.server.core.utils.alf.server.spring.core.env.JceksPropertiesWrapper;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JceksPropertiesWrapperTest {
	private static final Logger LOG = LoggerFactory.getLogger(JceksPropertiesWrapperTest.class);

	@Test
	public void testWrapper() {
		String vendor = System.getProperty("java.vendor");
		if (vendor.startsWith("IBM")) {
			String location = "classpath:alf_ibm.jck";
			String pass = "password";
			Properties props = new JceksPropertiesWrapper(location, pass);

			assertThat(props.size(), equalTo(1));

			Object value = props.get("password.datasource.ds1.password");
			assertThat(value, notNullValue());
		}
		else {
			LOG.warn("No test for non IBM JVM written.");
		}

	}
}
