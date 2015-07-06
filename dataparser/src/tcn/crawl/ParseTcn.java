package crawl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bmtech.htmls.parser.Node;
import com.bmtech.htmls.parser.NodeFilter;
import com.bmtech.htmls.parser.Parser;
import com.bmtech.htmls.parser.Tag;
import com.bmtech.htmls.parser.filters.HasAttributeFilter;
import com.bmtech.htmls.parser.filters.TagNameFilter;
import com.bmtech.htmls.parser.tags.LinkTag;
import com.bmtech.htmls.parser.util.NodeList;
import com.bmtech.utils.Misc;
import com.bmtech.utils.io.FileGet;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ParseTcn {
	public static class Comment{
		public String usernickName;
		public String userId;
		public String content;
		public String url;
		public String zan;
		public String time;
		public String zhuanfa;
		public String pinglun;
		public Comment refer;
		public String toString(){
			return toString(false);
		}
		public String toString(boolean extend){
			StringBuilder sb = new StringBuilder();
			Field[] fs = this.getClass().getFields();
			sb.append("comment:\n");
			for(Field f : fs){
				try {
					sb.append("\t");
					sb.append(f.getName());
					sb.append(" : ");
					Object o = f.get(this);
					if(o instanceof Comment){
						if(extend){
							sb.append(o);
						}else{
							sb.append(((Comment)o).url);
						}
					}else{
						sb.append(o);
					}
					sb.append("\n");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return sb.toString();
		}

	}
	static String prefix = "{\"pid\":\"pl_weibo_direct\"";
	static NodeList extract(Node n, NodeFilter... filters){
		NodeList nl = new NodeList();
		nl.add(n);
		return extract(nl, filters);
	}
	static NodeList extract(NodeList n, NodeFilter... filters){
		NodeList current = n;
		for(NodeFilter flt : filters){
			current = extract(current, flt);
		}
		return current;
	}
	static NodeList extract(NodeList n, NodeFilter filter){
		NodeList ret = new NodeList();
		NodeList got = n.extractAllNodesThatMatch(filter, true);
		for(int x = 0; x < got.size(); x ++){
			Node nl = got.elementAt(x);
			ret.add(nl);
		}
		return ret;
	}
	public static Comment parseRefer(Node n) throws IOException, Exception{
		NodeList content;

		HasAttributeFilter feedList = new HasAttributeFilter("node-type", "feed_list_forwardContent");
		content = extract(n, feedList);

		NodeList clicks = extract(n, new HasAttributeFilter("class", "info W_linkb W_textb"), new TagNameFilter("span"), new TagNameFilter("a"));
		NodeList vv = extract(n, new HasAttributeFilter("class", "info W_linkb W_textb"), new HasAttributeFilter("class", "date"));
		Tag t = (LinkTag)(vv.elementAt(0));
		String date = t.getAttribute("date");
		String url = t.getAttribute("href");

		Comment c = new Comment();
		LinkTag lt = (LinkTag) content.elementAt(0).getChildren().extractAllNodesThatMatch(new TagNameFilter("a")).elementAt(0);

		c.content = extract(content, new TagNameFilter("em")).toHtml().trim();
		c.userId = lt.getAttribute("href");
		c.pinglun = clicks.elementAt(1).toPlainTextString().trim();
		c.time = date;
		c.url = url;
		c.usernickName = lt.getAttribute("nick-name");
		c.zan = null;
		c.zhuanfa = clicks.elementAt(0).toPlainTextString().trim();

		return c;
	}
	public static Comment parse(Node n) throws IOException, Exception{
		NodeList content;
		NodeList nlx = extract(n.getChildren(), new HasAttributeFilter("node-type", "feed_list_forwardContent"));
		Comment refer = null;
		if(nlx.size() > 0){
			Node par = nlx.elementAt(0);
			Node parOfPar = par.getParent();
			parOfPar.getParent().getChildren().remove(parOfPar);
			String html = parOfPar.toHtml();
			refer = parseRefer(parOfPar);
		}
		HasAttributeFilter feedList = new HasAttributeFilter("node-type", "feed_list_content");
		content = extract(n, feedList);


		NodeList clicks = extract(n, new HasAttributeFilter("class", "content"), new TagNameFilter("span"), new TagNameFilter("a"));
		NodeList vv = extract(n, new HasAttributeFilter("class", "content"), new HasAttributeFilter("class", "date"));
		Tag t = (LinkTag)(vv.elementAt(0));
		String date = t.getAttribute("date");
		String url = t.getAttribute("href");

		Comment c = new Comment();
		LinkTag lt = (LinkTag) content.elementAt(0).getChildren().extractAllNodesThatMatch(new TagNameFilter("a")).elementAt(0);

		c.content = extract(content, new TagNameFilter("em")).toHtml().trim();
		c.userId = lt.getAttribute("href");
		c.pinglun = clicks.elementAt(3).toPlainTextString().trim();
		c.time = date;
		c.url = url;
		c.usernickName = lt.getAttribute("nick-name");
		c.zan = clicks.elementAt(0).toPlainTextString().trim();
		c.zhuanfa = clicks.elementAt(1).toPlainTextString().trim();
		c.refer = refer;
		return c;
	}

	public static List<Comment> parse(String str) throws IOException, Exception{

		String var = Misc.substring(str, prefix, "})</script>");
		var = prefix + var + "}";
		ObjectMapper objectMapper = new ObjectMapper();
		System.out.println(var);
		Map<String, Object> map = objectMapper.readValue(var, Map.class);

		var = (String) map.get("html");
		Parser p = new Parser(var);
		HasAttributeFilter haf = new HasAttributeFilter("action-type", "feed_list_item");

		NodeList nl = p.parse(null);
		NodeList nl2 = nl.extractAllNodesThatMatch(haf, true);

		List<Comment> ret = new ArrayList<Comment>();
		for(int x = 0; x < nl2.size(); x ++){
			Node n = nl2.elementAt(x);
			Comment c = parse(n);
			ret.add(c);
		}
		return ret;
	}
	public static void main(String[] args) throws Exception {
		File f = new File("/aa.txt");
		String html = FileGet.getStr(f);
		List<Comment> list = parse(html);
		for(Comment c : list){
			System.out.println(c);
		}
	}
}
