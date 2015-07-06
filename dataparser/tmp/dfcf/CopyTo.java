package dfcf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.bmfs.MFile;
import com.bmtech.utils.bmfs.MFileReader;
import com.bmtech.utils.bmfs.MFileWriter;

public class CopyTo {
	MDir to, from;
	CopyTo() throws IOException{
		to = new MDir(new File("/dfcfcmt-bak"), true);
		from = new MDir(new File("/dfcfcmt"), false);
	}
	public void read() throws Exception{
		MFileReader itr = from.openReader();
		MFileWriter w = to.openWriter();
		while(itr.hasNext()){
			MFile mfile = itr.next();
			
			MFile add = to.createMFileIfPossible(mfile.name);
			if(add == null){
				System.out.println("SKIP " + mfile);
				itr.skip();
			}else{
				InputStream ips = itr.getInputStream();
				System.out.println("adding " + mfile);
				w.openMFile(add).write(ips).close();
			}
			
		}
	}
	public static void main(String[] args) throws Exception {
		CopyTo fc = new CopyTo();
		fc.read();
	}
	
}
