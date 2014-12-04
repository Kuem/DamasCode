package br.com.thiagomv.damasCode.estruturas;

import br.com.thiagomv.damasCode.constantes.IndicadorJogador;

/**
 * Representa um estado de jogo. � definido por dois par�metros: A configura��o
 * do tabuleiro e o jogador que realizar� a pr�xima jogada.
 * 
 * @author Thiago Mendes Vieira
 * 
 *         20/09/2014
 */
public class EstadoJogo implements Cloneable {
	/**
	 * Tabuleiro definido para este estado de jogo.
	 */
	private Tabuleiro tabuleiro;

	/**
	 * Jogador atual. O jogador que realizar� a pr�xima jogada.
	 */
	private IndicadorJogador jogadorAtual;

	public void setTabuleiro(Tabuleiro tabuleiro) {
		this.tabuleiro = tabuleiro;
	}

	public Tabuleiro getTabueiro() {
		return this.tabuleiro;
	}

	public void setJogadorAtual(IndicadorJogador jogador) {
		this.jogadorAtual = jogador;
	}

	public IndicadorJogador getJogadorAtual() {
		return this.jogadorAtual;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		EstadoJogo novoEstado = new EstadoJogo();
		novoEstado.setJogadorAtual(this.jogadorAtual);
		novoEstado.setTabuleiro((Tabuleiro) this.tabuleiro.clone());
		return novoEstado;
	}
}
