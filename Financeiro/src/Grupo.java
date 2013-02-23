import java.util.Vector;


public class Grupo implements Serializable {

	private static final char SEPARATOR = ',', EQUAL = '=';
	private Vector items;
	private String name;

	public Grupo(String name, Vector items) {
		this.items = items;
		this.name = name;
	}

	public Grupo() {
	}

	public String toString(){
		StringBuffer sb = new StringBuffer(name);
		int len = sb.length();
		for(int i = 0; i<len; i++){
			sb.append(items.elementAt(i));
			if(i+1 == len)sb.append(SEPARATOR);
		}
		return sb.toString();
	}
	
	public Serializable fromString(String string) {
		Vector items = new Vector();
		int fim = 0, inicio = 0;
		fim = string.indexOf(EQUAL);
		String name = string.substring(0, fim);
		inicio = fim+1;
		while(inicio<string.length()){
			fim = string.indexOf(SEPARATOR, inicio);
			if(fim == -1)fim = string.length();
			items.addElement(string.substring(inicio, fim));
			inicio = fim+1;
		}
		return new Grupo(name, items);
	}

}
