package br.com.thiagomv.damasCode.view.comparar;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import br.com.thiagomv.damasCode.constantes.IndicadorJogador;
import br.com.thiagomv.damasCode.view.damas.UIDamas;

public class PanelInfoJogador extends JPanel {

	/**
	 * Versão 1.0.
	 */
	private static final long serialVersionUID = 1L;

	private static final int WIDTH_LABEL_ALGORITMOS = 140;
	private static final String SEM_HEURISTICA = "-";

	private final JPanel panelAlgoritmo;
	private final JLabel labelAlgoritmo;
	private final JLabel labelAlgoritmoValue;

	private final JPanel panelHeuristica;
	private final JLabel labelHeuristica;
	private final JLabel labelHeuristicaValue;

	public PanelInfoJogador(String titulo, IndicadorJogador jogador) {
		Dimension dimension;

		// Algoritmos...
		panelAlgoritmo = new JPanel();
		labelAlgoritmo = new JLabel("Algoritmo:");
		labelAlgoritmoValue = new JLabel(jogador.getAlgoritmo().toString());

		dimension = labelAlgoritmoValue.getPreferredSize();
		dimension.width = WIDTH_LABEL_ALGORITMOS;
		labelAlgoritmoValue.setPreferredSize(dimension);

		// Heurísticas...
		panelHeuristica = new JPanel();
		labelHeuristica = new JLabel("Heurística:");
		labelHeuristicaValue = new JLabel(
				jogador.getHeuristica() != null ? jogador.getHeuristica()
						.toString() : SEM_HEURISTICA);

		dimension = labelHeuristicaValue.getPreferredSize();
		dimension.width = WIDTH_LABEL_ALGORITMOS
				- (labelHeuristica.getPreferredSize().width - labelAlgoritmo
						.getPreferredSize().width);
		labelHeuristicaValue.setPreferredSize(dimension);

		// Panel...
		setBorder(BorderFactory.createTitledBorder(titulo));

		// Layout algoritmos...
		UIDamas.criarGroupLayoutHorizontal(panelAlgoritmo,
				GroupLayout.PREFERRED_SIZE, GroupLayout.Alignment.CENTER,
				labelAlgoritmo, labelAlgoritmoValue);

		// Layout heurísticas...
		UIDamas.criarGroupLayoutHorizontal(panelHeuristica,
				GroupLayout.PREFERRED_SIZE, GroupLayout.Alignment.CENTER,
				labelHeuristica, labelHeuristicaValue);

		// Layout Panel...
		UIDamas.criarGroupLayoutVertical(this, Short.MAX_VALUE, panelAlgoritmo,
				panelHeuristica);
	}
}
