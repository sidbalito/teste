import java.util.Hashtable;
import java.util.Vector;

import comum.StringsVector;


public class View {
	
	private String nome;
	private Vector grupos = new Vector();
	private Hashtable descricoes = new Hashtable();

	public View(String nome, String grupos) {
		/*
		int i, len = grupos.length();
		i  = grupos.indexOf('[')+1;
		if(i < 0) i = 0;
		while(i < len){
			int pos = grupos.indexOf(',', i);
			if(pos<0)pos = grupos.indexOf(']', i);
			if(pos<0)break;
			this.grupos.addElement(grupos.substring(i, pos));
			i = pos+1;
		}//*/
		this.grupos = new StringsVector(grupos).getVector();  
		this.nome = nome;
	}

	public void addDescricao(String descricao, String grupo2){
		String grupo1 = (String) descricoes.get(descricao);
		if(grupo1 == null){
			descricoes.put(descricao, grupo2);
		} else {
			resolveConflito(grupo1, grupo2, descricao);
		}
	}
	
	public String getGrupo(String descricao){
		String grupo1 = (String) descricoes.get(descricao);
		return grupo1;
	}

	private void resolveConflito(String grupo1, String grupo2, String descricao) {
		grupos.removeElement(grupo2);
	}

	public Vector getGrupos() {
		return grupos;
	}
	
}
