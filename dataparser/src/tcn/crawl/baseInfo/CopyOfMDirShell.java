package crawl.baseInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import com.bmtech.utils.Consoler;
import com.bmtech.utils.HtmlRemover;
import com.bmtech.utils.Misc;
import com.bmtech.utils.ReadAllInputStream;
import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.bmfs.MFile;
import com.bmtech.utils.bmfs.MFileReader;
import com.bmtech.utils.io.LineWriter;

public class CopyOfMDirShell {
	public static void main(String[]args) throws Exception{
		
		File dir = new File("/stock/base");
		MDir mdir = new MDir(dir, false);
		MFileReader reader = mdir.openReader();
		LineWriter lw = new LineWriter("/stock/总股本", false);
		while(reader.hasNext()){
			MFile mfile = reader.next();
			System.out.println(mfile);
			if(!mfile.name.contains("OperationsRequired")){
				ReadAllInputStream rai = reader.getInputStream();
				while(rai.read() != -1){
					continue;
				}
				continue;
			}
			System.out.println(mfile);
			InputStream ips = reader.getInputStreamUnGZiped();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while(true){
				int x = ips.read();
				if(x == -1){
					break;
				}
				baos.write(x);
			}
			byte[] bs = baos.toByteArray();
			String str = new String(bs);
			str = Misc.substring(str, "流通股本", "</td>");
			System.out.println(str);
			if(str != null){
				lw.writeLine(mfile.name + "\t"  + HtmlRemover.htmlToLineFormat(str).lines);
			}
//			Consoler.readString("enter~>");
		}
		lw.close();
		System.out.println("size:" + mdir.size());
		
	}
}
