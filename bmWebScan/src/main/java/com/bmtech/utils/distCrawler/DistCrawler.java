//package com.bmtech.utils.distCrawler;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.TimeUnit;
//
//import com.bmtech.utils.log.BmtLogHelper;
//import com.bmtech.utils.tcp.TCPClient;
//
//public class DistCrawler {
//
//	int distPort;
//	String distHost;
//
//	private final Object lock = new Object();
//	public final BlockingQueue<CrawlContext> crawlContextQueue = new LinkedBlockingQueue<CrawlContext>();
//
//
//	Thread resultGenThread = new Thread(){
//
//		public void run(){
//			while(true){
//				CrawlContext task;
//				try {
//					task = crawlContextQueue.poll(1000, TimeUnit.MICROSECONDS);
//					if(task == null){
//						continue;
//					}
//					WorkHeader wh = new WorkHeader();
//					WorkTask head = new WorkTask();
//					
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//			}
//		}
//	};
//
//	public void regCrawlResult(CrawlContext ct){
//		if(ct != null){
//			this.crawlContextQueue.add(ct);
//		}
//	}
//
//	final BlockingQueue<WorkTask> sendBuffer = new LinkedBlockingQueue<WorkTask>();
//	final BlockingQueue<WorkHeader> receivedBuffer = new LinkedBlockingQueue<WorkHeader>();
//
//	BmtLogHelper log = new BmtLogHelper("cclient");
//	Thread sendThread = new Thread(){
//
//		public void run(){
//			while(true){
//				WorkTask task;
//				try {
//					task = sendBuffer.poll(1000, TimeUnit.MICROSECONDS);
//
//					if(task != null){
//						try {
//							sendTask(task);
//						} catch (IOException e1) {
//							e1.printStackTrace();
//						}finally{
//							task.sendStatus = WorkTask.writeError;
//							synchronized(task){
//								task.notifyAll();
//							}
//						}
//					}
//				} catch (InterruptedException e1) {
//					e1.printStackTrace();
//				}
//			}
//		}
//	};
//
//	private synchronized void sendTask(WorkTask task) throws IOException {
//		NetworkInfo nwi = getNetworkInfo();
//		OutputStream out = nwi.ops;
//		task.sendStatus = WorkTask.sending;
//		out.write(task.toBytes());
//		out.flush();
//		task.sendStatus = WorkTask.waitingResponse;
//	}
//
//	class NetworkInfo{
//		TCPClient client;
//		InputStream ips;
//		OutputStream ops;
//		byte[] checker;
//		boolean closed = false;
//		public void close() {
//			try{
//				this.closed = true;
//				TCPClient c = client;
//				if(c != null){
//					c.close();
//				}
//			}finally{
//				client = null;
//			}
//		}
//
//	}
//
//	NetworkInfo crt;
//	private NetworkInfo getNetworkInfo() {
//		blockUtilGetConnected();
//		return crt;
//	}
//
//	private synchronized void blockUtilGetConnected() {
//		if(isCrtGoodStatus()){
//			return;
//		}else{
//			//FIXME create a good connection
//			closeSocket(crt);
//			crt = null;
//			return;
//		}
//
//	}
//
//	private boolean isCrtGoodStatus() {
//		try{
//			if(crt != null){
//				if(crt.client != null){
//					if(!crt.client.getSocket().isClosed()){
//						return true;
//					}
//				}
//			}
//		}catch(Exception e){
//			log.error(e, "when check status for %s", crt);
//		}
//		return false;
//	}
//
//	private  void closeSocket(NetworkInfo ni) {
//		if(ni != null){
//			synchronized(lock){
//				ni.close();
//			}
//		}
//	}
//
//	Thread receiveThread = new Thread(){
//		public void run(){
//			while(true){
//				NetworkInfo ni = getNetworkInfo();
//				InputStream ips = ni.ips;
//				WorkHeader wi = new WorkHeader();
//				try {
//					wi.initFromInputStream(ips, ni.checker);
//					receivedBuffer.add(wi);	
//				} catch (Exception e) {
//					e.printStackTrace();
//					closeSocket(ni);
//					continue;
//				}
//			}
//		}
//	};
//
//	Thread receivedMessageConsumer = new Thread(){
//		public void run(){
//			while(true){
//				try {
//					WorkHeader wi = receivedBuffer.poll(1000, TimeUnit.MICROSECONDS);
//					if(wi == null){
//						continue;
//					}
//
//					if(wi.taskType() == WorkHeader.SERVER_TELL_STATUS){
//
//					}else if(wi.taskType() == WorkHeader.SERVER_TELL_URLS){
//
//					}else{
//						log.error("unknown workType %s", wi.taskType());
//					}
//
//				} catch (Exception e) {
//					log.error(e, "receivedMessageConsumer get!");
//				}
//
//			}
//		}
//	};
//}
