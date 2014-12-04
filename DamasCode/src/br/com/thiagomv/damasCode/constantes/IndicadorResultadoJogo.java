package br.com.thiagomv.damasCode.constantes;

/**
 * Indica o resultado após a finalização de um jogo.
 * 
 * @author Thiago Mendes Vieira
 * 
 *         20/09/2014
 */
public enum IndicadorResultadoJogo {
	/**
	 * Indica que o jogador 1 venceu eliminando todas as pedras do oponente ou
	 * impossibilitando o oponente de realizar alguma jogada.
	 */
	VENCE_JOGADOR1,

	/**
	 * Indica que o jogador 2 venceu eliminando todas as pedras do oponente ou
	 * impossibilitando o oponente de realizar alguma jogada.
	 */
	VENCE_JOGADOR2,

	/**
	 * Indica que o jogo empatou com a realização de 20 lances sucessivos de
	 * damas de cada jogador.
	 */
	EMPATE_MOVIMENTO_DAMAS,

	/**
	 * Indica que o jogo empatou após a realização de 5 lances de cada jogador,
	 * quando os jogadores 1 e 2 tinham apenas duas Damas cada um.
	 */
	EMPATE_2D_2D,

	/**
	 * Indica que o jogo empatou após a realização de 5 lances de cada jogador,
	 * quando um jogador tinha apenas duas Damas e o outro tinha apenas uma
	 * Dama.
	 */
	EMPATE_2D_1D,

	/**
	 * Indica que o jogo empatou após a realização de 5 lances de cada jogador,
	 * quando um jogador tinha apenas duas Damas e o outro tinha apenas uma Dama
	 * e uma pedra Normal.
	 */
	EMPATE_2D_1D_1N,

	/**
	 * Indica que o jogo empatou após a realização de 5 lances de cada jogador,
	 * quando os jogadores 1 e 2 tinham apenas uma Dama cada um.
	 */
	EMPATE_1D_1D,

	/**
	 * Indica que o jogo empatou após a realização de 5 lances de cada jogador,
	 * quando um jogador tinha apenas uma Dama e o outro tinha apenas uma Dama e
	 * uma pedra Normal.
	 */
	EMPATE_1D_1D_1N,

	/**
	 * Indica que o jogo foi interrompido e, portanto, não há vencedores.
	 */
	JOGO_INTERROMPIDO;
}
