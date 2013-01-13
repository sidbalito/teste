

import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

public class ListaGrafica extends Canvas{

	private Vector items;//coleção de strings da lista
	private int topIndex;//primeiro item visível
	private int bottomIndex;//ultimo item visível
	private int selectedIndex;//o item atualmente selecionado, -1 significa que nenhum item foi selecionado
	private KeyListener keyListener;
		
	public ListaGrafica(KeyListener keyListener){
		this.keyListener = keyListener;
		items = new Vector();//inicializa a lista de strings
	}
	
	private int validateIndex(int index){
		if((index<0)||(index>items.size())) index=-1;
		return index;
	}
	
	public void setTopIndex(int index){
		topIndex = validateIndex(index);
	}
	
	public void setSelected(int index){
		selectedIndex = validateIndex(index);
	}
	
	public String getString(int index){
		return (String) items.elementAt(index);
	}

	protected void paint(Graphics g) {
		int fontHeight = g.getFont().getHeight();
		int graphicsHeight = g.getClipHeight();
		int y;
		int itemIndex;
		bottomIndex = topIndex + graphicsHeight/fontHeight;
		if(bottomIndex > items.size()) bottomIndex = items.size()-1;
		if(selectedIndex >= 0 && bottomIndex > 0){
			if(selectedIndex < topIndex) topIndex = selectedIndex;
			else if (selectedIndex > bottomIndex)topIndex = validateIndex(selectedIndex - graphicsHeight / fontHeight);
			if(topIndex < 0) topIndex = 0;
		}
		g.setColor(255,255,255);
		g.fillRect(0, 0, g.getClipWidth(), graphicsHeight);
		g.setColor(0);
		for(int i = 0; i<items.size(); i++){
			y = i*fontHeight;
			itemIndex = i + topIndex;
			if((y > graphicsHeight)||(itemIndex>=items.size())) {
				bottomIndex = itemIndex-1;
				break;
			}
			if(itemIndex == selectedIndex) g.drawRect(0, y, g.getClipWidth()-2, fontHeight);
			g.drawString((String) items.elementAt(itemIndex), 2, y, 0);
			
		}
	}

	public void append(String s) {
		items.addElement(s);
	}
	
	public void setItems(Vector items){
		this.items = items;
	}

	protected void keyPressed(int keyCode) {
		int index = getSelected();
		switch(getGameAction(keyCode)){
		case UP: if(index > 0) setSelected(index-1); repaint(); break;
		case DOWN: if(index < size()-1) setSelected(index+1); repaint(); break;
		default: if(keyListener != null) keyListener.keyPressed(keyCode);
		}
	}
	public void setKeyReceiver(KeyListener keyListener){
		this.keyListener = keyListener;
	}

	public int getSelected() {
		return selectedIndex;
	}

	public int size() {
		return items.size();
	}
}

