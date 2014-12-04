package br.com.thiagomv.damasCode.constantes;

/**
 * Indica o tipo de uma pedra e a qual jogador ela pertence. Existem somente
 * quatro possibilidades: Pedras do tipo Normal que pertence ao jogador 1 ou ao
 * jogador 2, e pedras do tipo Dama que pertencem ao jogador 1 ou ao jogador 2.
 * 
 * @author Thiago Mendes Vieira
 * 
 *         20/09/2014
 */
public enum IndicadorPedraJogador {
	NORMAL_J1(IndicadorJogador.JOGADOR1, IndicadorTipoPedra.NORMAL), DAMA_J1(
			IndicadorJogador.JOGADOR1, IndicadorTipoPedra.DAMA), NORMAL_J2(
			IndicadorJogador.JOGADOR2, IndicadorTipoPedra.NORMAL), DAMA_J2(
			IndicadorJogador.JOGADOR2, IndicadorTipoPedra.DAMA);

	private final IndicadorJogador jogador;
	private final IndicadorTipoPedra tipo;

	private IndicadorPedraJogador(IndicadorJogador jogador,
			IndicadorTipoPedra tipo) {
		this.jogador = jogador;
		this.tipo = tipo;
	}

	public IndicadorJogador getJogador() {
		return this.jogador;
	}

	public IndicadorTipoPedra getTipoPedra() {
		return this.tipo;
	}

	/**
	 * Este método retorna o indicador que representa a promoção de uma pedra
	 * Normal para Dama, associado a um tipo de jogador. Este método só pode ser
	 * chamado quando a pedra deste indicador for do tipo Normal.
	 * 
	 * @return O indicador apropriado para a promoção desta pedra a Dama.
	 */
	public IndicadorPedraJogador promoverDama() {
		// Assegura que esta pedra seja do tipo normal para que possa ser
		// promovida.
		assert (IndicadorTipoPedra.NORMAL.equals(this.tipo));

		if (IndicadorJogador.JOGADOR1.equals(this.jogador)) {
			return DAMA_J1;
		} else {
			return DAMA_J2;
		}
	}
}
