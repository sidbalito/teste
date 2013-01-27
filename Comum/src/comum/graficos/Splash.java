package comum.graficos;

import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Splash extends Canvas {
	private static final int INC_Y = 1;

	Vector scrollImgs = new Vector();

	private String text, subText;

	private int index;

	private int startY;
	
	public Splash(String s, Vector logos) {
		scrollImgs = logos;
		text = s;	
		new Thread(new Runnable() {
			
			public void run() {
				while (true) {
					repaint();
					try {
						Thread.sleep(40);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	public Splash(String s) {
		this(s, null);
	}

	protected void paint(Graphics g) {
		System.out.println(scrollImgs);
		int width = getWidth();
		int height = getHeight();
		int fontHeight = g.getFont().getHeight();
		g.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE));
		g.setColor(0x007FFF);
		g.fillRect(0, 0, width, height);
		if(scrollImgs.size()>0)drawScrollImages(g);
		g.setColor(0xFF);
		g.drawString(text, width>>1, height-(fontHeight<<1), Graphics.BASELINE|Graphics.HCENTER);
		g.drawString(subText, width>>1, height-(fontHeight), Graphics.BASELINE|Graphics.HCENTER);
	}

	private void drawScrollImages(Graphics g) {
		int x = getWidth()>>1,
			y = startY,
			imgHeight = 0,
			index = this.index;
		if((index>=scrollImgs.size()))index = 0;		
		int height = getHeight();
		while(y < height){
			Image img = (Image) scrollImgs.elementAt(index);
			imgHeight = img.getHeight();
			if(startY < -imgHeight)this.index++;
			g.drawImage(img, x, y, Graphics.HCENTER|Graphics.TOP);
			y += imgHeight;
			index++;
			if((index>=scrollImgs.size()))index = 0; 
		}
		startY -= INC_Y;
	}
	public void setText(String s){
		text = s;
	}
	public void setSubtext(String s){
		subText = s;
	}
}
