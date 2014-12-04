package br.com.thiagomv.damasCode.constantes;

/**
 * Indica um jogador e suas respectivas configura��es. As configura��es de um
 * jogador s�o estabelecidas na interface gr�fica e atualizadas aqui no momento
 * em que o jogo � iniciado. Um jogador pode estar associado a um algoritmo e
 * uma heur�stica.
 * 
 * @author Thiago Mendes Vieira
 * 
 *         20/09/2014
 */
public enum IndicadorJogador {
	JOGADOR1, JOGADOR2;

	/**
	 * Algoritmo estabelecido para o jogador.
	 */
	private IndicadorAlgoritmo algoritmo;

	/**
	 * Heur�stica estabelecida para o jogador.
	 */
	private IndicadorHeuristica heuristica;

	public void setAlgoritmo(IndicadorAlgoritmo algoritmo) {
		this.algoritmo = algoritmo;
	}

	public IndicadorAlgoritmo getAlgoritmo() {
		return this.algoritmo;
	}

	public void setHeuristica(IndicadorHeuristica heuristica) {
		this.heuristica = heuristica;
	}

	public IndicadorHeuristica getHeuristica() {
		return this.heuristica;
	}
}
