import javax.microedition.midlet.MIDlet;


public class Fluxo {
	public static final int RESULT_OK = 0;
	public static final int RESULT_CANCEL = 1;
	static MIDlet midlet;
	public static void setResult(int resultOk) {
		System.out.println("resumindo...");
		midlet.resumeRequest();
	}
	
	public static MIDlet getMIDlet() {
		return midlet;
	}
	
	public static void setMIDlet(MIDlet midlet) {
		Fluxo.midlet = midlet;
	}
	
}
