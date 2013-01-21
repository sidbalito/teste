import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;


public class ColorPicker extends GameCanvas {


	private int squareWidth;
	private int squareHeight;
	private Point pointer;
	private int div = 3;
	private int divColor = 16777216 >> div ;
	private int cols, rows;

	protected ColorPicker(boolean suppressKeyEvents) {
		super(suppressKeyEvents);
	}
	public void paint(Graphics g) {
		super.paint(g);
		if(squareHeight == 0 | squareWidth == 0) initialize();
		drawSqares(g);
		drawPointer(g);
	}

	private void initialize() {
		cols = 16;
		rows = 16;
		squareWidth = getWidth()/cols;
		squareHeight = getHeight()/rows;
		pointer = new Point(0, 0, squareWidth, squareHeight);
	}
	
	private void drawPointer(Graphics g) {
		g.setColor(pointer.getColor());
		g.drawRect(pointer.getX(), pointer.getY(), pointer.getWidth(), pointer.getHeight());
	}

	private void drawSquare(Graphics g, int i, int j) {
		g.setColor(getColor(i, j));
		int x = i * squareWidth+1;
		int y = j * squareHeight+1;
		g.fillRect(x, y, squareWidth-2, squareHeight-2);
	}

	private int getColor(int i, int j) {
		int k = i * rows + j;
		int rgb = 0;
		//*
		for(int n = 0; n<6;n++){
			int c = k & 1<<n;
			int bits = 0xF<<(4*n);
			if(c>0)rgb |= bits;
			System.out.println(bits);
		}
		/*/
		int r1 = k & 1<<0;
		int r2 = k & 1<<1;
		int g1 = k & 1<<2;
		int g2 = k & 1<<3;
		int b1 = k & 1<<4;
		int b2 = k & 1<<5;
		if(r1>0)rgb|=0xF<<0;
		if(r2>0)rgb|=0xF<<4;
		if(g1>0)rgb|=0xF<<8;
		if(g2>0)rgb|=0xF<<12;
		if(b1>0)rgb|=0xF<<16;
		if(b2>0)rgb|=0xF<<20;//*/
		//System.out.println(k+" = "+rgb);
		return rgb;
	}
	private void drawSqares(Graphics g) {
		for(int i = 0; i < cols; i++)
			for(int j = 0; j < rows; j++)
				drawSquare(g, i, j);
	}

}

class Point{
	private int fX, fHeight, fY, fWidth;
	public Point(int x, int y, int width, int height) {
		fWidth = width;
		fHeight = height;
		fX = x * fWidth;
		fY = y * fHeight;
	}
	public int getColor() {
		// TODO Auto-generated method stub
		return 0;
	}
	public void setPoint(int x, int y){
		
	}
	public int getHeight() {
		return fHeight;
	}
	public int getWidth() {
		return fWidth;
	}
	public int getX() {
		return fX;
	}
	
	public int getY() {
		return fY;
	}
	
}
