package mujmail.html.element;

import javax.microedition.lcdui.Graphics;

import mujmail.html.Drawable;
import mujmail.html.Drawable.Point;

public class ScriptElement extends AElement {

	public ScriptElement() {
		super("script");
	}

	public Point draw(Graphics g, int x, int y) {
		return new Point(x, y);
	}

}
