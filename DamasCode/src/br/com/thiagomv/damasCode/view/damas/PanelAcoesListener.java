package br.com.thiagomv.damasCode.view.damas;

/**
 * Interface para objetos que recebem eventos associados ao painel de a��es.
 * 
 * @author Thiago
 * 
 */
public interface PanelAcoesListener {

	/**
	 * Evento de click gerado pelo bot�o IniciarJogo.
	 */
	public void onPanelAcoes_ClickIniciarJogo();

	/**
	 * Evento de click gerado pelo bot�o InterromperJogo.
	 */
	public void onPanelAcoes_ClickInterromperJogo();
}
