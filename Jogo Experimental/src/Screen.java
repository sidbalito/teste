import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

/**
 * Classe responsável pela interface do jogo
 * 
 * @author Sidnei
 * 
 * 
 */
public class Screen extends GameCanvas {
	private static final int bloqueado = 0x808080;
	private static final int LINHAS = 6;
	private static final int COLUNAS = 6;
	private static final int QUADRADOS = LINHAS * COLUNAS;
	private static final int ULTIMO = QUADRADOS - 1;
	private static int Rodape;
	private static final int MARGEM = 2;
	private static final int CLAREADOR = 0x050505;
	private static final int FUNDO = 8947848;
	private static final String TXT_FIM = "Fim de jogo";
	private static final String TXT_FASE = "FASE ";
	private static final String ZEROS = "000000";
	private static final String NOVO = "Novo";
	private static final String MENU = "Menu";
	protected static final long CEM = 100;
	private static String[] TXT_PAUSA = { "", "", "pressione 5", "para iniciar" };
	private static Texto[] MSG_PAUSA = { null, null, null, null };
	private static Texto MSG_FIM;
	private int fase;
	private int deslX;
	private int deslY;
	private int pX;
	private int pY;
	private Quadrado[] quadrados = new Quadrado[QUADRADOS];
	private int comprimentoMetas;
	private Jogo midlet;
	private TextoGrafico mensagem = new TextoGrafico();
	private int alturaFonte;
	private int meio;
	private int largura;
	private int altura;
	private Graphics g;

	public Screen(Jogo midlet) {
		super(false);
		this.midlet = midlet;
		novoJogo();
		Relogio.pausa();
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					Screen.this.quadrados[ULTIMO].organiza();
					try {
						Thread.sleep(CEM);
						Screen.this.repaint();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public void novoJogo() {
		Quadrado.novoJogo();
		fase = Quadrado.getFase();
		for (int i = 0; i < LINHAS; i++)
			for (int j = 0; j < COLUNAS; j++) {
				Quadrado direito = i > 0 ? this.quadrados[(i - 1 + j * LINHAS)]
						: null;
				Quadrado inferior = j > 0 ? this.quadrados[(i + (j - 1)
						* COLUNAS)] : null;
				Quadrado esquerdo = i < LINHAS - 1 ? this.quadrados[(i + 1 + j
						* LINHAS)] : null;
				Quadrado superior = j < COLUNAS - 1 ? this.quadrados[(i + (j + 1)
						* COLUNAS)]
						: null;

				this.quadrados[(i + j * COLUNAS)] = new Quadrado(direito,
						inferior, esquerdo, superior);
			}
		Relogio.reInicio();
		if (MSG_FIM != null)
			MSG_FIM.ocultar();
	}

	private void getValues(Graphics g) {
		this.g = g;
		largura = g.getClipWidth();
		altura = g.getClipHeight();
		meio = largura >> 1;
		MSG_FIM = new Texto(TXT_FIM, meio, (altura >> 1)
				- (TextoGrafico.ALT >> 1), Texto.MIDLE);
		MSG_FIM.ocultar();
		Texto.add(MSG_FIM);
		for (int i = 0; i < MSG_PAUSA.length; i++) {
			MSG_PAUSA[i] = new Texto(TXT_PAUSA[i], meio, (altura >> 1)
					- TextoGrafico.ALT * i, Texto.MIDLE);
			Texto.add(MSG_PAUSA[i]);
		}
	}

	public void paint(Graphics g) {
		if (this.g == null)
			getValues(g);
		String pontos = ZEROS + Quadrado.pontos();
		TextoGrafico.add(TXT_FASE + Quadrado.getFase(), 0, 0, Texto.LEFT);
		TextoGrafico.add(pontos.substring(pontos.length() - 6),
				g.getClipWidth(), 0, Texto.RIGHT);
		MSG_PAUSA[0].setTexto(TXT_FASE + Quadrado.getFase());
		if (Relogio.pausado())
			mostraPausa();
		if (alturaFonte == 0)
			alturaFonte = g.getFont().getHeight();
		if (Rodape == 0)
			Rodape = alturaFonte * 2;
		g.setColor(FUNDO);
		g.fillRect(0, 0, g.getClipWidth(), g.getClipHeight());
		g.setColor(Cores.PRETO);
		if ((this.deslX == 0) || (this.deslY == 0)) {
			this.deslX = (g.getClipWidth() / COLUNAS);
			this.deslY = ((g.getClipHeight() - Rodape) / LINHAS);
		}
		for (int i = 0; i < LINHAS; i++) {
			for (int j = 0; j < COLUNAS; j++)
				desenhaQuadrado(g, i, j, this.quadrados[(i + j * COLUNAS)]);
		}
		g.drawRect(pX * this.deslX + MARGEM, pY * this.deslY + MARGEM,
				this.deslX - 4, this.deslY - 4);
		g.drawRect(pX * this.deslX + MARGEM + 1, pY * this.deslY + MARGEM + 1,
				this.deslX - 6, this.deslY - 6);
		desenhaMetas(g);
		g.setColor(Cores.BRANCO);
		if (Quadrado.getFase() > fase)
			proximaFase();
		if (Relogio.tempoEsgotado())
			Quadrado.setFim();
		long total = Runtime.getRuntime().totalMemory();
		long usada = total - Runtime.getRuntime().freeMemory();
		long percentual = (usada * 100) / total;
		g.drawString("Em uso: " + (usada >> 10) + "KB (" + percentual + "%)",
				0, g.getClipHeight() - Rodape, 0);
		TextoGrafico.add(Relogio.tempo(), largura, altura - Rodape
				- TextoGrafico.ALT, Texto.RIGHT);
		this.comprimentoMetas = (g.getClipWidth() >> 1);
		comandos(g, NOVO, MENU);
		if (Quadrado.fim()) {
			ocultaPausa();
			MSG_FIM.mostrar();
		}
		mensagem.paint(g);
	}

	private void mostraPausa() {
		for (int i = 0; i < MSG_PAUSA.length; i++)
			if (MSG_PAUSA[i] != null)
				MSG_PAUSA[i].mostrar();
	}

	private void ocultaPausa() {
		for (int i = 0; i < MSG_PAUSA.length; i++)
			if (MSG_PAUSA[i] != null)
				MSG_PAUSA[i].ocultar();
	}

	private void comandos(Graphics g, String esquerda, String direita) {
		g.drawString(esquerda, 0, g.getClipHeight(), 36);
		g.drawString(direita, g.getClipWidth(), g.getClipHeight(), 40);
	}

	private void desenhaMetas(Graphics g) {
		int topo = g.getClipHeight() - Rodape + MARGEM;
		int inicio = g.getClipWidth() - this.comprimentoMetas;
		int altura = 10;
		g.setColor(Cores.AZUL);
		int comprimento = this.comprimentoMetas * Quadrado.getMeta()
				/ Quadrado.LIMITE - 5;
		g.fillRect(inicio, topo, comprimento, altura);
		topo += altura + MARGEM;
	}

	private void desenhaQuadrado(Graphics g, int i, int j, Quadrado quadrado) {
		float perc = this.deslY * quadrado.getDescendo() / 10;
		int descendo = (int) perc;
		g.setColor(cor(quadrado.getCor(), quadrado.getIdade(),
				quadrado.isBloqueado()));
		g.fillRect(i * this.deslX, j * this.deslY - descendo, this.deslX,
				this.deslY);
		g.setColor(Cores.CINZA);
		g.drawRect(i * this.deslX, j * this.deslY - descendo, this.deslX,
				this.deslY);
		g.setColor(Cores.BRANCO);
		// if ((this.pX == i) && (this.pY == j)) g.drawRect(pX * this.deslX +
		// MARGEM, pY * this.deslY + MARGEM, this.deslX - 4, this.deslY - 4);
	}

	private int cor(int codCor, int idade, boolean bloqueado) {
		int cor;

		switch (codCor) {
		case 1:
			cor = Cores.VERMELHO;
			break;
		case 2:
			cor = Cores.VERDE;
			break;// & mudanca; //65280;
		case 3:
			cor = Cores.AZUL;
			break;// & mudanca; //16711680;
		default:
			cor = Cores.CINZA;
			break;
		}
		if (bloqueado)
			cor |= Screen.bloqueado;
		else
			cor &= (CLAREADOR * idade);

		return cor; // 8947848;
	}

	protected void keyPressed(int keyCode) {
		if (!Quadrado.fim() && !Relogio.pausado()) {
			switch (keyCode) {
			case -2:
			case 56:
				if (this.pY > LINHAS - 2)
					break;
				this.pY += 1;
				break;
			case -1:
			case 50:
				if (this.pY <= 0)
					break;
				this.pY -= 1;
				break;
			case -4:
			case 54:
				if (this.pX > COLUNAS - 2)
					break;
				this.pX += 1;
				break;
			case -3:
			case 52:
				if (this.pX <= 0)
					break;
				this.pX -= 1;
				break;
			case -5:
			case 53:
				if (!this.quadrados[(this.pX + this.pY * COLUNAS)].verifica())
					break;
				this.quadrados[(this.pX + this.pY * COLUNAS)].limpa();
				this.quadrados[ULTIMO].organiza();
			}
		}
		switch (keyCode) {
		case -7:
			midlet.pausa();
			break;
		case -6:
			novoJogo();
			break;
		case -5:
		case 53:
			if (Relogio.pausado()) {
				ocultaPausa();
				Relogio.inicio();
			}
			break;
		default:
		}
		repaint();
	}

	void proximaFase() {
		fase = Quadrado.getFase();
		Relogio.reInicio();
	}

	public static void mensagem() {
		// instancia.mensagem = MSG_PAUSA;
	}
}