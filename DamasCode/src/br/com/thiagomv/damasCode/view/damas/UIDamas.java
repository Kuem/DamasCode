package br.com.thiagomv.damasCode.view.damas;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.LayoutManager;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

import br.com.thiagomv.damasCode.constantes.IndicadorPedraJogador;
import br.com.thiagomv.damasCode.controle.ArchitectureSettings;
import br.com.thiagomv.damasCode.controle.CompararJogadoresController;
import br.com.thiagomv.damasCode.controle.MemoriaController;
import br.com.thiagomv.damasCode.controle.UIDamasController;
import br.com.thiagomv.damasCode.controle.UIDamasControllerException;
import br.com.thiagomv.damasCode.estruturas.RelatorioComparacao;
import br.com.thiagomv.damasCode.view.comparar.UICompararJogadores;

/**
 * Esta classe define um JFrame personalizado que cria e controla toda a
 * interface de usuário da aplicação.
 * 
 * @author Thiago
 * 
 */
public final class UIDamas extends JFrame implements
		PanelCasasTabuleiroListener, PanelAcoesListener {
	/**
	 * Versão 1.0.
	 */
	private static final long serialVersionUID = 1L;
	private static final String DECIMAL_FORMAT = "%.3f";

	private final UIDamasController controller;
	private final CompararJogadoresController comparadorController;
	private boolean comparando = false;

	private final PanelMenu panelMenu;
	private final JPanel panelCentral;
	private final PanelCasasTabuleiro panelCasasTabuleiro;
	private final PanelEstatisticas panelEstatisticas;

	/**
	 * Contrutor da classe.
	 */
	public UIDamas() {
		this.controller = new UIDamasController(this);
		this.comparadorController = new CompararJogadoresController(this);

		panelMenu = new PanelMenu(this);
		panelCentral = new JPanel();
		panelCasasTabuleiro = new PanelCasasTabuleiro(
				ArchitectureSettings.getNumLinhas(),
				ArchitectureSettings.getNumColunas(), this);
		panelEstatisticas = new PanelEstatisticas();

		// Layout Conteiner Tabuleiro...
		criarGroupLayoutVertical(panelCentral, GroupLayout.PREFERRED_SIZE,
				panelCasasTabuleiro);

		// Layout UIDamas...
		criarGroupLayoutHorizontal(getContentPane(),
				GroupLayout.PREFERRED_SIZE, GroupLayout.Alignment.LEADING,
				panelMenu, panelCentral, panelEstatisticas);

		desabilitarTabuleiroParaJogadorHumano();
		pack();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	// #####################################
	// # Métodos chamados por UIPanelAcoes #
	// #####################################
	@Override
	public void onPanelAcoes_ClickIniciarJogo() {
		comparando = false;
		try {
			controller.iniciarJogo(panelMenu.getAlgoritmoJogador1(),
					panelMenu.getHeuristicaJogador1(),
					panelMenu.getAlgoritmoJogador2(),
					panelMenu.getHeuristicaJogador2());
		} catch (UIDamasControllerException e) {
			ArchitectureSettings.logLine("ERRO", e.getMessage());
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

	@Override
	public void onPanelAcoes_ClickInterromperJogo() {
		if (comparando) {
			comparadorController.interromperComparacao();
		} else {
			controller.interromperJogo();
		}
		comparando = false;
	}

	// ##############################################
	// # Métodos chamados por UIPanelCasasTabuleiro #
	// ##############################################
	@Override
	public void onPanelCasasTabuleiro_ClickCasa(int linha, int coluna) {
		try {
			controller.onClickCasa(linha, coluna);
		} catch (UIDamasControllerException e) {
			ArchitectureSettings.logLine("ERRO", e.getMessage());
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

	// ##########################################
	// # Métodos chamados por UIDamasController #
	// ##########################################
	public void habilitarTabuleiroParaJogadorHumano() {
		panelCasasTabuleiro.setPanelCasasTabuleiroEnabled(true);
	}

	public void desabilitarTabuleiroParaJogadorHumano() {
		panelCasasTabuleiro.setPanelCasasTabuleiroEnabled(false);
	}

	public void configurarLayoutParaJogoIniciado() {
		MemoriaController.setMemoryAnalysisEnabled(panelMenu
				.isMemoryAnalysisEnabled());
		panelMenu.configurarLayoutParaJogoIniciado();
		desabilitarTabuleiroParaJogadorHumano();
		panelEstatisticas.reset();
		pack();
	}

	public void configurarLayoutParaJogoFinalizado(int numJogadas) {
		panelMenu.configurarLayoutParaJogoFinalizado();
		desabilitarTabuleiroParaJogadorHumano();
		panelEstatisticas.update(numJogadas);
		panelMenu.setBarraValue(0);
		pack();
	}

	public void setImageCasa(int linha, int coluna,
			IndicadorPedraJogador pedra, boolean selecionada) {
		panelCasasTabuleiro.setImageCasa(linha, coluna, pedra, selecionada);
	}

	public static LayoutManager criarGroupLayoutVertical(Container panel,
			int sizeMax, JComponent... componentes) {

		GroupLayout panelLayout = new GroupLayout(panel);
		panel.setLayout(panelLayout);

		ParallelGroup parallelGroupAlgoritmos = panelLayout
				.createParallelGroup(GroupLayout.Alignment.LEADING);
		for (int i = 0; i < componentes.length; i++) {
			parallelGroupAlgoritmos.addComponent(componentes[i],
					GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
					sizeMax);
		}
		panelLayout.setHorizontalGroup(parallelGroupAlgoritmos);

		SequentialGroup sequencialGroupAlgoritmos = panelLayout
				.createSequentialGroup();
		sequencialGroupAlgoritmos.addContainerGap();
		for (int i = 0; i < componentes.length - 1; i++) {
			sequencialGroupAlgoritmos.addComponent(componentes[i],
					GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
					sizeMax);
			sequencialGroupAlgoritmos
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		}
		sequencialGroupAlgoritmos
				.addComponent(componentes[componentes.length - 1]);
		sequencialGroupAlgoritmos.addContainerGap(GroupLayout.DEFAULT_SIZE,
				Short.MAX_VALUE);

		panelLayout.setVerticalGroup(panelLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(
				GroupLayout.Alignment.TRAILING, sequencialGroupAlgoritmos));

		return panelLayout;
	}

	public static LayoutManager criarGroupLayoutHorizontal(Container panel,
			int sizeMax, Alignment alinhamento, JComponent... componentes) {

		GroupLayout panelLayout = new GroupLayout(panel);
		panel.setLayout(panelLayout);

		SequentialGroup sequencialGroup = panelLayout.createSequentialGroup();
		sequencialGroup.addContainerGap();
		for (int i = 0; i < componentes.length - 1; i++) {
			sequencialGroup.addComponent(componentes[i],
					GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
					sizeMax);
			sequencialGroup
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		}
		sequencialGroup.addComponent(componentes[componentes.length - 1]);
		sequencialGroup.addContainerGap(GroupLayout.DEFAULT_SIZE,
				Short.MAX_VALUE);
		panelLayout.setHorizontalGroup(sequencialGroup);

		ParallelGroup parallelGroup = panelLayout
				.createParallelGroup(alinhamento);
		for (int i = 0; i < componentes.length; i++) {
			parallelGroup.addComponent(componentes[i],
					GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
					sizeMax);
		}
		panelLayout.setVerticalGroup(parallelGroup);

		return panelLayout;
	}

	public void onClickCompararJogadores() {
		String numJogos = JOptionPane
				.showInputDialog("Informe a quantidade de jogos a serem realizados:");
		int nj;

		try {
			nj = Integer.parseInt(numJogos);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Número inválido!");
			return;
		}

		configurarLayoutParaJogoIniciado();
		panelMenu.setBarraValue(0);

		comparando = true;
		comparadorController.iniciarComparacao(nj);
	}

	public void updateBarra(int value) {
		panelMenu.setBarraValue(value);
		repaint();
	}

	public void throwException(Exception e) {
		ArchitectureSettings.logLine("ERRO", e.getMessage());
		JOptionPane.showMessageDialog(this, e.getMessage());
	}

	public void exibirRelatorio(final RelatorioComparacao rc) {
		if (rc == null) {
			return;
		}

		final JFrame frame = this;

		this.setEnabled(false);
		this.setVisible(false);

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new UICompararJogadores(frame, rc).setVisible(true);
			}
		});
	}

	public static String valorComCasasDecimais(double valor) {
		return String.format(DECIMAL_FORMAT, valor);
	}
}
