package govs;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.io.LineWriter;

public class XXX {
	public static void main(String[] args) throws Exception {
		LineReader lr = new LineReader("/x.txt");
		LineWriter lw = new LineWriter("gov.txt", false);
		Set<String>set = new HashSet<String>();
		Set<String>setHosts = new HashSet<String>();
		while(lr.hasNext()){
			String line = lr.next();
			if(set.contains(line)){
				continue;
			}
			URL url = new URL(line);
			String host = url.getHost();
			if(host.endsWith("gov.cn") ||
					host.endsWith("edu.cn") ||
					host.endsWith("org.cn")
			){
				set.add(line);
				setHosts.add(host);
			}else{
				continue;
			}
			lw.writeLine(line);
		}
		lw.close();
		System.out.println("hosts:" + setHosts.size());
	}

}
