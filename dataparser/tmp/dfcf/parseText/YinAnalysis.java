package dfcf.parseText;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bmtech.utils.Consoler;
import com.bmtech.utils.counter.Counter;
import com.bmtech.utils.counter.NumCount;
import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.io.LineWriter;
import com.bmtech.utils.log.LogHelper;

import dfcf.Env;

public class YinAnalysis {
	LogHelper log = new LogHelper("yya");
	Map<Character, List<Integer>>map = new HashMap<Character, List<Integer>>();

	static class Group{
		String line;
		List<String> sames = new ArrayList<String>();
		Counter<String>counter = new Counter<String>();
		Group(LineReader lr){
			line = lr.next();
			Pattern px = Pattern.compile(line.replace("...", "(.{1,4})"));
			while(lr.hasNext()){
				String x = lr.next();
				if(x.charAt(0) == '\t'){
					sames.add(x);
					Matcher m = px.matcher(x);
					boolean mc = m.find();
					if(mc){
						int count = m.groupCount();
						if(count > 0){
							counter.count(m.group(1));
						}
					}
				}else{
					break;
				}
			}
		}
		public String toString(){
			StringBuilder sb = new StringBuilder();
			sb.append(line);
			sb.append('(');
			sb.append(this.counter.size());
			sb.append('/');
			sb.append(this.sames.size());
			sb.append(')');
			
			
			sb.append("\n");

			for(Entry<String,NumCount> e: counter.topEntry()){

				sb.append('\t');
				sb.append(e);
			}


			return sb.toString();

		}
	}
	public static void main(String[]args) throws IOException, Exception{
		LineWriter lw = new LineWriter(
				new File(Env.tmpFile, "dfcf/lines-parsed-yin.txt"), false);
		LineReader lr = new LineReader(
				new File(Env.tmpFile, "dfcf/lines-parsed.txt"));
		lr.hasNext();
		List<Group>gs = new ArrayList<Group>();
		int index = 0;
		while(true){
			String line = lr.next();
			if(line == null){
				break;
			}
			Group g = new Group(lr);
			gs.add(g);
			System.out.println((++index) + "\t" + g.toString());
			Consoler.readString("~");
		}
	}
}
