package example.pim;

import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.pim.PIMException;
import javax.microedition.pim.PIMItem;
import javax.microedition.pim.PIMList;

public class ListsMerger implements Runnable{
	private static Vector lists = new Vector();
	private static Vector strings = new Vector();
	private int index;
	private static Contatos contatos ;
	private PIMItem item;
	private static int numListas;
	
	public static void setContatos(Contatos contatos, int numListas ){
		ListsMerger.contatos = contatos;
		ListsMerger.numListas = numListas;
	}
	
	public ListsMerger(PIMList list) {
		index = lists.size();
		lists.addElement(list);
		strings.addElement(null);
	}
	
	public void run() {
		try {
			while(addItem());
		} catch (PIMException e) {
			e.printStackTrace();
		}
	}
	
	private boolean addItem() throws PIMException{
		Enumeration list = ((PIMList) lists.elementAt(index)).items();
		if(strings.size() < numListas) return true;
		String next = new String("A");
		if(strings.elementAt(index)==null) strings.setElementAt(contatos.getString(item), index);
		for(int i = 0; i < strings.size(); i++){
			Object object = strings.elementAt(i);
			System.out.println(object);
			if(object == null) return true;
			next = compara(object, next); 
		}
		if(strings.elementAt(index)==null) strings.setElementAt(contatos.getString(item), index);
		System.out.println("addItem: "+next);
		contatos.addNomeContato(next, contatos.getImage(index));
		if(!list.hasMoreElements())	return contatos.hasMoreStrings();
		return false;
	}

	private String compara(Object elementAt, String next) {
		int compara = next.compareTo(elementAt.toString());
		if(compara > 0) return next;
		return elementAt.toString();
	}
}
