import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

/**
 * Exibe a o menu de opções
 * 
 * @author Sidnei Aparecido Teixeira Batista
 * 
 */
public class Menu extends Canvas {

	private static int menuPos;
	private Jogo jogo;
	private TextoGrafico itensMenu = new TextoGrafico();
	private int alturaTela;
	private int larguraTela;

	public void desenha(Graphics g) {
		alturaTela = g.getClipHeight();
		larguraTela = g.getClipHeight();
		int alturaFonte = g.getFont().getHeight();
		g.setColor(Cores.CINZA);
		g.fillRect(0, 0, g.getClipWidth(), g.getClipHeight());
		if (menuPos < 0)
			menuPos = 0;
		Item item = Item.getFirst();
		int i = 0;
		int meio = g.getClipWidth() >> 1;
		while (item != null) {
			if (item.visivel()) {
				if (item.isSelecionado())
					TextoGrafico.setLetras(2);
				else
					TextoGrafico.setLetras(1);
				try {
					itensMenu.desenhaTexto(meio, i * TextoGrafico.ALT,
							Texto.MIDLE, item, g);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// g.drawString(item.toString(), g.getClipWidth()/2,
				// i*alturaFonte, Graphics.HCENTER|Graphics.TOP);
				i++;
			}
			item = item.getNext();

		}
		g.setClip(0, 0, larguraTela, alturaTela);
		g.setColor(Cores.AMARELO);
		long total = Runtime.getRuntime().totalMemory();
		long usada = total - Runtime.getRuntime().freeMemory();
		long percentual = (usada * 100) / total;
		g.drawString("Em uso: " + (usada >> 10) + "KB (" + percentual + "%)",
				0, alturaTela - alturaFonte, 0);
		TextoGrafico.setLetras(1);
	}

	public Menu(Jogo jogo) {
		this.jogo = jogo;
	}

	protected void paint(Graphics g) {
		desenha(g);
	}

	protected void keyPressed(int keyCode) {
		Item selecionado = Item.selecionado();
		switch (keyCode) {
		case Teclas.ABAIXO://-2
		case Teclas.NUM_8://56
			selecionado.selectNext();
			break;

		case Teclas.ACIMA://-1
		case Teclas.NUM_2:
			selecionado.selectPrev();
			break;

		case Teclas.OK:
		case Teclas.NUM_5:
			jogo.onItem(Item.selecionado());
		case Teclas.ADICIONAL_DIREITA:
		}
			repaint();	
	}

	public void addItem(Item item) {
		Item.addItem(item);
	}

}

/**
 * 
 * @author Sidnei Aparecido Teixeira Batista
 * 
 */

class Item {
	private String texto;
	private boolean visivel = true, disponível = true;
	private Item next, prev;
	private static Item last;
	private static Item first;
	private static Item selecionado;

	public Item(String texto) {
		this.texto = texto;
	}

	public static Item getFirst() {
		return first;
	}

	public void selectPrev() {
		Item item = (prev == null ? last : prev);
		if (!item.disponível | !item.visivel)
			item.selectPrev();
		else
			item.setSelecionado();
	}

	public void selectNext() {
		Item item = (next == null ? first : next);
		if (!item.disponível | !item.visivel)
			item.selectNext();
		else
			item.setSelecionado();
	}

	public static void addItem(Item item) {
		if (item == null)
			return;
		if (last == null) {
			last = item;
		} else {
			last.setNext(item);
		}
		if (first == null)
			first = item;
		last = item;
	}

	public void atualiza(String s) {
		texto = s;
	}

	public String toString() {
		return texto;
	}

	public boolean visivel() {
		return visivel;
	}

	public void mostra() {
		visivel = true;
	}

	public void oculta() {
		visivel = false;
	}

	public Item getNext() {
		return next;
	}

	public void setNext(Item next) {
		this.next = next;
		if (next != null)
			next.prev = this;
	}

	public Item getPrev() {
		return prev;
	}

	public void setPrev(Item prev) {
		this.prev = prev;
		if (prev != null)
			prev.next = this;
	}

	public boolean isSelecionado() {
		return selecionado == this;
	}

	public static Item selecionado() {
		return selecionado;
	}

	public void setSelecionado() {
		if (disponível)
			selecionado = this;
		// else if(next != null) next.setSelecionado();
	}

	public void setBloqueado() {
		disponível = false;
	}

	public void setLiberado() {
		disponível = true;
	}

}

interface MenuListener {
	public void onItem(Item item);

	public boolean iniciado();
}