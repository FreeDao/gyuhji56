package test;

public class t {
	static class QEEvent{

		public static Object after(Runnable a) {
			return null;
		}
		
	}
	Runnable a,b,c,d;
	//a#d
	//a-->b-->c
	public void regist(){
		Object ab = registParaRun(a,b);
		result(ab);
		registerAfterRun(a, c);
		registerAfterRun(c, d);
	}
	private void result(Object ab) {
		// TODO Auto-generated method stub
	}
	private void registerAfterRun(Runnable a2, Runnable c2) {
		// TODO Auto-generated method stub
	}
	private Object registParaRun(Runnable ...a) {
		// TODO Auto-generated method stub
		return null;
	}
}
