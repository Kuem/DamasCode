package br.com.thiagomv.damasCode.view.damas;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import br.com.thiagomv.damasCode.constantes.IndicadorJogador;
import br.com.thiagomv.damasCode.constantes.IndicadorPedraJogador;
import br.com.thiagomv.damasCode.constantes.IndicadorTipoPedra;
import br.com.thiagomv.damasCode.estruturas.Tabuleiro;

/**
 * Esta classe define um JPanel personalizado que cria e agrupa o conjunto de
 * casas do tabuleiro. Os eventos associados às casa do tabuleiro são escutados
 * e repassados ao objeto PanelCasasTabuleiroListener.
 * 
 * @author Thiago
 * 
 */
public final class PanelCasasTabuleiro extends JPanel {
	/**
	 * Versão 1.0.
	 */
	private static final long serialVersionUID = 1L;

	private static final String strResourceDir = "/br/com/thiagomv/damasCode/resources/";
	private static final String strResourceCasaBranca = strResourceDir
			+ "casaBranca64.png";
	private static final String strResourceCasaPretaVazia = strResourceDir
			+ "casaPreta64.png";
	private static final String strResourceCasaPretaVaziaSelecionada = strResourceDir
			+ "casaPretaSelecionada.png";
	private static final String strResourceCasaPretaOcupadaNormalJ1 = strResourceDir
			+ "casaPretaOcupadaNormalJ1.png";
	private static final String strResourceCasaPretaOcupadaDamaJ1 = strResourceDir
			+ "casaPretaOcupadaDamaJ1.png";
	private static final String strResourceCasaPretaSelecionadaNormalJ1 = strResourceDir
			+ "casaPretaSelecionadaNormalJ1.png";
	private static final String strResourceCasaPretaSelecionadaDamaJ1 = strResourceDir
			+ "casaPretaSelecionadaDamaJ1.png";
	private static final String strResourceCasaPretaOcupadaNormalJ2 = strResourceDir
			+ "casaPretaOcupadaNormalJ2.png";
	private static final String strResourceCasaPretaOcupadaDamaJ2 = strResourceDir
			+ "casaPretaOcupadaDamaJ2.png";
	private static final String strResourceCasaPretaSelecionadaNormalJ2 = strResourceDir
			+ "casaPretaSelecionadaNormalJ2.png";
	private static final String strResourceCasaPretaSelecionadaDamaJ2 = strResourceDir
			+ "casaPretaSelecionadaDamaJ2.png";

	private final ImageIcon casaBranca = new ImageIcon(getClass().getResource(
			strResourceCasaBranca));
	private final ImageIcon casaPretaVazia = new ImageIcon(getClass()
			.getResource(strResourceCasaPretaVazia));
	private final ImageIcon casaPretaVaziaSelecionada = new ImageIcon(
			getClass().getResource(strResourceCasaPretaVaziaSelecionada));

	private final ImageIcon casaPretaOcupadaNormalJ1 = new ImageIcon(getClass()
			.getResource(strResourceCasaPretaOcupadaNormalJ1));
	private final ImageIcon casaPretaOcupadaDamaJ1 = new ImageIcon(getClass()
			.getResource(strResourceCasaPretaOcupadaDamaJ1));
	private final ImageIcon casaPretaSelecionadaNormalJ1 = new ImageIcon(
			getClass().getResource(strResourceCasaPretaSelecionadaNormalJ1));
	private final ImageIcon casaPretaSelecionadaDamaJ1 = new ImageIcon(
			getClass().getResource(strResourceCasaPretaSelecionadaDamaJ1));

	private final ImageIcon casaPretaOcupadaNormalJ2 = new ImageIcon(getClass()
			.getResource(strResourceCasaPretaOcupadaNormalJ2));
	private final ImageIcon casaPretaOcupadaDamaJ2 = new ImageIcon(getClass()
			.getResource(strResourceCasaPretaOcupadaDamaJ2));
	private final ImageIcon casaPretaSelecionadaNormalJ2 = new ImageIcon(
			getClass().getResource(strResourceCasaPretaSelecionadaNormalJ2));
	private final ImageIcon casaPretaSelecionadaDamaJ2 = new ImageIcon(
			getClass().getResource(strResourceCasaPretaSelecionadaDamaJ2));

	private final int numLinhas;
	private final int numColunas;
	private final PanelCasasTabuleiroListener listener;
	private final LabelCasa[][] casas;
	private boolean enabled;

	/**
	 * Construtor da classe.
	 * 
	 * @param linhas
	 *            Número de linhas do tabuleiro.
	 * @param colunas
	 *            Número de colunas do tabuleiro.
	 * @param listener
	 *            Objeto que recebe os eventos associados às casas do tabuleiro.
	 */
	public PanelCasasTabuleiro(int linhas, int colunas,
			PanelCasasTabuleiroListener listener) {
		this.numLinhas = linhas;
		this.numColunas = colunas;
		this.listener = listener;
		this.casas = new LabelCasa[numLinhas][numColunas];
		this.enabled = true;

		setBackground(new java.awt.Color(0, 0, 0));

		LabelCasa labelAux;
		for (int L = 0; L < numLinhas; L++) {
			for (int C = 0; C < numColunas; C++) {
				labelAux = new LabelCasa(L, C);
				if (Tabuleiro.isPosicaoValidaParaHabitar(L, C)) {
					labelAux.setIcon(casaPretaVazia);
				} else {
					labelAux.setIcon(casaBranca);
				}
				labelAux.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent evt) {
						LabelCasa source = (LabelCasa) evt.getSource();
						onClickCasa(source.getLinha(), source.getColuna());
					}
				});
				casas[L][C] = labelAux;
			}
		}

		GroupLayout panelCasasLayout = new GroupLayout(this);
		this.setLayout(panelCasasLayout);

		ParallelGroup grupoParaleloHorizontal = panelCasasLayout
				.createParallelGroup(GroupLayout.Alignment.LEADING);
		for (int L = numLinhas - 1; L >= 0; L--) {
			grupoParaleloHorizontal
					.addGroup(criarGrupoSequencialHorizontalLinha(
							panelCasasLayout, L));
		}
		panelCasasLayout.setHorizontalGroup(grupoParaleloHorizontal);

		SequentialGroup grupoSequencialVertical = panelCasasLayout
				.createSequentialGroup();
		grupoSequencialVertical.addContainerGap();
		for (int L = numLinhas - 1; L >= 0; L--) {
			grupoSequencialVertical.addGroup(criarGrupoParaleloVerticalLinha(
					panelCasasLayout, L));
		}
		grupoSequencialVertical.addContainerGap(GroupLayout.DEFAULT_SIZE,
				Short.MAX_VALUE);
		panelCasasLayout.setVerticalGroup(grupoSequencialVertical);

	}

	private Group criarGrupoParaleloVerticalLinha(GroupLayout groupLayout,
			int linha) {
		ParallelGroup g = groupLayout
				.createParallelGroup(GroupLayout.Alignment.TRAILING);

		for (int C = 0; C < numColunas; C++) {
			g.addComponent(casas[linha][C], GroupLayout.PREFERRED_SIZE,
					GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
		}

		return g;
	}

	private Group criarGrupoSequencialHorizontalLinha(GroupLayout groupLayout,
			int linha) {
		SequentialGroup g = groupLayout.createSequentialGroup();

		g.addContainerGap();
		for (int C = 0; C < numColunas; C++) {
			g.addComponent(casas[linha][C], GroupLayout.PREFERRED_SIZE,
					GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
		}
		g.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);

		return g;
	}

	private void onClickCasa(int linha, int coluna) {
		if (enabled) {
			listener.onPanelCasasTabuleiro_ClickCasa(linha, coluna);
		}
	}

	/**
	 * Habilita/Desabilita o painel.
	 * 
	 * @param enabled
	 *            "true" se o painel deve ser habilitado ou "false", caso
	 *            contrário.
	 */
	public void setPanelCasasTabuleiroEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setImageCasa(int linha, int coluna,
			IndicadorPedraJogador pedra, boolean selecionada) {
		LabelCasa casa = casas[linha][coluna];
		if (pedra == null) {
			if (Tabuleiro.isPosicaoValidaParaHabitar(linha, coluna)) {
				if (selecionada) {
					casa.setIcon(casaPretaVaziaSelecionada);
				} else {
					casa.setIcon(casaPretaVazia);
				}
			} else {
				casa.setIcon(casaBranca);
			}
		} else {
			IndicadorJogador jogador = pedra.getJogador();
			IndicadorTipoPedra tipoPedra = pedra.getTipoPedra();

			switch (jogador) {
			case JOGADOR1:
				switch (tipoPedra) {
				case NORMAL:
					if (selecionada) {
						casa.setIcon(casaPretaSelecionadaNormalJ1);
					} else {
						casa.setIcon(casaPretaOcupadaNormalJ1);
					}
					break;
				case DAMA:
					if (selecionada) {
						casa.setIcon(casaPretaSelecionadaDamaJ1);
					} else {
						casa.setIcon(casaPretaOcupadaDamaJ1);
					}
					break;
				}
				break;
			case JOGADOR2:
				switch (tipoPedra) {
				case NORMAL:
					if (selecionada) {
						casa.setIcon(casaPretaSelecionadaNormalJ2);
					} else {
						casa.setIcon(casaPretaOcupadaNormalJ2);
					}
					break;
				case DAMA:
					if (selecionada) {
						casa.setIcon(casaPretaSelecionadaDamaJ2);
					} else {
						casa.setIcon(casaPretaOcupadaDamaJ2);
					}
					break;
				}
				break;
			}
		}
	}

}
