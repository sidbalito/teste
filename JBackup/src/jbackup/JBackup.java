package jbackup;

import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;

import auxiliar.Util;

public class JBackup {
	
	private static final String[] TESTE = {"Teste", "de", "Digest"};

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Base base = new Base(TESTE);
		base.processStreams();
		
/*		Stream.getIn().read(buffer);
		System.out.println(new String(buffer));*/
	}
	
	public static void readStream(InputStream in) throws Exception{
		DigestInputStream din = new DigestInputStream(in, MessageDigest.getInstance("SHA-1"));
		byte[] buffer;// = new byte[din.available()];
		din.read();
		din.read();
		buffer =din.getMessageDigest().digest();
		System.out.println(Util.bytesToHex(buffer));
	}
	public static void readStream2(InputStream in) throws Exception{
		DigestInputStream din = new DigestInputStream(in, MessageDigest.getInstance("SHA-1"));
		byte[] buffer;// = new byte[din.available()];
		din.read();
		buffer =((MessageDigest) din.getMessageDigest().clone()).digest();
		System.out.println(Util.bytesToHex(buffer));
		din.read();
		buffer =din.getMessageDigest().digest();
		System.out.println(Util.bytesToHex(buffer).toUpperCase());
	}
}
