package br.com.thiagomv.damasCode.view.damas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Esta classe define um JPanel personalizado que cria e agrupa o conjunto de
 * botões de ações da aplicação. Os eventos associados aos botões são escutados
 * e repassados ao objeto PanelAcoesListener.
 * 
 * @author Thiago
 * 
 */
public final class PanelAcoes extends JPanel {
	/**
	 * Versão 1.0.
	 */
	private static final long serialVersionUID = 1L;

	private final JButton buttonIniciarJogo;
	private final JButton buttonInterromper;
	private final PanelAcoesListener listener;

	/**
	 * Construtor da classe.
	 */
	public PanelAcoes(PanelAcoesListener listener) {
		this.listener = listener;

		buttonIniciarJogo = new JButton("Iniciar Jogo");
		buttonIniciarJogo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				onClickIniciarJogo();
			}
		});

		buttonInterromper = new JButton("Interromper");
		buttonInterromper.setEnabled(false);
		buttonInterromper.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				onClickInterromperJogo();
			}
		});

		UIDamas.criarGroupLayoutHorizontal(this, Short.MAX_VALUE,
				Alignment.CENTER, buttonIniciarJogo, buttonInterromper);
	}

	private void onClickIniciarJogo() {
		listener.onPanelAcoes_ClickIniciarJogo();
	}

	private void onClickInterromperJogo() {
		listener.onPanelAcoes_ClickInterromperJogo();
	}

	public void configurarLayoutParaJogoIniciado() {
		buttonIniciarJogo.setEnabled(false);
		buttonInterromper.setEnabled(true);
	}

	public void configurarLayoutParaJogoFinalizado() {
		buttonIniciarJogo.setEnabled(true);
		buttonInterromper.setEnabled(false);
	}
}
