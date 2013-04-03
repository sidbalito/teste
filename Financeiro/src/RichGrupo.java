import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.lcdui.Graphics;

import comum.graficos.RichItem;


public class RichGrupo implements RichItem {

	private Vector lancamentos;
	private Hashtable viewTable;

	public RichGrupo(Vector lancamentos, Hashtable viewTable) {
		this.lancamentos = lancamentos;
		this.viewTable = viewTable;
	}

	public int paint(Graphics g, Object item, boolean selected) {
		int x = 0;
		g.setColor(0);
		g.drawString(item.toString(), x, 0, Graphics.LEFT|Graphics.TOP);
		String valor = Float.toString(soma(item.toString()));
		g.drawString(valor, g.getClipWidth(), 0, Graphics.RIGHT|Graphics.TOP);
		//System.out.println(item + ": "+valor);
		return g.getFont().getHeight();
	}
	
	private float soma(String item){
		float soma = 0;
		for(int i = 0; i < lancamentos.size(); i++){
			Lancamento lancamento = (Lancamento) lancamentos.elementAt(i);
			if(item.equals(viewTable.get(lancamento.getDescricao())))soma += lancamento.getValor();
		}
		return soma;
	}

}
