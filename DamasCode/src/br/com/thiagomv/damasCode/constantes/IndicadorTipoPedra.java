package br.com.thiagomv.damasCode.constantes;

/**
 * Indica o tipo de uma pedra.
 * 
 * @author Thiago Mendes Vieira
 * 
 *         20/09/2014
 */
public enum IndicadorTipoPedra {
	/**
	 * Este tipo é associado a pedras normais no jogo. Pedras deste tipo
	 * normalmente executam movimentos para "frente". Movimentos para "trás" só
	 * poderão ser executados quando houver captura.
	 */
	NORMAL,

	/**
	 * Este tipo é associado a pedras especiais. Estas pedras são formadas
	 * quando alguma pedra do jogador atinge o limite do tabuleiro, onde as
	 * pedras do oponente se localizam no início do jogo. Este tipo de pedra
	 * pode se movimentar em todas as direções, quantas casas quiser, desde que
	 * no máximo uma pedra do oponente seja capturada no movimento.
	 */
	DAMA
}
