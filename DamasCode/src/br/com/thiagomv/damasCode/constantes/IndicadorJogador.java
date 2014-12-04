package br.com.thiagomv.damasCode.constantes;

/**
 * Indica um jogador e suas respectivas configurações. As configurações de um
 * jogador são estabelecidas na interface gráfica e atualizadas aqui no momento
 * em que o jogo é iniciado. Um jogador pode estar associado a um algoritmo e
 * uma heurística.
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
	 * Heurística estabelecida para o jogador.
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
