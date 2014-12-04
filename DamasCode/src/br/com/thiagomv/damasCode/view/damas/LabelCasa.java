package br.com.thiagomv.damasCode.view.damas;

import javax.swing.JLabel;

/**
 * Esta classe define um JLabel personalizado que representa uma casa do
 * tabuleiro com informações de sua posição no mesmo.
 * 
 * @author Thiago
 * 
 */
public final class LabelCasa extends JLabel {
	/**
	 * Versão 1.0.
	 */
	private static final long serialVersionUID = 1L;

	private final int linha;
	private final int coluna;

	/**
	 * Construtor da classe.
	 * 
	 * @param L
	 *            Linha da posição no tabuleiro.
	 * @param C
	 *            Coluna da posição no tabuleiro.
	 */
	public LabelCasa(int L, int C) {
		super();
		this.linha = L;
		this.coluna = C;
	}

	/**
	 * Retorna a linha da posição no tabuleiro.
	 * 
	 * @return Linha.
	 */
	public int getLinha() {
		return this.linha;
	}

	/**
	 * Retorna a coluna da posição no tabuleiro.
	 * 
	 * @return Coluna.
	 */
	public int getColuna() {
		return this.coluna;
	}
}
