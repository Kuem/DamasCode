package br.com.thiagomv.damasCode.constantes;

/**
 * Indica a dire��o de um movimento de pedra no tabuleiro. Os movimentos s�
 * podem ser realizados em 4 dire��es: Noroeste, Nordeste, Sudeste e Sudoeste. A
 * dire��o tem como refer�ncia a vis�o que o jogador 1 tem do tabuleiro. O
 * jogador 1 s� pode executar movimentos nas dire��es Sudeste e Sudoeste se
 * forem movimentos com DAMA ou movimentos de captura. O jogador 2 s� pode
 * executar movimentos nas dire��es Noroeste e Nordeste se forem movimentos com
 * DAMA ou movimentos de captura.
 * 
 * @author Thiago Mendes Vieira
 * 
 *         20/09/2014
 */
public enum IndicadorDirecao {
	NOROESTE, NORDESTE, SUDESTE, SUDOESTE
}
