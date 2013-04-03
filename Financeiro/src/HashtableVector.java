import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


public class HashtableVector extends Vector {

	static final int KEYS = 2;
	public static final int VALUES = 1;

	public HashtableVector(Hashtable ht, int tipo) {
		switch(tipo){
			case VALUES: values(ht); break;
			case KEYS: keys(ht); break;
		}
	}

	private void keys(Hashtable ht) {
		Enumeration keys = ht.keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			if(contains(key)) continue;
			addElement(key);			
		}
		
	}

	private void values(Hashtable ht) {
		Enumeration values = ht.elements();
		while (values.hasMoreElements()) {
			Object element = values.nextElement();
			if(contains(element)) continue;
			addElement(element);			
		}
			
	}


}
