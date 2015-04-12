package it.geosolutions.httpproxy;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.util.Arrays;

import javax.net.ssl.SSLContext;

import org.apache.http.Consts;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.BrowserCompatHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.protocol.HttpContext;

public class HTTPHelper {
	HttpMessageParserFactory<HttpResponse> responseParserFactory = new HttpResponseParserFactory();
	HttpMessageWriterFactory<HttpRequest> requestWriterFactory = new DefaultHttpRequestWriterFactory();

	HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(
			requestWriterFactory, responseParserFactory);

	SSLContext sslcontext = SSLContexts.createSystemDefault();
	X509HostnameVerifier hostnameVerifier = new BrowserCompatHostnameVerifier();
	Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
			.register("http", PlainConnectionSocketFactory.INSTANCE)
			.register("https", new SSLConnectionSocketFactory(sslcontext, hostnameVerifier))
			.build();
	DnsResolver dnsResolver = new SystemDefaultDnsResolver() {

		@Override
		public InetAddress[] resolve(final String host) throws UnknownHostException {
			if (host.equalsIgnoreCase("localhost")) {
				return new InetAddress[] { InetAddress.getByAddress(new byte[] {127, 0, 0, 1}) };
			} else {
				return super.resolve(host);
			}
		}

	};
	void get(String url) throws IOException{


		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
				socketFactoryRegistry, connFactory, dnsResolver);

		SocketConfig socketConfig = SocketConfig.custom()
				.setTcpNoDelay(true)
				.build();
		connManager.setDefaultSocketConfig(socketConfig);

		// Create message constraints
		MessageConstraints messageConstraints = MessageConstraints.custom()
				.setMaxHeaderCount(200)
				.setMaxLineLength(2000)
				.build();
		// Create connection configuration
		ConnectionConfig connectionConfig = ConnectionConfig.custom()
				.setMalformedInputAction(CodingErrorAction.IGNORE)
				.setUnmappableInputAction(CodingErrorAction.IGNORE)
				.setCharset(Consts.UTF_8)
				.setMessageConstraints(messageConstraints)
				.build();
		// Configure the connection manager to use connection configuration either
		// by default or for a specific host.
		connManager.setDefaultConnectionConfig(connectionConfig);

		// Configure total max or per route limits for persistent connections
		// that can be kept in the pool or leased by the connection manager.
		connManager.setMaxTotal(100);
		connManager.setDefaultMaxPerRoute(10);
		connManager.setMaxPerRoute(new HttpRoute(new HttpHost("somehost", 80)), 20);

		// Use custom credentials provider if necessary.
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		// Create global request configuration
		RequestConfig defaultRequestConfig = RequestConfig.custom()
				.setCookieSpec(CookieSpecs.BEST_MATCH)
				.setExpectContinueEnabled(true)
				.setStaleConnectionCheckEnabled(true)
				.setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
				.setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
				.build();

		// Create an HttpClient with the given custom dependencies and configuration.
		CloseableHttpClient httpclient = HttpClients.custom()
				.setConnectionManager(connManager)
				.setRedirectStrategy(new RedirectStrategy(){

					@Override
					public HttpUriRequest getRedirect(HttpRequest arg0,
							HttpResponse arg1, HttpContext arg2)
									throws ProtocolException {
						return null;
					}

					@Override
					public boolean isRedirected(HttpRequest arg0,
							HttpResponse arg1, HttpContext arg2)
									throws ProtocolException {
						return false;
					}

				})
				.build();


		HttpGet httpget = new HttpGet(url);
		RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig)
				.setSocketTimeout(5000)
				.setConnectTimeout(5000)
				.setConnectionRequestTimeout(5000)
				.build();
		httpget.setConfig(requestConfig);

		HttpClientContext context = HttpClientContext.create();
		context.setCredentialsProvider(credentialsProvider);

		System.out.println("executing request " + httpget.getURI());
		CloseableHttpResponse response = httpclient.execute(httpget, context);




	}
}
