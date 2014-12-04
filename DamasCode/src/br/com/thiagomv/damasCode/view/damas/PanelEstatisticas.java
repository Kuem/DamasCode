package br.com.thiagomv.damasCode.view.damas;

import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Alignment;

import br.com.thiagomv.damasCode.constantes.IndicadorJogador;

public class PanelEstatisticas extends JPanel {

	/**
	 * Versão 1.0.
	 */
	private static final long serialVersionUID = 1L;

	private static final String INDEFINIDO_JOGADAS = "-";

	private final JLabel titulo;
	private final JLabel totalJogadas;
	private final JLabel totalJogadasValue;
	private final JPanel panelJogadas;
	private final PanelTempo panelTempoJ1;
	private final PanelTempo panelTempoJ2;
	private final PanelMemoria panelMemoriaJ1;
	private final PanelMemoria panelMemoriaJ2;

	public PanelEstatisticas() {
		titulo = new JLabel("Estatísticas:", JLabel.CENTER);
		Font fonte = new Font(null, Font.BOLD, 20);
		titulo.setFont(fonte);

		panelJogadas = new JPanel();
		totalJogadas = new JLabel("Total de jogadas:");
		totalJogadasValue = new JLabel(INDEFINIDO_JOGADAS);
		panelTempoJ1 = new PanelTempo("Jogador 1", IndicadorJogador.JOGADOR1);
		panelTempoJ2 = new PanelTempo("Jogador 2", IndicadorJogador.JOGADOR2);
		panelMemoriaJ1 = new PanelMemoria("Jogador 1",
				IndicadorJogador.JOGADOR1);
		panelMemoriaJ2 = new PanelMemoria("Jogador 2",
				IndicadorJogador.JOGADOR2);

		UIDamas.criarGroupLayoutHorizontal(panelJogadas,
				GroupLayout.PREFERRED_SIZE, Alignment.LEADING, totalJogadas,
				totalJogadasValue);

		UIDamas.criarGroupLayoutVertical(this, Short.MAX_VALUE, titulo,
				panelJogadas, panelTempoJ1, panelTempoJ2, panelMemoriaJ1,
				panelMemoriaJ2);
	}

	public void reset() {
		totalJogadasValue.setText(INDEFINIDO_JOGADAS);
		panelTempoJ1.reset();
		panelTempoJ2.reset();
		panelMemoriaJ1.reset();
		panelMemoriaJ2.reset();
	}

	public void update(int numJogadas) {
		boolean comparacao = false;
		if (numJogadas <= 0) {
			comparacao = true;
			totalJogadasValue.setText(INDEFINIDO_JOGADAS);
		} else {
			totalJogadasValue.setText(String.valueOf(numJogadas));
		}
		panelTempoJ1.updateTempos(comparacao);
		panelTempoJ2.updateTempos(comparacao);
		panelMemoriaJ1.updateMemorias(comparacao);
		panelMemoriaJ2.updateMemorias(comparacao);
	}
}
