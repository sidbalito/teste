import java.io.IOException;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class TextoGrafico {
	private static final char A = 'A';
	private static final char Z = 'Z';
	private static final char ESPACO = ' ';
	private static final char Num0 = '0';
	private static final char Num9 = '9';
	private static final String SIMBOLOS = "?!.,:~<>[] ";
	static final int LARG = 12;
	final static int ALT = 13;
	private static Image letras2;
	private static Image letras1;
	private static Image letras;

	// private String[] textos = {PRESSIONE_5, PARA_INICIAR};

	public TextoGrafico() {
		try {
			letras1 = Image.createImage("/Letras.png");
			letras2 = Image.createImage("/Letras2.png");
			letras = letras1;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setLetras(int cod) {
		switch (cod) {
		case 2:
			letras = letras2;
			break;
		default:
			letras = letras1;
		}
	}

	protected void paint(Graphics g) {
		try {
			desenhaTextos(g);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void desenhaTextos(Graphics g) throws IOException {
		Texto item = Texto.getFirst();
		while (item != null) {
			if (item.isVisivel())
				desenhaTexto(item.getX(), item.getY(), item.getAlinhamento(),
						item, g);
			item = item.getNext();
		}
	}

	private void desenhaLetra(char letra, int x, int y, Graphics g)
			throws IOException {
		if (letra >= Num0 && letra <= Num9) {
			desenhaNumero(letra, x, y, g);
			return;
		}
		if ((letra < A || letra > Z)) {
			desenhaSimbolo(letra, x, y, g);
			return;
		}
		int n = letra - A;
		int i = n % 10;
		int xp = -LARG * i;
		int j = n / 10;
		int yp = -ALT * j;

		g.setClip(x, y, LARG + 1, ALT);
		g.drawImage(letras, x + xp, y + yp, 0);

	}

	private void desenhaNumero(char algarismo, int x, int y, Graphics g)
			throws IOException {
		if ((algarismo < Num0 || algarismo > Num9)
				&& (int) algarismo != (int) ESPACO) {
			return;
		}

		int n = algarismo - Num0;
		int i = n % 10;
		int xp = -LARG * i - LARG * 11 + 8;
		int j = n / 10;
		int yp = -ALT * j;

		g.setClip(x, y, LARG - 2, ALT);
		g.drawImage(letras, x + xp, y + yp, 0);

	}

	private void desenhaSimbolo(char algarismo, int x, int y, Graphics g)
			throws IOException {
		int n = SIMBOLOS.lastIndexOf(algarismo);
		// System.out.println("Posição: "+n);
		n = n < 0 ? SIMBOLOS.length() : n;
		int i = n % 10;
		int xp = -LARG * i - LARG * 11 + 8;
		int j = n / 10;
		int yp = -ALT - ALT * j;

		g.setClip(x, y, LARG - 2, ALT);
		g.drawImage(letras, x + xp, y + yp, 0);

	}

	public void desenhaTexto(int x, int y, int alinhamento, Object s, Graphics g)
			throws IOException {
		String texto = s.toString().toUpperCase();
		switch (alinhamento) {
		case Texto.MIDLE:
			x -= (texto.length() * LARG) / 2;
			break;
		case Texto.RIGHT:
			x -= texto.length() * LARG;
			break;
		}

		for (int i = 0; i < texto.length(); i++)
			desenhaLetra(texto.charAt(i), x + i * LARG, y, g);
	}

	public static void add(String texto, int x, int y) {
		add(texto, x, y, Texto.LEFT);
	}

	public static void add(String texto, int x, int y, int alinhamento) {
		Texto.add(texto, x, y, alinhamento);
	}

}

class Texto {
	public static final int RIGHT = 2;
	public static final int MIDLE = 1;
	public static final int LEFT = 0;
	private String texto;
	private int x, y, alinhamento;
	private Texto next;
	private static Texto first, last;
	private boolean visivel = true;

	public Texto(String texto, int x, int y, int alinhamento) {
		this.texto = texto;
		this.x = x;
		this.y = y;
		this.setAlinhamento(alinhamento);
	}

	public static void add(String texto, int x, int y) {
		add(texto, x, y, MIDLE);
	}

	public static Texto getFirst() {
		return first;
	}

	public static void add(String[] textos, int x, int y) {
		for (int i = 0; i < textos.length; i++)
			add(textos[i], x, y);
	}

	public static void add(String texto, int x, int y, int alinhamento) {
		Texto item = new Texto(texto, x, y, alinhamento);
		add(item);
	}

	public static void add(Texto item) {
		if (last != null)
			last.next = item;
		last = item;
		if (first == null)
			first = last;
	}

	public int length() {
		if (texto == null)
			return 0;
		return texto.length();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public String toString() {
		return texto;
	}

	public Texto getNext() {
		return next;
	}

	public void setNext(Texto next) {
		next.next = this.next;
		this.next = next;
	}

	public int getAlinhamento() {
		return alinhamento;
	}

	public void setAlinhamento(int alinhamento) {
		this.alinhamento = alinhamento;
	}

	public boolean isVisivel() {
		return visivel;
	}

	public void mostrar() {
		visivel = true;
	}

	public void ocultar() {
		visivel = false;
	}

	public void setTexto(String string) {
		texto = string;
	}

}
