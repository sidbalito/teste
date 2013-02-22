import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class GraphicTextbox extends Canvas {
	WordWarper warper;

	protected void paint(Graphics g) {
		Font font = g.getFont();
		if(warper == null) warper = new WordWarper(g.getClipWidth(), g.getClipHeight(), g, font);
		
		warper.drawWarped(g, g.getClipWidth(), g.getClipHeight(), 0);
	}
	
	public void drawText(String text){
		if(warper != null) repaint();
		warper.addLine(text);
		repaint();
	}

}
