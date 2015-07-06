package dfcf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.bmfs.MFile;
import com.bmtech.utils.bmfs.MFileReader;

public class FromCurpInnerUnzip {
	MDir mdir;
	FromCurpInnerUnzip() throws IOException{
		mdir = new MDir(new File("/dfcfcmt"), false);
	}
	public void read() throws Exception{
		MFileReader itr = mdir.openReader();
		byte[]buf = new byte[4096];
		int cnt = 0;
		long st = System.currentTimeMillis();
		long totalx = 0, total = 0;
		ByteArrayOutputStream ops = new ByteArrayOutputStream();
		while(itr.hasNext()){
			MFile mfile = itr.next();
			InputStream ips = itr.getInputStreamUnGZiped();
			int len = 0;
			ops.reset();
			while(true){
				int xlen = ips.read(buf);
				if(xlen == -1){
					break;
				}
				len += xlen;
				ops.write(buf, 0, xlen);
			}
			total += len;
			totalx += len;
			++cnt;
			
			if(cnt % 1000 == 0){
				System.out.println("cnt:" + cnt + ", " + ((len == mfile.getLength()) + " total " + total + ", totalx " + totalx + ", pressRate " + total*1000/totalx/10.0 + "% " + (total/(System.currentTimeMillis() - st)/1024) + "MB/s " + (System.currentTimeMillis() - st)/1000.0 + "   " + (++cnt) + " " + mfile));
				st = System.currentTimeMillis();
				total = 0;
				totalx = 0;
			}
			
		}
	}
	public static void main(String[] args) throws Exception {
		FromCurpInnerUnzip fc = new FromCurpInnerUnzip();
		fc.read();
	}
	
}
