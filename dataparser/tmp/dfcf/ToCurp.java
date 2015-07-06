package dfcf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.bmtech.utils.bmfs.MDir;
import com.bmtech.utils.bmfs.MFile;
import com.bmtech.utils.bmfs.MFileWriter;
import com.bmtech.utils.bmfs.MFileWriter.MFileOutputStream;

public class ToCurp {

	MDir mdir;
	MFileWriter w;
	ToCurp() throws IOException{
		mdir = new MDir(new File("i:/mdir-dfcf"), true);
		w = mdir.openWriter();
	}
	public void read() throws IOException{
		File[] fs = new File("i:\\dfcf\\all").listFiles();
		for(File f : fs){
			doFile(f);
		}
		mdir.closeDir();
	}
	int count = 0;
	private void doFile(File f) throws IOException {
		try{
			if(f.isDirectory()){
				File[]fs = f.listFiles();
				for(File fx : fs){
					doFile(fx);
				}
			}else{
				if(f.length() < 4096){
					//				System.out.println("skip too short file " + f +", length=" + f.length());
					return;
				}
				MFile mfile = mdir.createMFileIfPossible(f.getName());
				if(mfile == null){
					System.out.println(count + " skip " + f);

					return;
				}
				FileInputStream fos = new FileInputStream(f);
				w.openMFile(mfile).write(fos).close();
				fos.close();

				count ++;
				if(count % 100 == 0){
					System.out.println(count + " total " + this.mdir.size());
				}
			}
		}finally{
			f.delete();
		}
	}
	public static void main(String[] args) throws IOException {
		ToCurp tc = new ToCurp();
		tc.read();
	}
}
