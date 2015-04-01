package test.utils;

import java.io.File;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bmtech.utils.counter.Counter;
import com.bmtech.utils.counter.NumCount;
import com.bmtech.utils.io.LineReader;
import com.bmtech.utils.segment.RMMSegment;
import com.bmtech.utils.segment.RMMSegment.CoupleResult;

public class TestSegment {
	public static void main(String[] args) throws Exception {
		LineReader lr = new LineReader(new File("c:/aaaa.txt"));
		Counter<String>cnt = new Counter<String>();
		RMMSegment instance =  RMMSegment.instance;
		while (lr.hasNext()) {
			String line = lr.next();
			//			RMMSegment.instance.noCoverSegment(line).toQuerySet()
			CoupleResult result = instance.noCoverSegment(line);
			while(result.handler.hasNext()){
				String token = result.handler.next();
				cnt.count(token);
			}
			System.out.println(line);
			Pattern p = Pattern.compile("\\d{1,2}\\s{0,}[\\:：：]\\s{0,}\\d{1,2}\\s{0,}[\\-至到—\\~～]\\s{0,}\\d{1,2}\\s{0,}[\\:：：]\\s{0,}\\d{1,2}");
			Matcher m = p.matcher(line);
			while(m.find()){
				String mt = m.group();
				System.out.println("-->" + mt);
				System.out.println();
			}
//						boolean bm = m.matches();
//			//			if(mc){
//			int mc =	m.groupCount();
//			if(mc > 1){
//				for(int x = 1; x < mc; x ++){
//					System.out.println("-->" + m.group(x));
//				}
//			}
			//			}

			//			System.out.println("binarySegment:" + binarySegment(ipt));

		}
		lr.close();

		Iterator<Entry<String, NumCount>> itr = cnt.topEntry(10000).iterator();
		while(itr.hasNext()){
			Entry<String, NumCount> e = itr.next();
			if(e.getKey().length() == 1)
				continue;
			System.out.println(e.getKey());
		}
	}
}
