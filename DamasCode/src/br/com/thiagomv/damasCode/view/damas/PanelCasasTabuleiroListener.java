package br.com.thiagomv.damasCode.view.damas;

/**
 * Interface para objetos que recebem eventos associados �s casas do tabuleiro.
 * 
 * @author Thiago
 * 
 */
public interface PanelCasasTabuleiroListener {

	/**
	 * Evento de click gerado por uma casa do tabuleiro.
	 * 
	 * @param linha
	 *            Linha de posi��o da casa no tabuleiro.
	 * @param coluna
	 *            Coluna de posi��o da casa no tabuleiro.
	 */
	public void onPanelCasasTabuleiro_ClickCasa(int linha, int coluna);
}
