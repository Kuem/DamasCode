package br.com.thiagomv.damasCode.view.damas;

/**
 * Interface para objetos que recebem eventos associados ao painel de ações.
 * 
 * @author Thiago
 * 
 */
public interface PanelAcoesListener {

	/**
	 * Evento de click gerado pelo botão IniciarJogo.
	 */
	public void onPanelAcoes_ClickIniciarJogo();

	/**
	 * Evento de click gerado pelo botão InterromperJogo.
	 */
	public void onPanelAcoes_ClickInterromperJogo();
}
