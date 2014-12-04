package br.com.thiagomv.damasCode.view.damas;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import br.com.thiagomv.damasCode.constantes.IndicadorAlgoritmo;
import br.com.thiagomv.damasCode.constantes.IndicadorHeuristica;
import br.com.thiagomv.damasCode.constantes.IndicadorJogador;

/**
 * Esta classe define um JPanel personalizado que representa as configurações do
 * jogador. Nestas configuração são deinidos o tipo de algoritmo e o tipo de
 * heurística utilizado pelo jogador. Por definição, o jogador com algoritmo
 * igual a HUMANO não terá nenhuma heurística.
 * 
 * @author Thiago Mendes Vieira
 * 
 *         20/09/2014
 */
public final class PanelJogador extends JPanel {
	/**
	 * Versão 1.0.
	 */
	private static final long serialVersionUID = 0100L;

	private static final int WIDTH_LISTA_ALGORITMOS = 140;

	private final JPanel panelAlgoritmo;
	private final JLabel labelAlgoritmo;
	private final JComboBox<IndicadorAlgoritmo> listaAlgoritmos;

	private final JPanel panelHeuristica;
	private final JLabel labelHeuristica;
	private final JComboBox<IndicadorHeuristica> listaHeuristicas;

	private final IndicadorJogador jogador;
	private final PanelJogadorListener listener;

	/**
	 * Contrutor da classe.
	 * 
	 * @param titulo
	 *            Título do painel.
	 * @param panelMenu
	 */
	public PanelJogador(String titulo, IndicadorJogador jogador,
			PanelJogadorListener listener) {
		this.jogador = jogador;
		this.listener = listener;

		Dimension dimension;

		// Algoritmos...
		panelAlgoritmo = new JPanel();
		labelAlgoritmo = new JLabel();

		labelAlgoritmo.setText("Algoritmo:");
		listaAlgoritmos = new JComboBox<IndicadorAlgoritmo>(
				IndicadorAlgoritmo.values());

		dimension = listaAlgoritmos.getPreferredSize();
		dimension.width = WIDTH_LISTA_ALGORITMOS;
		listaAlgoritmos.setPreferredSize(dimension);

		listaAlgoritmos.setSelectedItem(IndicadorAlgoritmo.HUMANO);

		listaAlgoritmos.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent evt) {
				IndicadorAlgoritmo alg = (IndicadorAlgoritmo) listaAlgoritmos
						.getSelectedItem();
				onAlgoritmoStateChanged(alg);

				IndicadorHeuristica heu = (IndicadorHeuristica) listaHeuristicas
						.getSelectedItem();
				onHeuristicaStateChanged(heu);
			}
		});

		// Heurísticas...
		panelHeuristica = new JPanel();
		labelHeuristica = new JLabel();

		labelHeuristica.setText("Heurística:");
		listaHeuristicas = new JComboBox<IndicadorHeuristica>(
				IndicadorAlgoritmo.HUMANO.getHeuristicas());

		dimension = listaHeuristicas.getPreferredSize();
		dimension.width = WIDTH_LISTA_ALGORITMOS
				- (labelHeuristica.getPreferredSize().width - labelAlgoritmo
						.getPreferredSize().width);
		listaHeuristicas.setPreferredSize(dimension);

		listaHeuristicas.setSelectedItem(null);

		listaHeuristicas.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent evt) {
				IndicadorHeuristica heu = (IndicadorHeuristica) listaHeuristicas
						.getSelectedItem();
				onHeuristicaStateChanged(heu);
			}
		});

		// Panel...
		setBorder(BorderFactory.createTitledBorder(titulo));

		// Layout algoritmos...
		UIDamas.criarGroupLayoutHorizontal(panelAlgoritmo,
				GroupLayout.PREFERRED_SIZE, GroupLayout.Alignment.CENTER,
				labelAlgoritmo, listaAlgoritmos);

		// Layout heurísticas...
		UIDamas.criarGroupLayoutHorizontal(panelHeuristica,
				GroupLayout.PREFERRED_SIZE, GroupLayout.Alignment.CENTER,
				labelHeuristica, listaHeuristicas);

		// Layout Panel...
		UIDamas.criarGroupLayoutVertical(this, Short.MAX_VALUE, panelAlgoritmo,
				panelHeuristica);

		onAlgoritmoStateChanged((IndicadorAlgoritmo) listaAlgoritmos
				.getSelectedItem());
	}

	/**
	 * Este método é chamado em eventos de seleção do botão Rádio associado ao
	 * algoritmo do jogador. Quando um algoritmo é selecionado, o painel de
	 * escolha de Heurísticas e todos os botões de escolhas associados ao painel
	 * são habilitados se o algorítimo tiver heurísticas, ou desabilitados caso
	 * contrário.
	 * 
	 * @param alg
	 *            Algoritmo selecionado.
	 */
	private void onAlgoritmoStateChanged(IndicadorAlgoritmo alg) {
		boolean enabled = alg.haveHeuristicas();
		listaHeuristicas
				.setModel(new DefaultComboBoxModel<IndicadorHeuristica>(alg
						.getHeuristicas()));
		listaHeuristicas.setEnabled(enabled);
		panelHeuristica.setEnabled(enabled);

		jogador.setAlgoritmo(alg);

		if (listener != null) {
			listener.onPanelJogadorListener_algoritmoChanged();
		}
	}

	/**
	 * Este método é chamado em eventos de seleção do botão Rádio associado à
	 * heurística do jogador.
	 * 
	 * @param heu
	 *            Heurística selecionada.
	 */
	private void onHeuristicaStateChanged(IndicadorHeuristica heu) {
		jogador.setHeuristica(heu);
	}

	/**
	 * Retorna o Algorítmo selecionado para este jogador.
	 * 
	 * @return Algoritmo selecionado.
	 */
	public IndicadorAlgoritmo getAlgoritmo() {
		return (IndicadorAlgoritmo) listaAlgoritmos.getSelectedItem();
	}

	/**
	 * Retorna a Heurística selecionada para este jogador.
	 * 
	 * @return Heurística selecionada. O valor null será retornado caso o
	 *         algorítmo selecionado seja HUMANO.
	 */
	public IndicadorHeuristica getHeuristica() {
		return (IndicadorHeuristica) listaHeuristicas.getSelectedItem();
	}

	/**
	 * Habilita/Desabilita o painel.
	 * 
	 * @param enabled
	 *            "true" se o painel deve ser habilitado ou "false", caso
	 *            contrário.
	 */
	public void setPanelJogadoresEnabled(boolean enabled) {
		this.setEnabled(enabled);
		listaAlgoritmos.setEnabled(enabled);
		panelAlgoritmo.setEnabled(enabled);
		if (!getAlgoritmo().haveHeuristicas()) {
			enabled = false;
		}
		listaHeuristicas.setEnabled(enabled);
		panelHeuristica.setEnabled(enabled);
	}
}
