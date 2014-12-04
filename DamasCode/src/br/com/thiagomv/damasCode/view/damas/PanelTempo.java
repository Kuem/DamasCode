package br.com.thiagomv.damasCode.view.damas;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Alignment;

import br.com.thiagomv.damasCode.constantes.IndicadorJogador;
import br.com.thiagomv.damasCode.controle.TimeController;

public class PanelTempo extends JPanel {
	/**
	 * Versão 1.0.
	 */
	private static final long serialVersionUID = 0100L;

	private static final String NO_TIME = "-";
	private static final String _UNIDADE = " s";
	private static final int WIDTH_LABELS = 120;
	private static final double ESCALA_UNIDADES = 1.0 / Math.pow(10, 9);
	
	private final IndicadorJogador jogador;

	private final JLabel labelTempoTotal;
	private final JLabel labelTempoTotalValue;

	private final JLabel labelMaiorTempo;
	private final JLabel labelMaiorTempoValue;

	private final JLabel labelTempoMedio;
	private final JLabel labelTempoMedioValue;

	private final JPanel panelLabels;
	private final JPanel panelValues;

	/**
	 * /** Contrutor da classe.
	 * 
	 * @param titulo
	 *            Título do painel.
	 */
	public PanelTempo(String titulo, IndicadorJogador jogador) {
		this.jogador = jogador;

		panelLabels = new JPanel();
		panelValues = new JPanel();

		Dimension dimension;

		labelTempoTotal = new JLabel("Tempo total:");
		labelTempoTotalValue = new JLabel(NO_TIME);

		dimension = labelTempoTotalValue.getPreferredSize();
		dimension.width = WIDTH_LABELS;
		labelTempoTotalValue.setPreferredSize(dimension);

		labelMaiorTempo = new JLabel("Maior tempo:");
		labelMaiorTempoValue = new JLabel(NO_TIME);

		dimension = labelMaiorTempoValue.getPreferredSize();
		dimension.width = WIDTH_LABELS;
		labelMaiorTempoValue.setPreferredSize(dimension);

		labelTempoMedio = new JLabel("Tempo médio:");
		labelTempoMedioValue = new JLabel(NO_TIME);

		dimension = labelTempoMedioValue.getPreferredSize();
		dimension.width = WIDTH_LABELS;
		labelTempoMedioValue.setPreferredSize(dimension);

		// Panel...
		setBorder(BorderFactory.createTitledBorder(titulo));

		// Layout Labels...
		UIDamas.criarGroupLayoutVertical(panelLabels,
				GroupLayout.PREFERRED_SIZE, labelTempoTotal, labelMaiorTempo,
				labelTempoMedio);

		UIDamas.criarGroupLayoutVertical(panelValues,
				GroupLayout.PREFERRED_SIZE, labelTempoTotalValue,
				labelMaiorTempoValue, labelTempoMedioValue);

		// Layout panel...
		UIDamas.criarGroupLayoutHorizontal(this, GroupLayout.PREFERRED_SIZE,
				Alignment.LEADING, panelLabels, panelValues);
	}

	public void reset() {
		labelTempoTotalValue.setText(NO_TIME);
		labelMaiorTempoValue.setText(NO_TIME);
		labelTempoMedioValue.setText(NO_TIME);
	}

	public void updateTempos(boolean comparacao) {
		if (comparacao) {
			reset();
			return;
		}

		TimeController timeController = TimeController.getInstance();

		labelTempoTotalValue.setText(UIDamas.valorComCasasDecimais(timeController
				.getSomaTotal(jogador) * ESCALA_UNIDADES)
				+ _UNIDADE);

		labelMaiorTempoValue.setText(UIDamas.valorComCasasDecimais(timeController
				.getMaiorTempo(jogador) * ESCALA_UNIDADES)
				+ _UNIDADE);

		labelTempoMedioValue.setText(UIDamas.valorComCasasDecimais(timeController
				.getMediaTempo(jogador) * ESCALA_UNIDADES)
				+ _UNIDADE);
	}

}
