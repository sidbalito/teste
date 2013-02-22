import java.util.Vector;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 * Classe java que gera quebra de linha em um conjunto de strings.
 * Cada string adicionada � tratada como uma linha.
 * H� o limite no n�mero de caracteres por linha e o n�mero de linhas por p�gina.
 * **/
public class WordWarper{
	private Vector warpedText;
	private int row;
	private int cols;
	private int rows;
	private Graphics graphics;
	private Font font;
	private boolean isGraphic;
	private int lostBytes ;
	
	/**
	 * Instancia uma classe de quebra de linhas definindo o n�mero m�ximo de linhas e colunas, 
	 * ou a largura e altura m�xima da tela gr�fica se os par�metros graphics e font forem definidos. 
 * @param cols � o n�mero de carcteres por linha ou a largura m�xima
 * 
 * 
 * 	 * @param rows � o n�mero de linhas ou a altura m�xima da tela
	 */
	public WordWarper(int cols, int rows ){
		setGraphics(cols, rows, graphics, font);
	}
	
	public WordWarper(int cols, int rows, Graphics graphics, Font font){
		setGraphics(cols, rows, graphics, font);
	}

	public void setGraphics(int cols, int rows, Graphics graphics, Font font) {
//		System.out.println(cols);
		this.rows = rows;
		this.cols = cols;
		this.graphics = graphics;
		this.font = font;
		isGraphic = ((graphics != null)&&(font != null));
				
	}
	
	private void init() {
		warpedText = new Vector();
		row = 0;
	}
	/**
	 * Adiciona uma nova string adicionando quebra de linha se necess�rio
	 * @param s � a string que ser� acrescentada.
	 * @return verdadeiro enquanto houver linhas dispon�veis
	 * **/
	public boolean addLine(String s){
//		System.out.println(s);
		int inseridas = addString(s, 0);
		lostBytes = s.length()-inseridas+1;
		return row < getRows();
	}

	/**
	 * Adiciona parte de uma matriz de caracteres, aplicando as quebras de linha.
	 * @param c � a matriz de caracteres que ser� adicionada.
	 * @param start � indice do primeiro caracter a ser adicionado.
	 * @return retorna o �ndice do primeiro caracter que n�o foi adicionado.    
	 * */
	public  int addString(String s, int start) {
		int lineIncr = (isGraphic ? font.getHeight():1);
		int lineLen = 0;
		//Se warpedText � nulo inicializa com init()
		if(warpedText==null) init();
		char[] c = s.toCharArray();
		//inicaliza o contador i com a posi��o inical (start)
		int i = start;
//		System.out.println("s.length(): "+s.length());
		int nextPos=i, linha=i;

		boolean hasNewLine = false;
		do {
			boolean space=false;
			do{
				hasNewLine = c[nextPos]=='\n';
				space = c[nextPos]==' ';
				nextPos++;
			}
			while((nextPos < c.length)&& !hasNewLine && !space);
			if(isGraphic){
				lineLen = font.stringWidth(s.substring(linha, nextPos));
//				System.out.println(cols+":"+lineLen+" - "+s.substring(linha, nextPos));
			}else{
				lineLen = (nextPos-linha);
//				System.out.println("nextPos - linha = lineLen " + nextPos + " - " + linha + " = " + lineLen);  
			}
//			System.out.println("linha  = "+( linha));
			if(lineLen >cols){
				if((i-linha)<1)	i+=cols;
				if(i>=s.length())i = s.length();
				warpedText.addElement(s.substring(linha, i)+'\n');
//				System.out.print(s.substring(linha, i));
				row +=lineIncr;
				nextPos = i;
				linha = nextPos;
			}
			i = nextPos;
		} while ((i<s.length())&&(row<rows));
		if(i>linha)	{
			warpedText.addElement(s.substring(linha, i)+'\n');
			row +=lineIncr;
		}
		//System.out.println(warpedText.toString());
		return i;
		
	}

	/**
	 * Retorna uma matriz de strings com as quebras de linha aplicadas e limpa a defini��o de warpedText.
	 * @return string com as quebras de linha aplicadas.
	 * */
	public  String[] getWarpedLines() {
		if(warpedText==null)return null;
		String[] s = new String[warpedText.size()];
		for(int i = 0; i<warpedText.size();i++){
			s[i] = (String) warpedText.elementAt(i);
		}
		init();
		return s;
	}
	
	/**
	 * Retorna a strings com as quebras de linha aplicadas e limpa a defini��o de warpedText.
	 * @return string com as quebras de linha aplicadas.
	 */
	public  String getWarpedString(){
		StringBuffer s = new StringBuffer();
		for(int i = 0; i<warpedText.size();i++){
			s.append((String) warpedText.elementAt(i));
		}
		init();
		return s.toString();
	}
	
	/**
	 * Define o n�mero m�ximo de caracteres por linha
	 * @param cols � o n�mero m�ximo de colunas
	 * @param rows � o n�mero m�ximo de linhas
	 */
	public  void setLimites(int cols, int rows) {
		this.cols = cols;
		this.rows = rows;
	}
	/**
	 * Retorna o n�mero m�ximo de caracteres por linha
	 * @return o n�mero m�ximo de caracteres por linha
	 */
	public  int getCols() {
		return cols;
	}
	/**
	 * Retorna o n�mero m�ximo de linhas por p�gina
	 * @return o n�mero m�ximo de linhas por p�gina
	 */
	public  int getRows() {
		return rows;
	}

	public  void drawWarped(Graphics g, int x, int y, int anchor){
		String[] s = getWarpedLines();
		if(s==null) return;
		g.setColor(255, 255, 255);
		g.fillRect(0, 0, g.getClipWidth(), g.getClipHeight());
		g.setColor(0);
		for(int i = 0; i < s.length; i++){
			g.drawString(s[i], x, y+i*font.getHeight(), anchor);
		}
	}
	
	public  void drawNextPage(Graphics g, int x, int y, int anchor){
		
		drawWarped(g, x, y, anchor);
	}

	public int lostBytes() {
		return lostBytes;
	}
}
