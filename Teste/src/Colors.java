import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;


public class Colors extends MIDlet {

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {}

	protected void pauseApp() {}

	protected void startApp() throws MIDletStateChangeException {
		/*String s[] = new String[]{"B", "A"};
		System.out.println(s[compare(0, 1, s)]);
		if(s==s)return;//*/
		Enumeration[] enums = new Enumeration[]{
				//new Enum("A", "D", "H"),
				//new Enum("B", "F", "G"),
				new Enum("C", "E", "I")
		};
		mergeEnumerations(enums);
		//while(enums[0].hasMoreElements()) System.out.println(enums[0].nextElement());
		//Display.getDisplay(this).setCurrent(new ColorPicker(false));
	}
	
	void mergeEnumerations(Enumeration[] pEnums){
		int len = pEnums.length;
		Enumeration[] enums = new Enumeration[len];
		String[] candidates = new String[len];
		for(int i = 0; i<len;i++) enums[i] = pEnums[i];
		int i = 0;
		int next = -1;
		int candidate;
		int numCandidates = 0;
		int numEnums = len;
		while(numCandidates > 1 | numEnums > 0){
			//System.out.println(len);
			if(i<numEnums){
				if(candidates[i] == null){
					if(!enums[i].hasMoreElements()){
						numEnums--;
						if(numEnums > 0) enums[i]=enums[numEnums];
						continue;
					}
					candidates[i] = (String) enums[i].nextElement();
					numCandidates++;
				}
			}				
			candidate = i;
			next = compare(next, candidate, candidates);
			//System.out.println(numCandidates);
			i++;
			if( i >= len){
				System.out.println(">"+candidates[next]);
				candidates[next] = null;
				numCandidates--;
				next = -1;
				i = 0;
			}
		}
	}
	
 	public int compare(int next, int candidate, String[] candidates){
		int len = candidates.length;
		if(next < 0 | next >= len){
			if(candidate < 0 | candidate >= len) return -1;
			return candidate;
		}
		if(candidates[candidate] == null) return next;
		int compare = candidates[next].compareTo(candidates[candidate]);
		//System.out.println(compare);
		if(compare>0)return candidate;
		return next;
	}

}

class Enum implements Enumeration{
	private Vector vector = new Vector();
	private int index;

	public void add(String s){
		vector.addElement(s);
	}
	public boolean hasMoreElements() {
		return index < vector.size();
	}

	public Object nextElement() {
		Object elemant = vector.elementAt(index);
		index++;
		return elemant;
	}
	
	public Enum(String s1, String s2, String s3){
		add(s1);
		add(s2);
		add(s3);
	}
}