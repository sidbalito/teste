import java.io.IOException;
import java.util.Hashtable;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import comum.graficos.RichItem;


public class RichLancamento implements RichItem {
	private static String CHECKED = "/checked.png";
	private static String UNCHECKED = "/unchecked.png";
	
	private Hashtable tabela;
	private Image checked;
	private Image unchecked;
	private Image icone;

	public RichLancamento(){
		this(null);
	}
	
	public RichLancamento(Hashtable tabela) {
		this.tabela = tabela;
		if(this.tabela == null) return;
		try {
			checked = Image.createImage(CHECKED);
			unchecked = Image.createImage(UNCHECKED);
		} catch (IOException e) {
			// TODO: tratar exceção
			e.printStackTrace();
		}
	}

	public int paint(Graphics g, Object item, boolean selected) {
		if(!(item instanceof Lancamento)) throw new IllegalArgumentException("item deve ser do tipo "+Lancamento.class);
		Lancamento lancamento = (Lancamento) item;
		int x = 0;
		Image icone = null;
		if(tabela != null) {
			if(tabela.containsKey(lancamento.getDescricao())) icone = checked;
			else icone = unchecked;
			//System.out.println(tabela.containsKey(lancamento.getDescricao()));
		}
		int fontHeight = g.getFont().getHeight();
		int itemHeight = fontHeight << 1;
		g.setColor(0xFFFFFF);
		g.fillRect(0, 0, g.getClipWidth(), g.getClipHeight());
		if(icone != null) {
			g.drawImage(icone, 0, 0, 0);
			x = icone.getWidth();
		}
		if(selected) g.setColor(0xFF0000);
		else g.setColor(0xFF);
		g.drawString(lancamento.getDescricao(), x, 0, 0);
		g.drawString(lancamento.printData(), x, fontHeight, 0);
		g.drawString(lancamento.printValor(), g.getClipWidth(), fontHeight, Graphics.RIGHT|Graphics.TOP);
//		g.drawString(lancamento.getDescricao(), x, 0, 0);
		
		return itemHeight;
	}

	public void toggle(Lancamento item, Object grupo) {
		if(tabela == null) return;
		String descricao = item.getDescricao();
		if(tabela.containsKey(descricao)){
			tabela.remove(descricao);
		}
		else tabela.put(descricao, grupo);
	}
}
