package dfcf;

import java.io.File;
import java.util.HashSet;

import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.io.LineWriter;

public class DetailRefine {

	private static String rewrite(String line){
		if(line.contains("#sendnewt")){
			return line.replace("#sendnewt", "");
		}
		if(!line.toLowerCase().contains("guba.eastmoney.com")){
			return null;
		}
		if(!line.contains("/news,")){
			return null;
		}
		if(!line.endsWith("html")){
			return null;
		}
		return line;
	}
	public static void main(String[] args) throws Exception {
		File input = new File(Env.tmpFile, "topics_new.txt");//Consoler.readString("fileInput:"));
		File output = new File(Env.tmpFile, "urls.txt");

		HashSet<String>old = new HashSet<String>();
		{
			LineReader has = new LineReader(output);
			while(has.hasNext()){
				String next = has.next();
				next = rewrite(next);
				if(next == null)
					continue;
				old.add(next);
			}
			has.close();
		}		
		LineReader lr = new LineReader(input);
		LineWriter lw = new LineWriter(output, false);
		while(lr.hasNext()){
			String next = lr.next();
			next = rewrite(next);
			if(next == null)
				continue;
			if(old.contains(next))
				continue;
			lw.writeLine(next);
		}
		for(String x : old){
			lw.writeLine(x);
		}
		lw.close();
		lr.close();
	}
}
