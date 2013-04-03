import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

import comum.graficos.MessageBox;


public class Tela extends Canvas {

	protected void paint(Graphics g) {
		int cor = 0xE;
		System.out.println(cor-g.getDisplayColor(cor));
		new MessageBox(0, 0, getWidth(), getHeight()).paint(g);
	}

}
