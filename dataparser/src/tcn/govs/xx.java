package govs;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

import com.bmtech.htmls.parser.Parser;
import com.bmtech.htmls.parser.filters.NodeClassFilter;
import com.bmtech.htmls.parser.tags.LinkTag;
import com.bmtech.htmls.parser.util.NodeList;
import com.bmtech.utils.http.HttpCrawler;
import com.bmtech.utils.io.LineWriter;

public class xx {

	public static void main(String []args) throws IOException{
		ArrayList<String>lst = new ArrayList<String>();
		lst.add("http://gov.elanw.com/");
		HashSet<String>has = new HashSet<String>();
		LineWriter lw = new LineWriter("/x.txt", true);
		while(true){
			for(int index = 0; index < lst.size(); index ++){
				String urlx = lst.get(index);
				System.out.println(index + ":" +lst.size() + " " + urlx);
				if(has.contains(urlx)){
					continue;
				}
				try{
					
					URL url = new URL(urlx);
					has.add(urlx);
					HttpCrawler crl = new HttpCrawler(url);
					crl.connect();
					String xx = crl.getString();
					Parser p = new Parser(xx);
					NodeList nl = p.parse(new NodeClassFilter(LinkTag.class));
					for(int x = 0; x < nl.size(); x ++){
						try{
							LinkTag t = (LinkTag) nl.elementAt(x);
							URL u = new URL(t.getLink());
							String ustr = u.toString();
							if(!has.contains(ustr)){
								String host = u.getHost();
								if(host.equals("gov.elanw.com")){
									lst.add(ustr);
								}
							}
							lw.writeLine(ustr);
							lw.flush();
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
}
