package br.com.thiagomv.damasCode.estruturas;

/**
 * Representa a posição de uma pedra no tabuleiro. A posição e definida pelas
 * coordenadas de linha e coluna. A coordenada (0,0) é definida pelo ponto
 * inferior esquerdo do tabuleiro, visto a partir do jogador 1.
 * 
 * @author Thiago Mendes Vieira
 * 
 *         20/09/2014
 */
public class PosicaoTabuleiro {
	private final int linha;
	private final int coluna;

	/**
	 * Cria uma posição do tabuleiro, definida pelas coordenadas de linha e
	 * coluna.
	 * 
	 * @param linha
	 *            Coordenada da linha desta posiçao.
	 * @param coluna
	 *            Coordenada da coluna desta posição.
	 */
	public PosicaoTabuleiro(int linha, int coluna) {
		this.linha = linha;
		this.coluna = coluna;
	}

	/**
	 * Obtém a coordenada de linha desta posição.
	 * 
	 * @return Valor da coordenada de linha.
	 */
	public int getLinha() {
		return this.linha;
	}

	/**
	 * Obtém a coordenada de coluna desta posição.
	 * 
	 * @return Valor da coordenada de coluna.
	 */
	public int getColuna() {
		return this.coluna;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PosicaoTabuleiro) {
			PosicaoTabuleiro posicaoTabuleiro = (PosicaoTabuleiro) obj;
			if (posicaoTabuleiro.linha == this.linha
					&& posicaoTabuleiro.coluna == this.coluna) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "[" + linha + " , " + coluna + "]";
	}
}
