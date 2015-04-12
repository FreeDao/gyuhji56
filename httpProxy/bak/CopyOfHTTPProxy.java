package it.geosolutions.httpproxy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.io.DefaultHttpResponseParserFactory;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.message.BasicHeader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.util.Arrays;

import javax.net.ssl.SSLContext;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
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
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.DefaultHttpResponseParser;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.LineParser;
import org.apache.http.util.CharArrayBuffer;

public class CopyOfHTTPProxy {
	private final static Logger LOGGER = Logger.getLogger(CopyOfHTTPProxy.class.toString());
	private int maxFileUploadSize = Utils.DEFAULT_MAX_FILE_UPLOAD_SIZE;

	private HttpClient httpClient;

	private ProxyConfig proxyConfig;

	private List<ProxyCallback> callbacks;
	public CopyOfHTTPProxy(ProxyConfig proxyConfig) throws ServletException {
		this.proxyConfig = proxyConfig;

		callbacks = new ArrayList<ProxyCallback>();
		callbacks.add(new MimeTypeChecker(proxyConfig));
		callbacks.add(new HostNameChecker(proxyConfig));
		callbacks.add(new MethodsChecker(proxyConfig));
		callbacks.add(new HostChecker(proxyConfig));
	}


	void onInit(HttpServletRequest request, HttpServletResponse response, URL url)
			throws IOException {
		for (ProxyCallback callback : callbacks) {
			callback.onRequest(request, response, url);
		}
	}
//---------------------------
	 public final static void main(String[] args) throws Exception {

	        // Use custom message parser / writer to customize the way HTTP
	        // messages are parsed from and written out to the data stream.
	        HttpMessageParserFactory<HttpResponse> responseParserFactory = new DefaultHttpResponseParserFactory() {

	            @Override
	            public HttpMessageParser<HttpResponse> create(
	                SessionInputBuffer buffer, MessageConstraints constraints) {
	                LineParser lineParser = new BasicLineParser() {

	                    @Override
	                    public Header parseHeader(final CharArrayBuffer buffer) {
	                        try {
	                            return super.parseHeader(buffer);
	                        } catch (ParseException ex) {
	                            return new BasicHeader(buffer.toString(), null);
	                        }
	                    }

	                };
	                return new DefaultHttpResponseParser(
	                    buffer, lineParser, DefaultHttpResponseFactory.INSTANCE, constraints) {

	                    @Override
	                    protected boolean reject(final CharArrayBuffer line, int count) {
	                        // try to ignore all garbage preceding a status line infinitely
	                        return false;
	                    }

	                };
	            }

	        };
	        HttpMessageWriterFactory<HttpRequest> requestWriterFactory = new DefaultHttpRequestWriterFactory();

	        // Use a custom connection factory to customize the process of
	        // initialization of outgoing HTTP connections. Beside standard connection
	        // configuration parameters HTTP connection factory can define message
	        // parser / writer routines to be employed by individual connections.
	        HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(
	                requestWriterFactory, responseParserFactory);

	        // Client HTTP connection objects when fully initialized can be bound to
	        // an arbitrary network socket. The process of network socket initialization,
	        // its connection to a remote address and binding to a local one is controlled
	        // by a connection socket factory.

	        // SSL context for secure connections can be created either based on
	        // system or application specific properties.
	        SSLContext sslcontext = SSLContexts.createSystemDefault();
	        // Use custom hostname verifier to customize SSL hostname verification.
	        X509HostnameVerifier hostnameVerifier = new BrowserCompatHostnameVerifier();

	        // Create a registry of custom connection socket factories for supported
	        // protocol schemes.
	        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
	            .register("http", PlainConnectionSocketFactory.INSTANCE)
	            .register("https", new SSLConnectionSocketFactory(sslcontext, hostnameVerifier))
	            .build();

	        // Use custom DNS resolver to override the system DNS resolution.
	        DnsResolver dnsResolver = new SystemDefaultDnsResolver() {

	            @Override
	            public InetAddress[] resolve(final String host) throws UnknownHostException {
	                if (host.equalsIgnoreCase("myhost")) {
	                    return new InetAddress[] { InetAddress.getByAddress(new byte[] {127, 0, 0, 1}) };
	                } else {
	                    return super.resolve(host);
	                }
	            }

	        };

	        // Create a connection manager with custom configuration.
	        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
	                socketFactoryRegistry, connFactory, dnsResolver);

	        // Create socket configuration
	        SocketConfig socketConfig = SocketConfig.custom()
	            .setTcpNoDelay(true)
	            .build();
	        // Configure the connection manager to use socket configuration either
	        // by default or for a specific host.
	        connManager.setDefaultSocketConfig(socketConfig);
	        connManager.setSocketConfig(new HttpHost("somehost", 80), socketConfig);

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
	        connManager.setConnectionConfig(new HttpHost("somehost", 80), ConnectionConfig.DEFAULT);

	        // Configure total max or per route limits for persistent connections
	        // that can be kept in the pool or leased by the connection manager.
	        connManager.setMaxTotal(100);
	        connManager.setDefaultMaxPerRoute(10);
	        connManager.setMaxPerRoute(new HttpRoute(new HttpHost("somehost", 80)), 20);

	        // Use custom cookie store if necessary.
	        CookieStore cookieStore = new BasicCookieStore();
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
	            .setDefaultCookieStore(cookieStore)
	            .setDefaultCredentialsProvider(credentialsProvider)
	            .setProxy(new HttpHost("myproxy", 8080))
	            .setDefaultRequestConfig(defaultRequestConfig)
	            .build();

	        try {
	            HttpGet httpget = new HttpGet("http://www.apache.org/");
	            // Request configuration can be overridden at the request level.
	            // They will take precedence over the one set at the client level.
	            RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig)
	                .setSocketTimeout(5000)
	                .setConnectTimeout(5000)
	                .setConnectionRequestTimeout(5000)
	                .setProxy(new HttpHost("myotherproxy", 8080))
	                .build();
	            httpget.setConfig(requestConfig);

	            // Execution context can be customized locally.
	            HttpClientContext context = HttpClientContext.create();
	            // Contextual attributes set the local context level will take
	            // precedence over those set at the client level.
	            context.setCookieStore(cookieStore);
	            context.setCredentialsProvider(credentialsProvider);

	            System.out.println("executing request " + httpget.getURI());
	            CloseableHttpResponse response = httpclient.execute(httpget, context);
	            try {
	                HttpEntity entity = response.getEntity();

	                System.out.println("----------------------------------------");
	                System.out.println(response.getStatusLine());
	                if (entity != null) {
	                    System.out.println("Response content length: " + entity.getContentLength());
	                }
	                System.out.println("----------------------------------------");

	                // Once the request has been executed the local context can
	                // be used to examine updated state and various objects affected
	                // by the request execution.

	                // Last executed request
	                context.getRequest();
	                // Execution route
	                context.getHttpRoute();
	                // Target auth state
	                context.getTargetAuthState();
	                // Proxy auth state
	                context.getTargetAuthState();
	                // Cookie origin
	                context.getCookieOrigin();
	                // Cookie spec used
	                context.getCookieSpec();
	                // User security token
	                context.getUserToken();

	            } finally {
	                response.close();
	            }
	        } finally {
	            httpclient.close();
	        }
	    }
	//---------------------------
	/**
	 * @param method
	 * @throws IOException
	 */
	void onRemoteResponse(HttpMethod method) throws IOException {
		for (ProxyCallback callback : callbacks) {
			callback.onRemoteResponse(method);
		}
	}

	/**
	 * @throws IOException
	 */
	void onFinish() throws IOException {
		for (ProxyCallback callback : callbacks) {
			callback.onFinish();
		}
	}

	/**
	 * Performs an HTTP GET request
	 * 
	 * @param httpServletRequest The {@link HttpServletRequest} object passed in by the servlet engine representing the client request to be proxied
	 * @param httpServletResponse The {@link HttpServletResponse} object by which we can send a proxied response to the client
	 * @throws URISyntaxException 
	 */
	public void doGet(URL url, String paraStr, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws IOException, ServletException, URISyntaxException {

		try {

			onInit(httpServletRequest, httpServletResponse, url);

			HttpGet getMethodProxyRequest = new HttpGet(url.toExternalForm() + "?" + paraStr);
//			getMethodProxyRequest.setQueryString(paraStr);
			
			final ProxyInfo proxyInfo = setProxyRequestHeaders(url, httpServletRequest,
					getMethodProxyRequest);

			this.executeProxyRequest(getMethodProxyRequest, httpServletRequest,
					httpServletResponse, proxyInfo);

		} catch (HttpErrorException ex) {
			httpServletResponse.sendError(ex.getCode(), ex.getMessage());
		} finally {
			onFinish();
		}
	}

	/**
	 * Performs an HTTP POST request
	 * 
	 * @param httpServletRequest The {@link HttpServletRequest} object passed in by the servlet engine representing the client request to be proxied
	 * @param httpServletResponse The {@link HttpServletResponse} object by which we can send a proxied response to the client
	 */
	public void doPost(URL url, String paraStr, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException, ServletException {
		try {

			if (url != null) {

				onInit(httpServletRequest, httpServletResponse, url);
				HttpPost postMethodProxyRequest = new HttpPost(url.toExternalForm() + "?" + paraStr);
//				postMethodProxyRequest.setQueryString(paraStr);
				final ProxyInfo proxyInfo = setProxyRequestHeaders(url, httpServletRequest,
						postMethodProxyRequest);

				if (ServletFileUpload.isMultipartContent(httpServletRequest)) {
					this.handleMultipart(postMethodProxyRequest, httpServletRequest);
				} else {
					this.handleStandard(postMethodProxyRequest, httpServletRequest);
				}
				this.executeProxyRequest(postMethodProxyRequest, httpServletRequest,
						httpServletResponse, proxyInfo);

			}

		} catch (HttpErrorException ex) {
			httpServletResponse.sendError(ex.getCode(), ex.getMessage());
		} finally {
			onFinish();
		}
	}



	/**
	 * Sets up the given {@link PostMethod} to send the same multipart POST data as was sent in the given {@link HttpServletRequest}
	 * 
	 * @param postMethodProxyRequest The {@link PostMethod} that we are configuring to send a multipart POST request
	 * @param httpServletRequest The {@link HttpServletRequest} that contains the mutlipart POST data to be sent via the {@link PostMethod}
	 */
	private void handleMultipart(EntityEnclosingMethod methodProxyRequest,
			HttpServletRequest httpServletRequest) throws ServletException {

		DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();

		diskFileItemFactory.setSizeThreshold(this.getMaxFileUploadSize());
		diskFileItemFactory.setRepository(Utils.DEFAULT_FILE_UPLOAD_TEMP_DIRECTORY);

		ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);

		try {

			List<FileItem> listFileItems = (List<FileItem>) servletFileUpload
					.parseRequest(httpServletRequest);


			List<Part> listParts = new ArrayList<Part>();

			for (FileItem fileItemCurrent : listFileItems) {

				if (fileItemCurrent.isFormField()) {
					StringPart stringPart = new StringPart(
							// The field name
							fileItemCurrent.getFieldName(),
							// The field value
							fileItemCurrent.getString());

					listParts.add(stringPart);

				} else {

					FilePart filePart = new FilePart(

							fileItemCurrent.getFieldName(),

							new ByteArrayPartSource(
									// The uploaded file name
									fileItemCurrent.getName(),
									// The uploaded file contents
									fileItemCurrent.get()));

					listParts.add(filePart);
				}
			}

			MultipartRequestEntity multipartRequestEntity = new MultipartRequestEntity(
					listParts.toArray(new Part[] {}), methodProxyRequest.getParams());

			methodProxyRequest.setRequestEntity(multipartRequestEntity);

			methodProxyRequest.setRequestHeader(Utils.CONTENT_TYPE_HEADER_NAME,
					multipartRequestEntity.getContentType());

		} catch (FileUploadException fileUploadException) {
			throw new ServletException(fileUploadException);
		}
	}

	/**
	 * Sets up the given {@link PostMethod} to send the same standard POST data as was sent in the given {@link HttpServletRequest}
	 * 
	 * @param postMethodProxyRequest The {@link PostMethod} that we are configuring to send a standard POST request
	 * @param httpServletRequest The {@link HttpServletRequest} that contains the POST data to be sent via the {@link PostMethod}
	 * @throws IOException
	 */
	private void handleStandard(EntityEnclosingMethod methodProxyRequest,
			HttpServletRequest httpServletRequest) throws IOException {
		try {

			InputStream is =httpServletRequest.getInputStream();

			methodProxyRequest.setRequestEntity(new InputStreamRequestEntity(is));

		} catch (IOException e) {
			throw new IOException(e);
		}
	}

	/**
	 * Executes the {@link HttpMethod} passed in and sends the proxy response back to the client via the given {@link HttpServletResponse}
	 * 
	 * @param httpMethodProxyRequest An object representing the proxy request to be made
	 * @param httpServletResponse An object by which we can send the proxied response back to the client
	 * @param digest
	 * @throws IOException Can be thrown by the {@link HttpClient}.executeMethod
	 * @throws ServletException Can be thrown to indicate that another error has occurred
	 */
	private void executeProxyRequest(HttpRequestBase httpMethodProxyRequest,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			ProxyInfo proxyInfo) throws IOException, ServletException {


		httpMethodProxyRequest.setFollowRedirects(false);

		InputStream inputStreamServerResponse = null;

		try {
			httpClient = HttpClient();
			int intProxyResponseCode = httpClient.executeMethod(httpMethodProxyRequest);

			onRemoteResponse(httpMethodProxyRequest);

			if (intProxyResponseCode >= HttpServletResponse.SC_MULTIPLE_CHOICES /* 300 */
					&& intProxyResponseCode < HttpServletResponse.SC_NOT_MODIFIED /* 304 */) {

				String stringStatusCode = Integer.toString(intProxyResponseCode);
				String stringLocation = httpMethodProxyRequest.getResponseHeader(
						Utils.LOCATION_HEADER).getValue();

				if (stringLocation == null) {
					throw new ServletException("Recieved status code: " + stringStatusCode
							+ " but no " + Utils.LOCATION_HEADER
							+ " header was found in the response");
				}

				httpServletResponse.sendRedirect(stringLocation);
				return;

			} else if (intProxyResponseCode == HttpServletResponse.SC_NOT_MODIFIED) {
				httpServletResponse.setIntHeader(Utils.CONTENT_LENGTH_HEADER_NAME, 0);
				httpServletResponse.setStatus(HttpServletResponse.SC_NOT_MODIFIED);

				return;
			}
			httpServletResponse.setStatus(intProxyResponseCode);
			Header[] headerArrayResponse = httpMethodProxyRequest.getResponseHeaders();
			for (Header header : headerArrayResponse) {
				if (header.getName().equalsIgnoreCase(Utils.HTTP_HEADER_ACCEPT_ENCODING)
						&& header.getValue().toLowerCase().contains("gzip")){
					//					useGzip = true;
					continue;
				}else if (header.getName().equalsIgnoreCase(Utils.HTTP_HEADER_CONTENT_ENCODING)
						&& header.getValue().toLowerCase().contains("gzip")){
					//					useGzip = true;
					continue;
				}else if (header.getName().equalsIgnoreCase(Utils.HTTP_HEADER_TRANSFER_ENCODING)){
					continue;
				}else if (header.getName().equalsIgnoreCase(Utils.HTTP_HEADER_Content_Length)){
					continue;
				}else{
					httpServletResponse.setHeader(header.getName(), header.getValue());
				}
			}

			inputStreamServerResponse = httpMethodProxyRequest
					.getResponseBodyAsStream();
			OutputStream out = httpServletResponse.getOutputStream();
			if(inputStreamServerResponse != null){
				File f = this.proxyConfig.newTmpFile();
				FileOutputStream fos = new FileOutputStream(f);

				byte[] b = new byte[proxyConfig.getDefaultStreamByteSize()];


				int read = 0;
				while((read = inputStreamServerResponse.read(b)) > 0){ 
					fos.write(b, 0, read);
				}
				fos.close();

				httpServletResponse.setHeader(Utils.HTTP_HEADER_Content_Length, "" + f.length());
				FileInputStream fis = new FileInputStream(f);


				while((read = fis.read(b)) > 0){
					out.write(b, 0, read);	
				}
				fis.close();
			}else{
				httpServletResponse.setHeader(Utils.HTTP_HEADER_Content_Length, "0");

			}
			out.close();
		} catch (Exception e) {
			if (LOGGER.isLoggable(Level.SEVERE))
				LOGGER.log(Level.SEVERE, "Error executing HTTP method ", e);
		} finally {
			try {
				if(inputStreamServerResponse != null)
					inputStreamServerResponse.close();
			} catch (IOException e) {
				if (LOGGER.isLoggable(Level.SEVERE))
					LOGGER.log(Level.SEVERE,
							"Error closing request input stream ", e);
				throw new ServletException(e.getMessage());
			}

			httpMethodProxyRequest.releaseConnection();
		}
	}

	/**
	 * Retrieves all of the headers from the servlet request and sets them on the proxy request
	 * 
	 * @param httpServletRequest The request object representing the client's request to the servlet engine
	 * @param httpMethodProxyRequest The request that we are about to send to the proxy host
	 * @return ProxyInfo
	 */
	@SuppressWarnings("rawtypes")
	private ProxyInfo setProxyRequestHeaders(URL url, HttpServletRequest httpServletRequest,
			HttpRequestBase httpMethodProxyRequest) {

		final String proxyHost = url.getHost();
		final int proxyPort = url.getPort();
		final String proxyPath = url.getPath();
		final ProxyInfo proxyInfo = new ProxyInfo(proxyHost, proxyPath, proxyPort);

		Enumeration enumerationOfHeaderNames = httpServletRequest.getHeaderNames();

		while (enumerationOfHeaderNames.hasMoreElements()) {
			String stringHeaderName = (String) enumerationOfHeaderNames.nextElement();

			if (stringHeaderName.equalsIgnoreCase(Utils.CONTENT_LENGTH_HEADER_NAME))
				continue;

			Enumeration enumerationOfHeaderValues = httpServletRequest.getHeaders(stringHeaderName);

			while (enumerationOfHeaderValues.hasMoreElements()) {
				String stringHeaderValue = (String) enumerationOfHeaderValues.nextElement();

				if (stringHeaderName.equalsIgnoreCase(Utils.HOST_HEADER_NAME)) {
					stringHeaderValue = Utils.getProxyHostAndPort(proxyInfo);
				}

				if (stringHeaderName.equalsIgnoreCase(Utils.HTTP_HEADER_ACCEPT_ENCODING)
						&& stringHeaderValue.toLowerCase().contains("gzip"))
					continue;
				if (stringHeaderName.equalsIgnoreCase(Utils.HTTP_HEADER_CONTENT_ENCODING)
						&& stringHeaderValue.toLowerCase().contains("gzip"))
					continue;
				if (stringHeaderName.equalsIgnoreCase(Utils.HTTP_HEADER_TRANSFER_ENCODING))
					continue;

				BasicHeader header = new BasicHeader(stringHeaderName, stringHeaderValue);

				httpMethodProxyRequest.setHeader(header);
			}
		}

		return proxyInfo;
	}

	public int getMaxFileUploadSize() {
		return maxFileUploadSize;
	}

}
