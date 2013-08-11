package net.ilx.server.shell.core.utils.wink;


import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import net.ilx.server.shell.core.utils.wink.security.SecurityHandler;

import org.apache.http.client.HttpClient;
import org.apache.wink.client.ClientConfig;
import org.apache.wink.client.ClientRequest;
import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.RestClient;
import org.apache.wink.client.handlers.ClientHandler;
import org.apache.wink.client.handlers.HandlerContext;
import org.apache.wink.client.httpclient.ApacheHttpClientConfig;
import org.apache.wink.common.internal.providers.entity.StringProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientFactory {

	private SecurityHandler securityHandler;
	private HttpClient httpClient;

	public ClientFactory(final SecurityHandler securityHandler, HttpClient httpClient) {
		this.securityHandler = securityHandler;
		this.httpClient = httpClient;
	}

	public RestClient createClient() {
		return createClient(false);
	}

	public RestClient createClient(final boolean debug) {
		ClientConfig clientConfig = null;
		if (null != httpClient) {
			ApacheHttpClientConfig apacheClientConfig = new ApacheHttpClientConfig(httpClient);
			apacheClientConfig.setMaxPooledConnections(1);
			clientConfig = apacheClientConfig;
		} else {
			clientConfig = new ClientConfig();

		}


		clientConfig.connectTimeout(30000);
		clientConfig.readTimeout(30000);

		Application app = new Application() {
		@Override
			public Set<Class<?>> getClasses() {
				return super.getClasses();
			}

		@Override
			public Set<Object> getSingletons() {
				return new HashSet<Object>();
			}
		};


		clientConfig.handlers(securityHandler);
		if (debug) {
			ClientHandler loggingHandler = new LoggingHandler();
			clientConfig.handlers(loggingHandler);
		}

		// DO NOT USE JaxbElementXmlProvider since we will not be able to calculate MAC consistently
		// app.getSingletons().add(new JAXBElementXmlProvider());
		app.getSingletons().add(new StringProvider());
		clientConfig.applications(app);



		RestClient client = new RestClient(clientConfig);

		return client;
	}

	static class LoggingHandler implements ClientHandler {
		private static Logger LOG = LoggerFactory.getLogger(LoggingHandler.class);

		@Override
		public ClientResponse handle(final ClientRequest request,
									final HandlerContext context) throws Exception
		{
			Object entity = request.getEntity();
			if (entity instanceof String) {
				LOG.debug("sending message:" + entity);
			}
//			context.addOutputStreamAdapter(new LoggingAdapter());
//			context.addInputStreamAdapter(new LoggingAdapter());
			return context.doChain(request);
		}
	}

}
