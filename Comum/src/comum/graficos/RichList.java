package comum.graficos;


import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import comum.KeyBoard;



public class RichList extends Canvas implements Drawable, Commandable{

	protected Vector items, icones;//coleção de strings da lista
	private int topIndex;//primeiro item visível
	private int bottomIndex;//ultimo item visível
	private int selectedIndex;//o item atualmente selecionado, -1 significa que nenhum item foi selecionado
	protected ListListener listaListener;
	private int graphicsHeight;
	private int graphicsWidth;
	private int fBkColor = 0xFFFFFF;
	private int fColor = 0;
	private Font font;
	private Image imgItem;
	private RichItem richListItem;
	private Comparator comparator;
	private Image img;
	
		
	public RichList(ListListener listaListener, Vector items, RichItem richListItem){
		this.listaListener = listaListener;
		this.richListItem = richListItem;
		if(items != null) this.items = items;
		else items = new Vector();
		icones = new Vector();
	}
	
	private int validateIndex(int index){
		if((index<0)||(index>items.size())) index=-1;
		return index;
	}
	
	private void setTopIndex(int index){
		topIndex = validateIndex(index);
	}
	
	public void setSelected(int index){
		selectedIndex = validateIndex(index);
	}
	
	public Object getItem(int index){
		if(items.size()>0) return items.elementAt(index);;
		return null;
	}

	public void paint(Graphics g) {
		//Usa a fonte se a estiver definida
		if(font == null)font = g.getFont();
		else g.setFont(font);
		//pega as medidas da tela
		graphicsWidth = g.getClipWidth();
		graphicsHeight = g.getClipHeight();
		//Cria a imagem que será usada para desenhar cada item
		if(imgItem == null) imgItem = Image.createImage(graphicsWidth, graphicsHeight<<1);
		if(img == null) img = Image.createImage(graphicsWidth, graphicsHeight);;

		g.setColor(fBkColor);
		g.fillRect(0, 0, graphicsWidth, graphicsHeight);
		g.setColor(fColor);
		Graphics gr = imgItem.getGraphics();
		gr.setColor(fBkColor);
		gr.fillRect(0, 0, graphicsWidth, graphicsHeight);
		gr.setColor(fColor);
		
		int x = 0, y = 0;
		int bottom = graphicsHeight;		
		if(selectedIndex < topIndex)topIndex = selectedIndex;
		if(items == null) items = new Vector();
		for(int i = topIndex; i<items.size(); i++){
			y = drawItem(imgItem.getGraphics(), i, x, y, false);
			if(selectedIndex==i){
				if(y > graphicsHeight){
					bottomIndex = i;
					bottom = y;
				}
			}
			if(y > graphicsHeight & bottomIndex == i){
				bottomIndex = i;
				topIndex++;
				break;
			}
		}
		g.drawImage(imgItem, 0, graphicsHeight-bottom, 0);
		
	}
	
	public void append(Object s, Image icone) {
		if(isOrdered()){
			int index = getInsertPos(s);
			items.insertElementAt(s, index);
			icones.insertElementAt(icone, index);
		} else {
			items.addElement(s);
			icones.addElement(icone);
		}
	}
	
	private int getInsertPos(Object s) {
		if(items.size() < 1) return 0;
		int startAt = items.size()/2;
		return getInsertPos(s, startAt);
	}

	private int getInsertPos(Object s, int startAt) {
		int nextPos = comparator.compare(s, (String) items.elementAt(startAt));
		switch (nextPos) {
		case 0: return startAt;
		case 1: 
			startAt += (items.size()-startAt)/2;
		case -1:
			startAt = startAt/2;
		}
		return getInsertPos(s, startAt);
	}


	public void setItems(Vector items){
		this.items = items;
	}

	protected void keyPressed(int keyCode) {
		System.out.println("KeyCode: "+keyCode);
		int index = getSelected();
		switch(keyCode){
		case KeyBoard.UP_KEY: if(index > 0) setSelected(index-1); repaint(); break;
		case KeyBoard.DOWN_KEY: if(index < size()-1) setSelected(index+1); repaint(); break;
		case KeyBoard.FIRE_KEY: {
			if(listaListener != null)listaListener.clickItem(this, index);
		}break;
		}
	}
	
	protected void keyRepeated(int keyCode) {
		//keyPressed(keyCode);
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
		return comparator != null;
	}

	public void setComparator(Comparator comparator) {
		this.comparator =comparator;
	}

	public void setTopSelected(int index) {
		topIndex = index;
		setSelected(index);
	}
	
	private int drawItem(Graphics g, int i, int x, int y, boolean before){
		//System.out.println("Indíce:"+i);
		Graphics itemGraphics = img.getGraphics();
		itemGraphics.setColor(0xFFFFFF);
		itemGraphics.fillRect(0, 0, graphicsWidth, graphicsHeight);
		int imgHeight = richListItem.paint(itemGraphics, items.elementAt(i), i == selectedIndex);
		g.setClip(x, y, graphicsWidth, imgHeight);
		g.drawImage(img, x, y, 0);
		y+=imgHeight;
		return y;
	}
	
	public RichItem getRichItem(){
		return richListItem;
	}

	public void removeItem(int selected) {
		items.removeElementAt(selected);
	}
}


