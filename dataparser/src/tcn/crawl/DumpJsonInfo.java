package crawl;

import java.io.File;
import java.util.List;

import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.bmfs.MFile;
import com.bmtech.utils.bmfs.MFileReader;

import crawl.ParseTcn.Comment;

public class DumpJsonInfo {

	public static void main(String[]args) throws Exception{
		MDir mdir = new MDir(new File("/stock/tcn"));
		MFileReader reader = mdir.openReader();
		while(reader.hasNext()){
			MFile file = reader.next();
			System.out.println("file:" + file);
			byte[] bytes = reader.getBytesUnGZiped();
			String html = new String(bytes);
			try{
				List<Comment> lst = ParseTcn.parse(html);
				for(Comment c : lst){
					System.out.println(c);
				}
			}catch(Exception e){

			}
		}
	}
}
