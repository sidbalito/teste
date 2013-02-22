package example.pim;

import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class ImagePicker extends Canvas {

	private PickerListener listener;
	private Vector icones;
	private int selected, inicial;
	private int w;
	private int h;
	private String text;
	private String subtext;

	public ImagePicker(PickerListener listener, int i, Vector icones) {
		this.listener = listener;
		this.icones = icones;
		inicial = i;
		selected = i;
	}

	protected void paint(Graphics g) {
		w=g.getClipWidth();
		h=g.getClipHeight();
		g.setColor(0, 128, 0);
		g.fillRect(0, 0, w, h);
		g.setColor(255, 255, 255);
		g.drawString(text, w>>1, 0, Graphics.TOP|Graphics.HCENTER);
		g.drawString(subtext, w>>1, g.getFont().getHeight(), Graphics.TOP|Graphics.HCENTER);
		g.drawString("voltar", 0, h, Graphics.BASELINE|Graphics.LEFT);
		g.drawImage((Image)icones.elementAt(selected), w>>1, h>>1, Graphics.HCENTER|Graphics.VCENTER);
	}
	
	protected void keyPressed(int keyCode) {
		super.keyPressed(keyCode);
		if(keyCode == -6) listener.setPicked(inicial);
		int index = getSelected();
		int gameActionCode = getGameAction(keyCode);
		switch(gameActionCode){
		case RIGHT: setSelected(((index > 0)?index:icones.size())-1); repaint(); break;
		case LEFT:  setSelected((index < icones.size()-1)?index+1:0); repaint(); break;
		case FIRE:
				listener.setPicked(selected);
			break;
		default:
			System.out.println(keyCode+" ["+gameActionCode+"]");
		}
		repaint();
	}

	private void setSelected(int i) {
		selected = i;
	}

	public int getSelected() {
		return selected;
	}

	public void setTexts(String nome, String numero) {
		text = nome;
		subtext = numero;
	}

}
