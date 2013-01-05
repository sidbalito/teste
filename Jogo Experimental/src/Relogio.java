/**
 * Controla o fluxo de tempo do jogo
 * 
 * @author Usuario
 * 
 */
public final class Relogio {
	private static final long UM_SEGUNDO = 1000;
	private static final long UM_MINUTO = 60 * UM_SEGUNDO;
	private static final long TEMPO_MAXIMO = UM_MINUTO;
	private static long fim;
	private static long tempoRestante = TEMPO_MAXIMO;
	private static boolean pausado = true;

	// marca o tempo para o fim
	public static void inicio() {
		fim = System.currentTimeMillis() + tempoRestante;
		pausado = false;
	}

	// retorna o tempo restante para o fim
	public static String tempo() {
		return tempo(tempoRestante());
	}

	public static String tempo(long tempo) {
		if (tempo < 0)
			tempo = 0;
		long segundos = tempo % UM_MINUTO / UM_SEGUNDO;
		long minutos = tempo / UM_MINUTO;
		return (minutos > 9 ? "" : "0") + minutos + ":"
				+ (segundos > 9 ? "" : "0") + segundos;
	}

	// marca o inicio da pausa
	public static void pausa() {
		if (pausado)
			return;
		tempoRestante = fim - System.currentTimeMillis();
		pausado = true;
	}

	// Retorna status do relógio se está em pausa
	public static boolean pausado() {
		return pausado;
	}

	public static boolean tempoEsgotado() {
		return tempoRestante() < 0;
	}

	private static long tempoRestante() {
		long tempo;
		if (pausado)
			tempo = tempoRestante;
		else
			tempo = fim - System.currentTimeMillis();
		return tempo;
	}

	public static void reInicio() {
		tempoRestante = TEMPO_MAXIMO;
		pausado = true;
	}

}
