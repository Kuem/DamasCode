package br.com.thiagomv.damasCode.constantes;

/**
 * Indica a direção de um movimento de pedra no tabuleiro. Os movimentos só
 * podem ser realizados em 4 direções: Noroeste, Nordeste, Sudeste e Sudoeste. A
 * direção tem como referência a visão que o jogador 1 tem do tabuleiro. O
 * jogador 1 só pode executar movimentos nas direções Sudeste e Sudoeste se
 * forem movimentos com DAMA ou movimentos de captura. O jogador 2 só pode
 * executar movimentos nas direções Noroeste e Nordeste se forem movimentos com
 * DAMA ou movimentos de captura.
 * 
 * @author Thiago Mendes Vieira
 * 
 *         20/09/2014
 */
public enum IndicadorDirecao {
	NOROESTE, NORDESTE, SUDESTE, SUDOESTE
}
