package comum.graficos;

import javax.microedition.lcdui.Graphics;

public interface RichItem {
	public int paint(Graphics g, Object item, boolean selected);	
}
