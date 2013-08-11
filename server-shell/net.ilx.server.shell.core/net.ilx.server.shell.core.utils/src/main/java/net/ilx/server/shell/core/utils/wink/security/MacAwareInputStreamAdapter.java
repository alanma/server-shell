package net.ilx.server.shell.core.utils.wink.security;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import javax.ws.rs.core.MultivaluedMap;

import net.ilx.server.shell.core.utils.wink.UriUtils;

import org.apache.wink.client.ClientRequest;
import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.handlers.InputStreamAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class MacAwareInputStreamAdapter implements InputStreamAdapter {
	private static final Logger LOG = LoggerFactory.getLogger(MacAwareInputStreamAdapter.class);

	private String channel;
	private String keyString;

	private ClientRequest request;

	public MacAwareInputStreamAdapter(final String channel, final String keystring, ClientRequest request) {
		this.channel = channel;
		this.keyString = keystring;
		this.request = request;
	}

	@Override
	public InputStream adapt(final InputStream is,
							final ClientResponse response) throws IOException
	{
		verifyResponse(response);
		return is;
	}


	private void verifyResponse(final ClientResponse clientResponse)
	{
		MultivaluedMap<String, String> headers = clientResponse.getHeaders();
		String messageDate = headers.getFirst(SecurityHandler.HEADER_MESSAGE_DATE);
		String authorization = headers.getFirst(SecurityHandler.HEADER_AUTHORIZATION);
		String msg = clientResponse.getMessage();

		try {
			URI uri = request.getURI();
			String urlPath = UriUtils.relativePathToSign(uri);

			StringBuilder sb = new StringBuilder()
			.append(request.getMethod())
			.append(urlPath)
			.append(messageDate)
			.append(msg);
			String stringToSign = sb.toString();

			MacBuilder builder = new MacBuilder(keyString);
			builder.append(stringToSign);

			if (!builder.verify(authorization)) {
				throw new IllegalStateException("Responses HMac value is invalid.");
			}
		} catch (Throwable e) {
			throw new IllegalStateException("Responses HMac value is invalid.", e);
		}
	}
}