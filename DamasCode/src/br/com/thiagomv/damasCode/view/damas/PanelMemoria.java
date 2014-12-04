package br.com.thiagomv.damasCode.view.damas;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Alignment;

import br.com.thiagomv.damasCode.constantes.IndicadorJogador;
import br.com.thiagomv.damasCode.controle.MemoriaController;

public class PanelMemoria extends JPanel {
	/**
	 * Versão 1.0.
	 */
	private static final long serialVersionUID = 0100L;

	private static final String NO_MEMORY = "-";
	private static final String _UNIDADE = " KB";
	private static final int WIDTH_LABELS = 120;
	private static final double ESCALA_UNIDADES = 1.0 / Math.pow(1024, 1);

	private final IndicadorJogador jogador;

	private final JLabel labelUsoMedio;
	private final JLabel labelUsoMedioValue;

	private final JLabel labelMaiorUso;
	private final JLabel labelMaiorUsoValue;

	private final JPanel panelLabels;
	private final JPanel panelValues;

	/**
	 * Contrutor da classe.
	 * 
	 * @param titulo
	 *            Título do painel.
	 */
	public PanelMemoria(String titulo, IndicadorJogador jogador) {
		this.jogador = jogador;

		panelLabels = new JPanel();
		panelValues = new JPanel();

		Dimension dimension;

		labelUsoMedio = new JLabel("Uso médio:");
		labelUsoMedioValue = new JLabel(NO_MEMORY);

		dimension = labelUsoMedioValue.getPreferredSize();
		dimension.width = WIDTH_LABELS;
		labelUsoMedioValue.setPreferredSize(dimension);

		labelMaiorUso = new JLabel("Maior uso:");
		labelMaiorUsoValue = new JLabel(NO_MEMORY);

		dimension = labelMaiorUsoValue.getPreferredSize();
		dimension.width = WIDTH_LABELS;
		labelMaiorUsoValue.setPreferredSize(dimension);

		// Panel...
		setBorder(BorderFactory.createTitledBorder(titulo));

		// Layout Labels...
		UIDamas.criarGroupLayoutVertical(panelLabels,
				GroupLayout.PREFERRED_SIZE, labelUsoMedio, labelMaiorUso);

		UIDamas.criarGroupLayoutVertical(panelValues,
				GroupLayout.PREFERRED_SIZE, labelUsoMedioValue,
				labelMaiorUsoValue);

		// Layout panel...
		UIDamas.criarGroupLayoutHorizontal(this, GroupLayout.PREFERRED_SIZE,
				Alignment.LEADING, panelLabels, panelValues);
	}

	public void reset() {
		labelUsoMedioValue.setText(NO_MEMORY);
		labelMaiorUsoValue.setText(NO_MEMORY);
	}

	public void updateMemorias(boolean comparacao) {
		if (!MemoriaController.isMemoryAnalysisEnabled() || comparacao) {
			reset();
			return;
		}

		labelUsoMedioValue.setText(UIDamas
				.valorComCasasDecimais(MemoriaController.getMediaUso(jogador)
						* ESCALA_UNIDADES)
				+ _UNIDADE);

		labelMaiorUsoValue.setText(UIDamas
				.valorComCasasDecimais(MemoriaController.getMaiorUso(jogador)
						* ESCALA_UNIDADES)
				+ _UNIDADE);
	}

}
