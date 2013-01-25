package comum.graficos;


import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import comum.KeyBoard;



public class ListaGrafica extends Canvas{

	private static final int CLAREADOR = 0x404040;
	private Vector items, icones;//coleção de strings da lista
	private int topIndex;//primeiro item visível
	private int bottomIndex;//ultimo item visível
	private int selectedIndex;//o item atualmente selecionado, -1 significa que nenhum item foi selecionado
	private ListaListener listaListener;
	private int graphicsHeight;
	private int graphicsWidth;
	private int fBkColor = 0xFFFFFF;
	private int fColor = 0;
	private Font font;
	private int detailColor = 0x008000;
	private boolean ordered;
		
	public ListaGrafica(ListaListener listaListener){
		this.listaListener = listaListener;
		items = new Vector();//inicializa a lista de strings
		icones = new Vector();
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
		if(items.size()>0) return (String) items.elementAt(index);
		return ""; 
	}

	protected void paint(Graphics g) {
		if(g.getFont() != font)g.setFont(font);
		int itemHeight = 32, fontHeight = font.getHeight();
		if(graphicsHeight == 0 || graphicsWidth== 0) {
			graphicsHeight = g.getClipHeight();
			graphicsWidth = g.getClipWidth();
		}
		
		int y = 0;
		int itemIndex;
		int maxItensTela = graphicsHeight/itemHeight-1;
		bottomIndex = topIndex + maxItensTela;
		if(bottomIndex > items.size()) bottomIndex = items.size()-1;
		if(selectedIndex >= 0 && bottomIndex > 0){
			if(selectedIndex < topIndex) topIndex = selectedIndex;
			else if (selectedIndex > bottomIndex)topIndex = validateIndex(selectedIndex - maxItensTela);
			if(topIndex < 0) topIndex = 0;
		}
		g.setColor(fBkColor);
		g.fillRect(0, 0, graphicsWidth, graphicsHeight);
		g.setColor(fColor);
		if(items.size()<=0)return;
		int selctedY = 0 ;
		int selItemIndex = 0;
		for(int i = 0; i<items.size(); i++){
			itemIndex = i + topIndex;
			if((y+itemHeight > graphicsHeight)||(itemIndex>=items.size())) {
				bottomIndex = itemIndex-1;
				break;
			}
			if(itemIndex == selectedIndex) {
				selItemIndex  = itemIndex;
				selctedY = y;
			}
			g.drawString((String) items.elementAt(itemIndex), 2, y, 0);
			Image icone = (Image) icones.elementAt(selItemIndex);
			if(icone != null) g.drawImage((Image) icones.elementAt(itemIndex), graphicsWidth, y, Graphics.TOP|Graphics.RIGHT);
			y += itemHeight;
		}
		y = selctedY;
		g.setColor(fBkColor|CLAREADOR);
		g.fillRect(0, y, graphicsWidth-2, itemHeight-1);
		String detail = listaListener.getDetail(selItemIndex);
		g.setColor(fColor);
		if(detail != ""){					
			int y1 = y+(fontHeight>>2);
			y1 +=((selectedIndex >= bottomIndex)? -fontHeight:fontHeight);
			g.setColor(detailColor);
			g.fillRect(5, y1 , font.stringWidth(detail), fontHeight);
			g.setColor(fColor);
			g.drawRect(5, y1 , font.stringWidth(detail), fontHeight);
			g.drawString(detail, 5, y1, 0);
		}
		g.drawString((String) items.elementAt(selItemIndex), 2, y+(itemHeight>>1)-(fontHeight>>1), Graphics.TOP|Graphics.LEFT);
		Image icone = (Image) icones.elementAt(selItemIndex);
		if(icone != null) g.drawImage(icone, graphicsWidth, y, Graphics.TOP|Graphics.RIGHT);
		
	}

	public void append(String s, Image icone) {
		if(ordered){
			int index = getInsertPos(s);
			
			items.insertElementAt(s, index);
			icones.insertElementAt(icone, index);
		} else {
			items.addElement(s);
			icones.addElement(icone);
		}
	}
	
	private int getInsertPos(String s) {
		if(items.size() < 1) return 0;
		int startAt = items.size()/2;
		return getInsertPos(s, startAt);
	}

	private int getInsertPos(String s, int startAt) {
		int nextPos = compare(s, (String) items.elementAt(startAt));
		switch (nextPos) {
		case 0: return startAt;
		case 1: 
			startAt += (items.size()-startAt)/2;
		case -1:
			startAt = startAt/2;
		}
		return getInsertPos(s, startAt);
	}

	private int compare(String s, String elementAt) {
		int compare = s.compareTo(elementAt);
		return compare/(compare & 0xEFFFFFFF);
	}

	public void setItems(Vector items){
		this.items = items;
	}

	protected void keyPressed(int keyCode) {
		int index = getSelected();
		switch(keyCode){
		case KeyBoard.UP_KEY: if(index > 0) setSelected(index-1); repaint(); break;
		case KeyBoard.DOWN_KEY: if(index < size()-1) setSelected(index+1); repaint(); break;
		default: if(listaListener != null) listaListener.keyPressed(keyCode);
		}
	}
	
	protected void keyRepeated(int keyCode) {
		super.keyRepeated(keyCode);
		keyPressed(keyCode);
	}
	public void setKeyReceiver(ListaListener keyListener){
		this.listaListener = keyListener;
	}

	public int getSelected() {
		return selectedIndex;
	}

	public int size() {
		return items.size();
	}

	public void setImage(int selected, Image image) {
		icones.setElementAt(image, selected);
	}
	
	public void setBkColor(int color){
		fBkColor = color;
	}
	public void setColor(int color){
		fColor = color;
	}
	
	public void setColor(int bkColor, int color) {
		fBkColor = bkColor;
		fColor = color;
	}
	
	public void setFont(Font font){
		this.font = font;
	}

	public boolean isOrdered() {
		return ordered;
	}

	public void setOrdered(boolean ordered) {
		this.ordered = ordered;
	}

	public void setTopSelected(int index) {
		topIndex = index;
		setSelected(index);
	}
}


