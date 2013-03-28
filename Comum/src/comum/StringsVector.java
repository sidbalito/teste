package comum;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Converte lista de strings separadas por vígula em um vetor de strings
 * @author Sidnei Aparecido Teixeira Batista
 *
 */
public class StringsVector {
	private static final char CLOSE = ']';
	private static final char COMMA = ',';
	private static final char OPEN = '[';
	public Vector vector = new Vector();

	public StringsVector(String string) {
		int i, len = string.length();
		i  = string.indexOf(OPEN)+1;
		if(i < 0) i = 0;
		while(i < len){
			int pos = string.indexOf(COMMA, i);
			if(pos<0)pos = string.indexOf(CLOSE, i);
			if(pos<0 & i < len)pos = len;
			if(pos<0)break;
			vector.addElement(string.substring(i, pos));
			i = pos+1;
		}
	}
	
	public Vector getVector(){
		return vector;		
	}
}