package br.com.thiagomv.damasCode.view.damas;

import javax.swing.JLabel;

/**
 * Esta classe define um JLabel personalizado que representa uma casa do
 * tabuleiro com informa��es de sua posi��o no mesmo.
 * 
 * @author Thiago
 * 
 */
public final class LabelCasa extends JLabel {
	/**
	 * Vers�o 1.0.
	 */
	private static final long serialVersionUID = 1L;

	private final int linha;
	private final int coluna;

	/**
	 * Construtor da classe.
	 * 
	 * @param L
	 *            Linha da posi��o no tabuleiro.
	 * @param C
	 *            Coluna da posi��o no tabuleiro.
	 */
	public LabelCasa(int L, int C) {
		super();
		this.linha = L;
		this.coluna = C;
	}

	/**
	 * Retorna a linha da posi��o no tabuleiro.
	 * 
	 * @return Linha.
	 */
	public int getLinha() {
		return this.linha;
	}

	/**
	 * Retorna a coluna da posi��o no tabuleiro.
	 * 
	 * @return Coluna.
	 */
	public int getColuna() {
		return this.coluna;
	}
}
