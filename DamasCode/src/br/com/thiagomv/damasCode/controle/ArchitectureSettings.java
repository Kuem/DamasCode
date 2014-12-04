package br.com.thiagomv.damasCode.controle;

/**
 * Agrupa definições globais que são utilizadas por toda a aplicação.
 * 
 * @author Thiago Mendes Vieira
 * 
 *         20/09/2014
 */
public final class ArchitectureSettings {
	/**
	 * Número de linhas do tabuleiro.
	 */
	private static final int NUM_LINHAS = 8;

	/**
	 * Número de colunas do tabuleiro.
	 */
	private static final int NUM_COLUNAS = 8;

	/**
	 * Tempo padrão em que o jogo deve esperar para atualizar uma jogada.
	 */
	private static long DEFAULT_UPDATE_TIME = 500;

	/**
	 * Tempo mínimo em que o jogo deve esperar para atualizar uma jogada.
	 */
	private static long minUpdateTime = DEFAULT_UPDATE_TIME;

	/**
	 * Habilita/Desabilita o log em MomoriaController.
	 */
	private static boolean memoriaControllerLogEnabled = false;

	/**
	 * Retorna o número de linhas do tabuleiro.
	 * 
	 * @return Número de linhas.
	 */
	public static int getNumLinhas() {
		return NUM_LINHAS;
	}

	/**
	 * Retorna o número de colunas do tabuleiro.
	 * 
	 * @return Número de colunas.
	 */
	public static int getNumColunas() {
		return NUM_COLUNAS;
	}

	/**
	 * Retorna o tempo mínimo, em milisegundos, em que o jogo deve esperar para
	 * atualizar uma jogada.
	 * 
	 * @return Tempo em milisegundos.
	 */
	public static long getMinTimeUpdate() {
		return minUpdateTime;
	}

	/**
	 * Estabelece o tempo mínimo, em milisegundos, em que o jogo deve esperar
	 * para atualizar uma jogada.
	 */
	public static void setMinTimeUpdate(long tempo) {
		minUpdateTime = tempo;
	}

	/**
	 * Estabelece o tempo mínimo padrão, em milisegundos, em que o jogo deve
	 * esperar para atualizar uma jogada.
	 */
	public static void resetMinTimeUpdate() {
		minUpdateTime = DEFAULT_UPDATE_TIME;
	}

	public static void logLine(String level, String msg) {
		System.out.println(level.toString() + ": " + msg);
	}

	public static void logLine(String msg) {
		System.out.println(msg);
	}

	public static boolean isMemoriaControllerLogEnabled() {
		return memoriaControllerLogEnabled;
	}
}
