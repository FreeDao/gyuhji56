package textMatcher.Pattern.tools;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import com.bmtech.utils.io.LineReader;

public class SingleCnMerge {

	/**
	 * 如果发现空格，则从空格后开始搜索 汉字-空格的复合体
	 * @param cs
	 * @param start
	 * @return
	 */
	private static int getJGLen(char[] cs, int start){
		boolean lastKG = true;
		boolean lastHZ = true;
		int len = 0;
		for(int x = start; x < cs.length; x ++){
//			System.out.println(cs[x]);
			if(cs[x] == ' '){
				if(!lastHZ){
					return len;
				}
				len ++;
				lastKG = true;
				lastHZ = false;

			}else if(cs[x] > 255){
				if(Character.isLetter(cs[x])){
					if(lastKG){
						lastHZ = true;
						len ++;
					}else{
						len -= 3;
						break;
					}

				}else{
					len -= 1;
					return len;
				}
				lastKG = false;


			}else{
				if(!lastKG){
					len --;
				}
				break;
			}
		}
		return len;
	}
	public static String mergeSingleCns(String str){
		StringBuilder sb = new StringBuilder();
		char cs[] = str.toCharArray();
		boolean lastKG = true;
		for(int x = 0; x < cs.length; x ++){
			char c = cs[x];

			if(cs[x] > 255 && Character.isLetter(cs[x])){
				if(lastKG){
					int len = getJGLen(cs, x);
					if(len < 4){
						sb.append(c);
					}else{
						for(int v = 0; v < len; v ++){
							c = cs[x + v];
							if(c != ' '){
								sb.append(c);
							}
						}
						x = x + len - 1;
					}
				}else{
					sb.append(c);
				}
				lastKG = false;
			}else{
				sb.append(c);
				if(c == ' '){
					lastKG = true;
				}else{
					lastKG = false;
				}
			}
		}
		return sb.toString();
	}
	public static void main(String[]args) throws IOException{

		{
			String v = "主 题 词 牌楼新建 立项 批复|";
			String v2 = mergeSingleCns(v);
			System.out.println("'" + v + "'");
			System.out.println("'" + v2 + "'");
		}




		for(int idx = 77; idx < 100; idx ++){
			File dir = new File("E:\\work\\parsedHtmls\\"+idx);
			//			Syste
			File[] fs = dir.listFiles();

			for(File f : fs){
				//				System.out.println(f.getAbsolutePath());
				LineReader lr = new LineReader(f);
				while(lr.hasNext()){
					String line = lr.next();
					String v = line.replace('　', ' ').replaceAll("\\s{2,}", " ").trim();
					String v2 = mergeSingleCns(v);
					PrintStream p;
					if(v.equals(v2)){
						p = System.out;
					}else{
						p = System.err;
						p.println("'--------------'");
						p.println("'" + v + "'");
						p.println("'" + v2 + "'");
						p.flush();
					}
				}
				lr.close();
				//Consoler.readString("press any key to continue");
			}
		}
	}
}
