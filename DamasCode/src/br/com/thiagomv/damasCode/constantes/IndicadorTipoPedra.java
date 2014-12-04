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
	 * Este tipo � associado a pedras normais no jogo. Pedras deste tipo
	 * normalmente executam movimentos para "frente". Movimentos para "tr�s" s�
	 * poder�o ser executados quando houver captura.
	 */
	NORMAL,

	/**
	 * Este tipo � associado a pedras especiais. Estas pedras s�o formadas
	 * quando alguma pedra do jogador atinge o limite do tabuleiro, onde as
	 * pedras do oponente se localizam no in�cio do jogo. Este tipo de pedra
	 * pode se movimentar em todas as dire��es, quantas casas quiser, desde que
	 * no m�ximo uma pedra do oponente seja capturada no movimento.
	 */
	DAMA
}
