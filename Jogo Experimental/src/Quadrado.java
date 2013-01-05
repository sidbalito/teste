import java.util.Random;

public class Quadrado {
	private static final int RISCO_MINIMO = 2;
	private static int risco = RISCO_MINIMO;
	private static final int CEM_PORCENTO = 100;
	public static final int VAZIO = 0;
	private static final int SUPERIOR = 1;
	private static final int INFERIOR = 7;
	private static final int ESQUERDO = 3;
	private static final int DIREITO = 5;
	static final int LIMITE = 300;
	private static final long IDADE_MAXIMA = 510;
	private static final long IDADE_MINIMA = 210;
	private static final long UM_SEGUNDO = 1000;
	private static int cores = 3;
	private static Random aleatorio = new Random();
	private int cor;
	private Quadrado[] vizinhos = new Quadrado[8];
	private boolean isTop;
	private static int num;
	private int n;
	private int descendo = 0;
	private static int pontos;
	private static int metas;
	private static boolean fim = true;
	private static int incPonto = 1;
	private static int incMeta = 30;
	private static int fase = 1;
	private long idade = IDADE_MAXIMA;
	private long tempo = System.currentTimeMillis();
	private long redutorIdade = 1;
	private int bloqueado;
	private int bloqueioMaximo = 1;
	private boolean desbloqueado;

	public Quadrado() {
		metas = 0;
		this.cor = novo();
	}

	public Quadrado(Quadrado direito, Quadrado inferior, Quadrado esquerdo,
			Quadrado superior) {
		this();
		num += 1;
		this.n = num;
		this.vizinhos[5] = direito;
		this.vizinhos[7] = inferior;
		if (direito != null)
			direito.vizinhos[3] = this;
		if (inferior != null)
			inferior.vizinhos[1] = this;
	}

	public boolean verifica() {
		if (isBloqueado()) {
			return false;
		}
		if (vizinhos(this.cor) > 1)
			return true;
		if ((this.vizinhos[SUPERIOR] != null) && (vizinho(SUPERIOR, this.cor))
				&& (this.vizinhos[SUPERIOR].vizinhos(this.cor) > 1))
			return true;
		if ((this.vizinhos[ESQUERDO] != null) && (vizinho(ESQUERDO, this.cor))
				&& (this.vizinhos[ESQUERDO].vizinhos(this.cor) > 1))
			return true;
		if ((this.vizinhos[INFERIOR] != null) && (vizinho(INFERIOR, this.cor))
				&& (this.vizinhos[INFERIOR].vizinhos(this.cor) > 1))
			return true;
		if ((this.vizinhos[DIREITO] != null) && (vizinho(DIREITO, this.cor))
				&& (this.vizinhos[DIREITO].vizinhos(this.cor) > 1))
			return true;
		return false;
	}

	public int cor() {
		return 0;
	}

	public void limpa() {
		if (metas < LIMITE)
			metas += incMeta;
		limpa(cor);
	}

	private void limpa(int cor) {
		if (cor == 0)
			return;
		pontos += incPonto;
		// if(!isBloqueado())
		this.cor = 0;
		limpa(SUPERIOR, cor);
		limpa(INFERIOR, cor);
		limpa(DIREITO, cor);
		limpa(ESQUERDO, cor);
		if (testaMetas())
			proximaFase();
	}

	private boolean testaMetas() {
		if (metas < LIMITE)
			return false;
		return true;
	}

	private void limpa(int vizinho, int cor) {
		if (this.vizinhos[vizinho] == null)
			return;
		if (this.vizinhos[vizinho].cor == 0)
			return;

		if (this.vizinhos[vizinho].cor == cor) {
			if (this.vizinhos[vizinho].isBloqueado()) {
				this.vizinhos[vizinho].desbloqueia();
				return;
			}
			this.vizinhos[vizinho].limpa(cor);
			this.vizinhos[vizinho].cor = 0;
		}
	}

	public void organiza() {

		Quadrado quadrado = this;
		do {
			quadrado.desce();
			quadrado = quadrado.vizinhos[5];
		} while (quadrado != null);
	}

	private void desce() {
		if (desbloqueado) {
			bloqueado--;
			desbloqueado = false;
		}
		if (this.descendo > 0)
			decDescendo();
		if (this.vizinhos[INFERIOR] != null) {
			if (this.cor == 0) {
				this.cor = this.vizinhos[INFERIOR].cor;
				this.vizinhos[INFERIOR].cor = 0;
				this.descendo = 9;
				this.idade = this.vizinhos[INFERIOR].idade;
				bloqueado = vizinhos[INFERIOR].bloqueado;
			}
			this.vizinhos[INFERIOR].desce();
		} else if (this.cor == 0) {
			this.cor = novo();
			this.descendo = 9;
		}
		if (this.cor == 0) {
		}
	}

	private int novo() {
		idade = IDADE_MAXIMA;// System.currentTimeMillis();
		bloqueado = aleatorio.nextInt(CEM_PORCENTO) < risco ? bloqueioMaximo
				: 0;
		return aleatorio.nextInt(cores) + 1;
	}

	private int vizinhos(int cor) {
		int conta = 0;
		if (vizinho(SUPERIOR, cor))
			conta++;
		if (vizinho(ESQUERDO, cor))
			conta++;
		if (vizinho(INFERIOR, cor))
			conta++;
		if (vizinho(DIREITO, cor))
			conta++;
		return conta;
	}

	private boolean vizinho(int vizinho, int cor) {
		if (this.vizinhos[vizinho] == null)
			return false;
		if (this.vizinhos[vizinho].isBloqueado())
			return false;
		return this.vizinhos[vizinho].cor == cor;
	}

	public void setTop(boolean isTop) {
		this.isTop = isTop;
	}

	public boolean isTop() {
		return this.isTop;
	}

	public int getCor() {
		return this.cor;
	}

	public void setCor(int i) {
		this.cor = i;
	}

	int num(int vizinho) {
		if (this.vizinhos[vizinho] != null)
			return this.vizinhos[vizinho].n;
		return -1;
	}

	public int num() {
		return this.n;
	}

	public void decDescendo() {
		this.descendo -= 1;
	}

	public int getDescendo() {
		return this.descendo;
	}

	public static int pontos() {
		return pontos;
	}

	public static int getMeta() {
		return metas;
	}

	public static boolean fim() {
		return fim;
	}

	public static void novoJogo() {
		pontos = 0;
		fim = false;
		metas = 0;
		fase = 1;
	}

	public static void setFim() {
		Controle.salva(Quadrado.pontos());
		Relogio.pausa();
		fim = true;
	}

	public void proximaFase() {
		incPonto += 3;
		incMeta = incMeta - 1;
		fase += 1;
		risco = RISCO_MINIMO * fase;
		metas = 0;
	}

	public static int getFase() {
		return fase;
	}

	public int getIdade() {
		if (!Relogio.pausado() & !isBloqueado()) {
			long agora = System.currentTimeMillis();
			if (agora - tempo > UM_SEGUNDO) {
				tempo = agora;
				idade -= redutorIdade;
			}
			if (idade < IDADE_MINIMA)
				setFim();
		}
		return (int) (idade / 10);
	}

	public boolean isBloqueado() {
		return bloqueado > 0;
	}

	public void desbloqueia() {
		desbloqueado = true;
	}

}