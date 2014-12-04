package br.com.thiagomv.damasCode.constantes;

/**
 * Indica o tipo de algorítmo que o jogador implementa. Os tipos de algoritmos
 * definidos aqui serão disponibilizados no menu de configuraçções de cada
 * jogador. O tipo "HUMANO" é um tipo especial que já está implementado pela
 * arquitetura do jogo e não pode ser removido. Quando este tipo é definido para
 * o jogador, o mesmo não terá uma inteligência artificial, mas o controle será
 * dado a um jogador real que interage com a interface gráfica.
 * 
 * @author Thiago Mendes Vieira
 * 
 *         20/09/2014
 */
public enum IndicadorAlgoritmo {
	/**
	 * Algoritmo que habilita a interação pela interface gráfica para que um
	 * jogador real possa jogar.
	 */
	HUMANO("Humano", null),

	// Tipos definidos a partir deste ponto são personalizados e deverão ter uma
	// implementação no módulo que controla a inteligência artificial.

	/**
	 * Um jogador definido com este algoritmo realiza jogadas aleatórias.
	 * Nenhuma heurística está definida para este tipo de jogador.
	 */
	ALEATORIO("Aleatório", null),

	/**
	 * Um jogador definido com este algoritmo realiza jogadas analisando uma
	 * árvore de decisão gerada pelo algoritmo Minimax. A profundidade da árvore
	 * está associada à heurística escolhida.
	 */
	MINIMAX("Minimax", new IndicadorHeuristica[] { IndicadorHeuristica.H1_P3,
			IndicadorHeuristica.H1_P4, IndicadorHeuristica.H1_P5 }),

	/**
	 * Um jogador definido com este algoritmo realiza jogadas analisando uma
	 * árvore de decisão gerada pelo algoritmo Minimax com podas Alfa-Beta. A
	 * profundidade da árvore está associada à heurística escolhida.
	 */
	ALPHA_BETA("Alfa-Beta", new IndicadorHeuristica[] {
			IndicadorHeuristica.H1_P3, IndicadorHeuristica.H1_P4,
			IndicadorHeuristica.H1_P5, IndicadorHeuristica.H1_P6 });

	/**
	 * Label para exibição na interface gráfica.
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
