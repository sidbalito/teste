package comum.graficos;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class MessageBox implements Drawable {

	private Graphics g;
	private String title = "Titúlo";
	private String text = "Texto";
	private int x;
	private int y;
	private int width;
	private int height;
	private int titleHeight = 20 ;
	private int backColor = 0xFFFFFF;
	private int foreColor = 0x000000;
	private int titleBackColor = 0x808080;
	private int titleForeColor = 0x000000;
	private Font titleFont, font;

	public MessageBox() {
		
	}
	
	public MessageBox(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void paint(Graphics g) {
		if(this.g == null)this.g = g;
		int screenWidth = g.getClipWidth();
		int screenHeight = g.getClipHeight();
		if(width == 0) width = g.getClipWidth();
		if(height == 0) height = g.getClipHeight();
		if(titleFont == null) titleFont = g.getFont();
		if(font == null) font = g.getFont();
		drawTitle();
		drawText();
		g.setClip(0, 0, screenWidth, screenHeight);
	}

	
	public void repaint() {
		if(g != null)paint(g);
	}

	
	private void drawTitle() {
		titleHeight = titleFont.getHeight();
		g.setFont(titleFont);
		fillRect(x, y, width, titleHeight, titleBackColor, titleForeColor);
		g.drawString(title, width >> 1, y, Graphics.TOP|Graphics.HCENTER);
	}

	
	private void drawText() {
		g.setFont(font);
		fillRect(x, titleHeight, width, height, backColor, foreColor);
		g.drawString(text, width >> 1, titleHeight, Graphics.TOP|Graphics.HCENTER);
	}

	
	private void fillRect(int x, int y, int width, int height,
			int backColor, int foreColor) {
		g.setColor(backColor);
		g.fillRect(x, y, width, height);
		g.setColor(foreColor);
	}

}
