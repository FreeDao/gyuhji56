package rays.httpproxy;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.bmtech.utils.io.ConfigReader;
import com.bmtech.utils.log.LogHelper;

public class Startor {

	public static void main(String[] args) throws Exception {
		ConfigReader cr = new ConfigReader("config/proxy.properties", "main");
		int port = cr.getInt("proxy_port");
		Server server = new Server(port);

		ProxyHandler handler = new ProxyHandler();
		server.setHandler(handler);

		server.start();
		LogHelper.log.info("httproxy started at port %s", port);
		server.join();
	}

	public static class ProxyHandler extends AbstractHandler {
		LogHelper log = new LogHelper("handler");

		ProxyHandler() {

		}

		@Override
		public void handle(String target, Request baseRequest,
				HttpServletRequest request, HttpServletResponse response)
				throws IOException {

			OutputStream ops = null;

			try {
				String queryStr = request.getQueryString();
				ops = response.getOutputStream();
				String method = request.getMethod();
				String path = baseRequest.getRequestURL().toString();
				String fullPath = path;
				if (queryStr != null) {
					fullPath = path + "?" + queryStr;
				}
				URL url = new URL(fullPath);

				System.out.println("---> " + path);
				HTTPProxy proxy = new HTTPProxy();
				if (method.equalsIgnoreCase("GET")) {
					proxy.doGet(url, request, response);
				} else if (method.equalsIgnoreCase("post")) {
					proxy.doPost(url, request, response);
				} else {
					log.debug("xxxxxxxxxxxxxxx for %s , to %", method, target);
					String str = "ERROR method " + method + ", for url "
							+ target;
					response.getOutputStream().write(str.getBytes());
				}
				System.out.println("<--- " + path);

			} catch (Exception e) {
				e.printStackTrace();
				System.err.println(baseRequest.getRequestURL().toString());
				System.err.println(target);
				response.setStatus(500);
				response.getOutputStream().write(
						("proxy error!" + e).getBytes());
			} finally {
				if (ops != null) {
					ops.close();
				}
			}
		}
	}
}