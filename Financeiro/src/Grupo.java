import java.util.Vector;


public class Grupo implements Serializable {

	private static final char SEPARATOR = ';', EQUAL = '=';
	private Vector items = new Vector();
	private String name;

	public Grupo(String name, String descricoes) {
		int i = 0, len = descricoes.length();
		while(i < len){
			int pos = descricoes.indexOf(SEPARATOR, i);
			System.out.println(pos);
			if(pos<0)break;
			this.items.addElement(descricoes.substring(i, pos));
			i = pos+1;
		}
		this.name = name;
	}

	public Grupo() {
	}//*/
	
	public Vector getDescricoes(){
		return items;		
	}

	public String toString(){
		StringBuffer sb = new StringBuffer();
		int len = items.size();
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
		return null;// new Grupo(name, items);
	}
	
	

}
