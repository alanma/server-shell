package net.ilx.server.core.utils.wink;

import java.net.URI;
import java.net.URISyntaxException;

public abstract class UriUtils {

	public static String relativePathToSign(final URI uri) throws URISyntaxException {
		String scheme = uri.getScheme();
		String host = uri.getHost();
		int port = uri.getPort();

		URI parentUri = new URI(scheme, null, host, port, null, null, null);
		URI relativeUri = parentUri.relativize(uri);


		return "/" + relativeUri.toString();
	}
}
