package br.com.thiagomv.damasCode.view.damas;

/**
 * Interface para objetos que recebem eventos associados às casas do tabuleiro.
 * 
 * @author Thiago
 * 
 */
public interface PanelCasasTabuleiroListener {

	/**
	 * Evento de click gerado por uma casa do tabuleiro.
	 * 
	 * @param linha
	 *            Linha de posição da casa no tabuleiro.
	 * @param coluna
	 *            Coluna de posição da casa no tabuleiro.
	 */
	public void onPanelCasasTabuleiro_ClickCasa(int linha, int coluna);
}
