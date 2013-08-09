package net.ilx.server.core.utils.alf.server.spring.core.env;

import java.io.InputStream;
import java.net.URL;
import java.security.Key;
import java.security.KeyStore;
import java.util.Enumeration;
import java.util.Properties;

import javax.crypto.SecretKey;

import net.ilx.server.core.utils.alf.util.StreamUtils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.ResourceUtils;

/**
 * Wraps JCEKS keystore into Properties object.
 * Exposes aliases as property keys, and keys as property values.
 *
 * @author ilonca
 */
public class JceksPropertiesWrapper extends Properties {
	private static final String JCEKS = "jceks";
	private String keystoreResourceLocation;
	private String keystorePass;

	public JceksPropertiesWrapper(String keystoreResourceLocation, String keystorePass) {
		super();
		this.keystoreResourceLocation = keystoreResourceLocation;
		this.keystorePass = keystorePass;
		loadFromKeystore();
	}

	private void loadFromKeystore() {
		InputStream inputStream = null;
		try {
			KeyStore ks = KeyStore.getInstance(JCEKS);
			URL keystoreUrl = ResourceUtils.getURL(keystoreResourceLocation);
			inputStream = keystoreUrl.openStream();
			ks.load(inputStream, keystorePass.toCharArray());
			Enumeration<String> aliases = ks.aliases();
			while(aliases.hasMoreElements()) {
				String alias = aliases.nextElement();
				Key key = ks.getKey(alias, keystorePass.toCharArray());
				if (null != key && key instanceof SecretKey) {
					SecretKey secretKey = (SecretKey) key;

					byte[] encodedData = secretKey.getEncoded();
					String rawKeyValue = new String(encodedData, "latin1");

					this.put(alias, rawKeyValue);

				}
			}
		} catch (Throwable t) {
			String msg = "Unable to load key from keystore";
			throw new IllegalStateException(msg, t);
		} finally {
			StreamUtils.close(inputStream);
		}
	}

}
