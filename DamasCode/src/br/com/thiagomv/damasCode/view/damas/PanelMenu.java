package br.com.thiagomv.damasCode.view.damas;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;

import br.com.thiagomv.damasCode.constantes.IndicadorAlgoritmo;
import br.com.thiagomv.damasCode.constantes.IndicadorHeuristica;
import br.com.thiagomv.damasCode.constantes.IndicadorJogador;

/**
 * Esta classe define um JPanel personalizado que agrupa as configuraçoes dos
 * dois jogadores.
 * 
 * @author Thiago
 * 
 */
public final class PanelMenu extends JPanel implements PanelJogadorListener {
	/**
	 * Versão 1.0.
	 */
	private static final long serialVersionUID = 1L;

	private final JButton buttonSobre;
	private final JLabel titulo;
	private final PanelJogador panelJogador1;
	private final PanelJogador panelJogador2;
	private final JCheckBox memoryAnalysisEnabled;
	private final PanelAcoes panelAcoes;
	private final JButton compararJogadores;
	private final UIDamas ui;

	private final JProgressBar barra;

	/**
	 * Contrutor da classe.
	 */
	public PanelMenu(UIDamas uiDamas) {
		this.ui = uiDamas;

		buttonSobre = new JButton("Sobre");
		buttonSobre.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				onClickSobre();
			}
		});

		compararJogadores = new JButton("Comparar Jogadores...");
		compararJogadores.setEnabled(false);
		compararJogadores.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				onClickCompararJogadores();
			}
		});

		barra = new JProgressBar(0, 100);
		barra.setValue(0);

		titulo = new JLabel("Configurações:", JLabel.CENTER);
		Font fonte = new Font(null, Font.BOLD, 20);
		titulo.setFont(fonte);

		panelJogador1 = new PanelJogador("Jogador 1",
				IndicadorJogador.JOGADOR1, this);
		panelJogador2 = new PanelJogador("Jogador 2",
				IndicadorJogador.JOGADOR2, this);
		memoryAnalysisEnabled = new JCheckBox("Habilitar Análise de Memória");
		panelAcoes = new PanelAcoes(uiDamas);

		JSeparator separador = new JSeparator(JSeparator.HORIZONTAL);

		UIDamas.criarGroupLayoutVertical(this, Short.MAX_VALUE, buttonSobre,
				titulo, panelJogador1, panelJogador2, memoryAnalysisEnabled,
				panelAcoes, separador, compararJogadores, barra);
	}

	private void onClickSobre() {
		JOptionPane.showMessageDialog(this,
				"Jogo desenvolvido por Thiago Mendes Vieira.\n"
						+ "Graduando em Enganharia de Computação no CEFET-MG.");
	}

	private void onClickCompararJogadores() {
		ui.onClickCompararJogadores();
	}

	private void setPanelJogadoresEnabled(boolean enabled) {
		panelJogador1.setPanelJogadoresEnabled(enabled);
		panelJogador2.setPanelJogadoresEnabled(enabled);
	}

	public IndicadorAlgoritmo getAlgoritmoJogador1() {
		return panelJogador1.getAlgoritmo();
	}

	public IndicadorAlgoritmo getAlgoritmoJogador2() {
		return panelJogador2.getAlgoritmo();
	}

	public IndicadorHeuristica getHeuristicaJogador1() {
		return panelJogador1.getHeuristica();
	}

	public IndicadorHeuristica getHeuristicaJogador2() {
		return panelJogador2.getHeuristica();
	}

	public void configurarLayoutParaJogoIniciado() {
		setPanelJogadoresEnabled(false);
		memoryAnalysisEnabled.setEnabled(false);
		compararJogadores.setEnabled(false);
		panelAcoes.configurarLayoutParaJogoIniciado();
	}

	public void configurarLayoutParaJogoFinalizado() {
		setPanelJogadoresEnabled(true);
		memoryAnalysisEnabled.setEnabled(true);
		onPanelJogadorListener_algoritmoChanged();
		panelAcoes.configurarLayoutParaJogoFinalizado();
	}

	public boolean isMemoryAnalysisEnabled() {
		return memoryAnalysisEnabled.isSelected();
	}

	@Override
	public void onPanelJogadorListener_algoritmoChanged() {
		boolean compararEnabled = false;

		if ((!IndicadorAlgoritmo.HUMANO.equals(IndicadorJogador.JOGADOR1
				.getAlgoritmo()))
				&& (!IndicadorAlgoritmo.HUMANO.equals(IndicadorJogador.JOGADOR2
						.getAlgoritmo()))) {
			compararEnabled = true;
		}

		compararJogadores.setEnabled(compararEnabled);
	}

	public void setBarraValue(int value) {
		barra.setValue(value);
	}
}
