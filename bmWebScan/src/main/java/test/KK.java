package test;

import java.net.MalformedURLException;
import java.net.URL;

public class KK {

	public static void main(String[] args) throws MalformedURLException {
		String x = "HTTP://XdsD.com/sdf";
		URL url = new URL(x);
		System.out.println(url.getHost());
		System.out.println(url.getProtocol());
	}
}
