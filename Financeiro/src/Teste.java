import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

import comum.StringsHashtable;
import comum.StringsVector;


public class Teste extends MIDlet {

	public static void startApp(Teste teste) {
		teste.startApp();
	}

	protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
		
	}

	protected void pauseApp() {
		
	}

	protected void startApp() {
		/*Persist p = new Persist();
		try {
			//p.store("visao.Padrão", "{L1=G1, L2=G2}");
			Hashtable ht = new StringsHashtable(p.load("visao.Padrão", 1));
			
			System.out.println(new HashtableVector(ht, HashtableVector.KEYS));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		ht.put("chave1", "valor1");
		ht.put("chave2", "valor2");
		ht.put("chave3", "valor3");//
		try {
			Persist p = new Persist();
			p.store("Teste", 9, "ABC");
			//System.out.println(p.load("Teste", 9));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ht = new StringsHashtable("{A=B}").getHashtable();
		System.out.println("Vetor: "+new StringsVector("").getVector());
		Object element = null;
		for(Enumeration elements = ht.elements(); elements.hasMoreElements(); )
			System.out.println(elements.nextElement());//
		new Hashtable().put("Nome", null);//*/
		String[] RS = RecordStore.listRecordStores();
		for(int i = 0; i<RS.length; i++)
			System.out.println(RS[i]);//*/
		
	}

}
