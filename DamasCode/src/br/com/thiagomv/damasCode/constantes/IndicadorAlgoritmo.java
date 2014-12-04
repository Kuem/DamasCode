package br.com.thiagomv.damasCode.constantes;

/**
 * Indica o tipo de algor�tmo que o jogador implementa. Os tipos de algoritmos
 * definidos aqui ser�o disponibilizados no menu de configura���es de cada
 * jogador. O tipo "HUMANO" � um tipo especial que j� est� implementado pela
 * arquitetura do jogo e n�o pode ser removido. Quando este tipo � definido para
 * o jogador, o mesmo n�o ter� uma intelig�ncia artificial, mas o controle ser�
 * dado a um jogador real que interage com a interface gr�fica.
 * 
 * @author Thiago Mendes Vieira
 * 
 *         20/09/2014
 */
public enum IndicadorAlgoritmo {
	/**
	 * Algoritmo que habilita a intera��o pela interface gr�fica para que um
	 * jogador real possa jogar.
	 */
	HUMANO("Humano", null),

	// Tipos definidos a partir deste ponto s�o personalizados e dever�o ter uma
	// implementa��o no m�dulo que controla a intelig�ncia artificial.

	/**
	 * Um jogador definido com este algoritmo realiza jogadas aleat�rias.
	 * Nenhuma heur�stica est� definida para este tipo de jogador.
	 */
	ALEATORIO("Aleat�rio", null),

	/**
	 * Um jogador definido com este algoritmo realiza jogadas analisando uma
	 * �rvore de decis�o gerada pelo algoritmo Minimax. A profundidade da �rvore
	 * est� associada � heur�stica escolhida.
	 */
	MINIMAX("Minimax", new IndicadorHeuristica[] { IndicadorHeuristica.H1_P3,
			IndicadorHeuristica.H1_P4, IndicadorHeuristica.H1_P5 }),

	/**
	 * Um jogador definido com este algoritmo realiza jogadas analisando uma
	 * �rvore de decis�o gerada pelo algoritmo Minimax com podas Alfa-Beta. A
	 * profundidade da �rvore est� associada � heur�stica escolhida.
	 */
	ALPHA_BETA("Alfa-Beta", new IndicadorHeuristica[] {
			IndicadorHeuristica.H1_P3, IndicadorHeuristica.H1_P4,
			IndicadorHeuristica.H1_P5, IndicadorHeuristica.H1_P6 });

	/**
	 * Label para exibi��o na interface gr�fica.
	 */
	private final String label;
	private final IndicadorHeuristica[] heuristicas;

	private IndicadorAlgoritmo(String label, IndicadorHeuristica[] heuristicas) {
		this.label = label;
		if (heuristicas == null || heuristicas.length == 0) {
			this.heuristicas = new IndicadorHeuristica[] {};
		} else {
			this.heuristicas = heuristicas;
		}
	}

	public String getLabel() {
		return this.label;
	}

	public IndicadorHeuristica[] getHeuristicas() {
		return this.heuristicas;
	}

	public boolean haveHeuristicas() {
		return (this.heuristicas.length > 0);
	}
}
