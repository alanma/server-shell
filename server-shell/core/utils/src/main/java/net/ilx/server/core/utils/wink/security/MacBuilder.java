package net.ilx.server.core.utils.wink.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

public class MacBuilder {
	private static final Logger LOG = Logger.getLogger(MacBuilder.class);
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
	private static final boolean CHUNK_BASE64 = false;

	private List<String> dataValues = new ArrayList<String>();
	private Mac mac;
	private boolean isFinal;

	public MacBuilder(final String base64Key) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
		byte[] encodedBytes = base64Key.getBytes("UTF-8");
		byte[] bytes = Base64.decodeBase64(encodedBytes);
		// get an hmac_sha1 key from the raw key bytes
		Key key = new SecretKeySpec(bytes, HMAC_SHA1_ALGORITHM);
		init(key);
	}

	public MacBuilder(final Key key) throws NoSuchAlgorithmException, InvalidKeyException {
		init(key);
	}

	private void init(final Key key) throws NoSuchAlgorithmException, InvalidKeyException {
		mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
		mac.init(key);
		isFinal = false;
	}

	public MacBuilder append(final String value) throws IllegalStateException, UnsupportedEncodingException {
		dataValues.add(value);
		mac.update(value.getBytes("UTF-8"));
		return this;
	}

	public String build() throws UnsupportedEncodingException {
		byte [] hmac = mac.doFinal();

		String encodedMac = new String(Base64.encodeBase64(hmac, CHUNK_BASE64), "UTF-8");
		isFinal = true;
		return encodedMac;
	}

	public boolean verify(final String expectedMac) throws UnsupportedEncodingException {
		if (isFinal) {
			for (String value : dataValues) {
				mac.update(value.getBytes("UTF-8"));
			}
		}
		String calculatedMac = build();
		return calculatedMac.equals(expectedMac);
	}

}
