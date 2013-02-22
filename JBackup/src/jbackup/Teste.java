package jbackup;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Teste {
	static final long X = -1024*1024*1024;

	public static void main(String[] args) {
		test();
		//System.out.println(Util.bytesToHex(new byte[]{14, 15, 16, 17, 18}));
		/*
		for(int i = (int) (LIMITE-127L); i < LIMITE; i++)
			firstLen(i);
		System.out.println();//*/
	}
	
	static boolean firstLen(long fileLen) {
		long faixa = fileLen & ~63L;
		long bit = 1L << (fileLen & 63L);
		
		System.out.println("FileLen: "+ fileLen + " - faixa: " + faixa + " = " + Long.toBinaryString(bit));
		return false;
	}
	
	static  void benchMark(){
        int N = 77777777;
        long t;

        {
            StringBuffer sb = new StringBuffer();
            t = System.currentTimeMillis();
            for (int i = N; i --> 0 ;) {
                sb.append("");
            }
            System.out.println(System.currentTimeMillis() - t);
        }

        {
            StringBuilder sb = new StringBuilder();
            t = System.currentTimeMillis();
            for (int i = N; i --> 0 ;) {
                sb.append("");
            }
            System.out.println(System.currentTimeMillis() - t);
        }
		
	}

	private static void test(){
		try {
			new ZIP().processStreams();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
