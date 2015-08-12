package rays.httpproxy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bmtech.utils.http.HttpCrawler;
import com.bmtech.utils.http.HttpHandler.Prop;

public class Httproxy {
	private int connectTimeout = 30000;
	private int readTimeout = 90000;
	public Httproxy() {}

	public void doGet(URL url, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws Exception {
		try {

			final List<String[]> headers = collectClientHeaders(url,
					httpServletRequest);

			HttpCrawler crl = new HttpCrawler(url) {
				@Override
				public int connect(Prop[] props) throws IOException {
					if (handler.getProxy() != null) {
						conn = (HttpURLConnection) url.openConnection(handler
								.getProxy());
					} else {
						conn = (HttpURLConnection) url.openConnection();
					}
					conn.setConnectTimeout(connectTimeout);
					conn.setReadTimeout(readTimeout);
					conn.setInstanceFollowRedirects(false);
					for (Prop p : props) {
						conn.addRequestProperty(p.key, p.getValue());
					}
					conn.connect();
					connected = true;
					return parseHttpHead();
				}
			};
			crl.connect(toProp(headers));
			this.executeProxyRequest(crl, httpServletRequest,
					httpServletResponse);
		} finally {
		}
	}

	public void doPost(URL url, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws Exception {
		try {
			List<String[]> headers = collectClientHeaders(url,
					httpServletRequest);

			HttpCrawler crl = new HttpCrawler(url) {

				@Override
				public int post(Prop[] props, byte[] bs) throws IOException {
					if (handler.getProxy() != null) {
						conn = (HttpURLConnection) url.openConnection(handler
								.getProxy());
					} else {
						conn = (HttpURLConnection) url.openConnection();
					}
					conn.setConnectTimeout(connectTimeout);
					conn.setReadTimeout(readTimeout);

					conn.setInstanceFollowRedirects(false);
					for (Prop p : props) {
						conn.addRequestProperty(p.key, p.getValue());
					}
					conn.setDoOutput(true);
					OutputStream ops = conn.getOutputStream();
					ops.write(bs);
					ops.flush();
					ops.close();
					connected = true;
					return parseHttpHead();
				}
			};
			byte[] bytes = toBytes(httpServletRequest.getInputStream());
			String str = new String(bytes);
			System.out.println("post data:" + str);
			crl.post(toProp(headers), bytes);
			this.executeProxyRequest(crl, httpServletRequest,
					httpServletResponse);
		} finally {
		}
	}

	private Prop[] toProp(List<String[]> headers) {
		Prop[] ret = new Prop[headers.size()];
		for (int x = 0; x < headers.size(); x++) {
			String[] xx = headers.get(x);
			ret[x] = new Prop(xx[0], xx[1]);
		}
		return ret;
	}

	private byte[] toBytes(ServletInputStream inputStream) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int readed = 0;
		while ((readed = inputStream.read(buf)) != -1) {
			out.write(buf, 0, readed);
		}
		return out.toByteArray();
	}


	private void executeProxyRequest(HttpCrawler crl,
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws Exception,
	ServletException {
		try {
			Map<String, List<String>> oldHead = crl.getHeadInfo();
			HttpHeader header = new HttpHeader(oldHead);
			header.setHttpCode(crl.getHttpCode());

			byte[] injected = RewriteEngine.httpHeaderRewriter.filter(crl.getURL().toString(), header, null);
			int intProxyResponseCode = header.getHttpCode();

			httpServletResponse.setStatus(intProxyResponseCode);
			Iterator<Entry<String, List<String>>> itr = header.respHead.entrySet()
					.iterator();
			while (itr.hasNext()) {
				Entry<String, List<String>> ent = itr.next();
				String name = ent.getKey();
				List<String> lst = ent.getValue();
				if (name == null) {
					System.out
					.println("status " + lst + " for " + crl.getURL());
					continue;
				}

				if (lst.size() > 1) {
					System.out.println("multi " + name + " for url "
							+ crl.getURL());
					for (String x : lst) {
						System.out.println("\t" + x);
					}
				}

				for (String value : lst) {
					if (name.equalsIgnoreCase(Utils.HTTP_HEADER_CONTENT_ENCODING)) {
						if (value.equalsIgnoreCase("gzip")) {
							continue;
						}
					} else if (name
							.equalsIgnoreCase(Utils.HTTP_HEADER_TRANSFER_ENCODING)) {
						if (value.equalsIgnoreCase("gzip")) {
							continue;
						}
					} else if (name
							.equalsIgnoreCase(Utils.HTTP_HEADER_Content_Length)) {
						continue;
					}
					httpServletResponse.addHeader(name, value);
				}
			}
			if (intProxyResponseCode >= HttpServletResponse.SC_MULTIPLE_CHOICES /* 300 */
					&& intProxyResponseCode < HttpServletResponse.SC_NOT_MODIFIED /* 304 */) {

				String stringStatusCode = Integer
						.toString(intProxyResponseCode);
				List<String> stringLocation =  header.respHead
						.get(Utils.LOCATION_HEADER);

				if (stringLocation == null || stringLocation.size() == 0) {
					throw new ServletException("Recieved status code: "
							+ stringStatusCode + " but no "
							+ Utils.LOCATION_HEADER
							+ " header was found in the response");
				}
				if (stringLocation.size() != 1) {
					System.err.println("Error redirect " + stringLocation);
				}
				String redr = stringLocation.get(0);
				System.out.println("redirect to  " + redr + " , from "
						+ crl.getURL());
				httpServletResponse.sendRedirect(stringLocation.get(0));
				return;

			} else if (intProxyResponseCode == HttpServletResponse.SC_NOT_MODIFIED) {
				httpServletResponse.setIntHeader(
						Utils.CONTENT_LENGTH_HEADER_NAME, 0);
				httpServletResponse
				.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				return;
			}

			if(injected == null){
				ByteArrayOutputStream ops = new ByteArrayOutputStream();
				crl.dumpTo(ops);
				injected = ops.toByteArray();
			}else{
				System.out.println("using injected html");
			}

			httpServletResponse.setHeader(Utils.HTTP_HEADER_Content_Length, ""
					+ injected.length);

			OutputStream out = httpServletResponse.getOutputStream();
			if (out != null) {
				out.write(injected);
			}
		} finally {
			crl.close();
		}
	}

	private List<String[]> collectClientHeaders(URL url,
			HttpServletRequest httpServletRequest) {

		Enumeration<?> enumerationOfHeaderNames = httpServletRequest
				.getHeaderNames();
		List<String[]> headers = new ArrayList<String[]>();
		while (enumerationOfHeaderNames.hasMoreElements()) {
			String stringHeaderName = (String) enumerationOfHeaderNames
					.nextElement();

			if (stringHeaderName
					.equalsIgnoreCase(Utils.CONTENT_LENGTH_HEADER_NAME))
				continue;

			Enumeration<?> enumerationOfHeaderValues = httpServletRequest
					.getHeaders(stringHeaderName);

			while (enumerationOfHeaderValues.hasMoreElements()) {
				String stringHeaderValue = (String) enumerationOfHeaderValues
						.nextElement();

				headers.add(new String[] { stringHeaderName, stringHeaderValue });
			}
		}
		return headers;
	}

	static String urlEnc(String str) {
		try {
			return URLEncoder.encode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
