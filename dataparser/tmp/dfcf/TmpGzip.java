package dfcf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.zip.GZIPOutputStream;

public class TmpGzip extends Thread{
	static final File base = new File(Env.tmpFile, "all");
	LinkedBlockingQueue<File>lst = new LinkedBlockingQueue<File>();

	static File newFile(File f){
		String name = f.getName();
		int hashCode = name.hashCode();
		int[] par = new int[]{
				hashCode & 0x00ff0000,
				hashCode & 0x0000ff00,
				hashCode & 0x00000ff
		};
		File retDir = new File(base, par[0] +"/" + par[1] +"/" + par[2] +"/" );
		if(!retDir.exists()){
			retDir.mkdirs();
		}
		return new File(retDir, name + ".gz");
	}

	byte[] buf = new byte[1024 * 32];
	public void run(){
		while(true){

			try {
				File f = this.lst.take();
				if(f == null){break;}
				gzip(f);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	void gzip(File f) throws IOException, IOException{
		System.out.println("gzip " + f.getName());
		File newFIle = newFile(f);
		System.out.println("gzip " + f.getName() + " to " + newFIle.getName());
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		GZIPOutputStream ops = new GZIPOutputStream(bos);
		InputStream ips = new FileInputStream(f);
		while(true){
			int r = ips.read(buf);
			if(r == -1){
				break;
			}else{
				ops.write(buf, 0, r);
			}
		}
		ips.close();
		ops.close();
		FileOutputStream fos = new FileOutputStream(newFIle);
		fos.write(bos.toByteArray());
		fos.close();

		System.out.println(String.format("from %s, to %s, about %s, gz %s",
				f.length(), newFIle.length(), (double)newFIle.length() / f.length(), newFIle.getAbsoluteFile()));
		if(!f.delete()){
			System.out.println("delete fail!");
		}else{
			System.out.println("ok delete " + f);
		}
	}
	public static void main(String[] args) throws IOException {
		File []f = base.listFiles();
		final TmpGzip[] t = new TmpGzip[]{
				new TmpGzip(),
				new TmpGzip(),
				new TmpGzip(),
				new TmpGzip(),
		};
		for(int x = 0; x < f.length; x ++){
			if(f[x].isDirectory()){
				continue;
			}
			t[x % 4].lst.add( f[x]);
		}
		for(	TmpGzip tg : t){
			tg.start();
		}
		new Thread(){
			public void run(){
				while(true){
					int x = 0;
					for(	TmpGzip tg : t){
						x += tg.lst.size();
					}
					System.out.println("----------------->left " + x);
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
}
