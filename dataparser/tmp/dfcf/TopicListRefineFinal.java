package dfcf;

import java.io.File;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.io.LineWriter;

public class TopicListRefineFinal {

	private static String rewrite(String line){
		if(line.contains("#sendnewt")){
			line = line.replace("#sendnewt", "");
		}
		if(!line.toLowerCase().contains("guba.eastmoney.com")){
			return null;
		}
		if(line.contains("/topic,") || line.contains("/list,")){
			return line.replace("/topic,", "/list,");
		}else{
			return null;
		}
	}
	public static void main(String[] args) throws Exception {
		File input = new File(Env.tmpFile, "topics.txt");//Consoler.readString("fileInput:"));
		File output = new File(Env.tmpFile, "topics_" + System.currentTimeMillis() + ".txt");

		LineReader lr = new LineReader(input);
		LineWriter lw = new LineWriter(output, false);
		Pattern p = Pattern.compile("ist\\,(\\d+).html");


		HashSet<Integer>set = new HashSet<Integer>();
		while(lr.hasNext()){
			String next = lr.next();
			next = rewrite(next);
			if(next == null)
				continue;
			Matcher m = p.matcher(next);
			if(m.find()){
				String g1 = m.group(1);
				int g1i = Integer.parseInt(g1);
				System.out.println(next  + " " + g1i);
				set.add(g1i);
			}else{
//				System.out.println("bad:" + next);
			}
		}
		
		lr.close();
		System.out.println(set.size());
		
		for(int x = 99; x < 5000; x ++){
			for(int y : set){
				String v = "http://guba.eastmoney.com/list,"+ y + "_" + x + ".html";
				System.out.println(v);
				lw.writeLine(v);
			}
		}
		
		lw.close();
	}
}
