package comum.graficos;

import javax.microedition.lcdui.Graphics;

public class RichStringItem implements RichItem {

	public int paint(Graphics g, Object item, boolean selected) {
		g.setColor(0xFFFFFF);
		g.fillRect(0, 0, g.getClipWidth(), g.getClipHeight());
		int fontHeight = g.getFont().getHeight();
		if(selected)g.setColor(0xFF0000);
		else g.setColor(0xFF);
		g.drawString(item.toString(), 0, 0, 0);
		return fontHeight;
	}

}
