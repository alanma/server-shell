package net.ilx.server.core.utils.wink.security;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

import net.ilx.server.core.utils.wink.UriUtils;

import org.apache.wink.client.ClientRequest;
import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.handlers.ClientHandler;
import org.apache.wink.client.handlers.HandlerContext;

public class SecurityHandler implements ClientHandler {
	public static final String PARAM_CHANNEL = "channel";
	public static final String HEADER_MESSAGE_DATE = "MessageDate";
	public static final String HEADER_AUTHORIZATION = "Authorization";

	private String channel;
	private String keyString;
	private boolean verifyResponseAuthorization;

	public SecurityHandler(final String channel, final String key, final boolean verifyResponseAuthorization) {
		this.channel = channel;
		this.keyString = key;
		this.verifyResponseAuthorization = verifyResponseAuthorization;
	}

	@Override
	public ClientResponse handle(final ClientRequest request,
								final HandlerContext context) throws Exception
	{
		UriBuilder uriBuilder = UriBuilder.fromUri(request.getURI());
		uriBuilder.queryParam(PARAM_CHANNEL, this.channel);
		URI newUri = uriBuilder.build();
		request.setURI(newUri);

		if (verifyResponseAuthorization) {
			context.addInputStreamAdapter(new MacAwareInputStreamAdapter(channel, keyString, request));
		}

		MultivaluedMap<String, String> headers = request.getHeaders();
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
		String timestamp = format.format(new Date());

		URI uri = request.getURI();
		String urlPath = null;
		try {
			urlPath = UriUtils.relativePathToSign(uri);
		} catch (URISyntaxException e) {
			String msg = String.format("Unable to create relative path for uri: '%s'", uri.toString());
			throw new IllegalStateException(msg, e);
		}
		String msg = (String) request.getEntity();

		StringBuilder sb = new StringBuilder()
											.append(request.getMethod())
											.append(urlPath)
											.append(timestamp)
											.append(msg);
		String stringToSign = sb.toString();

		String authorization = buildMac(keyString, stringToSign);

		headers.putSingle(HEADER_MESSAGE_DATE, timestamp);
		headers.putSingle(HEADER_AUTHORIZATION, authorization);

		return context.doChain(request);
	}

	private String buildMac(final String keyString,
							final String... valuesToHash)
	{
		String mac = "";
		try {
			MacBuilder macBuilder = new MacBuilder(keyString);
			for (String value : valuesToHash) {
				macBuilder.append(value);
			}
			mac = macBuilder.build();
		} catch (Throwable e) {
			String msg = "Unable to create HMAC.";
			throw new IllegalStateException(msg, e);
		}

		return mac;
	}


}
