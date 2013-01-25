package comum;

import javax.microedition.lcdui.Canvas;

public final class KeyBoard {

	public static final int FIRE_KEY = -5;
	public static final int UP_KEY = -1;
	public static final int DOWN_KEY = -2;
	public static final int KEY_0 = Canvas.KEY_NUM0;
	public static final int KEY_1 = Canvas.KEY_NUM1;
	public static final int KEY_2 = Canvas.KEY_NUM2;
	public static final int KEY_3 = Canvas.KEY_NUM3;
	public static final int KEY_4 = Canvas.KEY_NUM4;
	public static final int KEY_5 = Canvas.KEY_NUM5;
	public static final int KEY_6 = Canvas.KEY_NUM6;
	public static final int KEY_7 = Canvas.KEY_NUM7;
	public static final int KEY_8 = Canvas.KEY_NUM8;
	public static final int KEY_9 = Canvas.KEY_NUM9;
	private static final int TO_LOW = 'a'-'A'; 
	private static final char[][] CHAR_KEYS = new char[][]{
		{' ','0'},									//0
		{'.','?','!',',','-','\'','@',':','/','1'},	//1 
		{'A','B','C','2'},							//2
		{'D','E','F','3'},							//3
		{'G','H','I','4'},							//4
		{'J','K','L','5'},							//5
		{'M','N','O','6'},							//6
		{'P','Q','R','S','7'},						//7
		{'Y','U','V','8'},							//8
		{'W','X','y','Z','9'}						//9
	};
	public static char getChar(int KeyCode, int repeated, boolean UpCase){
		if(KeyCode < KEY_0 | KeyCode > KEY_9) return (char) -1;
		int key = KeyCode-KEY_0;
		repeated = repeated % CHAR_KEYS[key].length;
		char c = CHAR_KEYS[key ][repeated];
		if(!UpCase)c+=TO_LOW;
		return c;
	}
	public static char getAlphaChar(int KeyCode, int repeated, boolean UpCase) {
		if(KeyCode < KEY_0 | KeyCode > KEY_9) return (char) -1;
		int key = KeyCode-KEY_0;
		repeated = repeated % (CHAR_KEYS[key].length-1);
		char c = CHAR_KEYS[key ][repeated];
		if(!UpCase)c+=TO_LOW;
		return c;
	}
}
