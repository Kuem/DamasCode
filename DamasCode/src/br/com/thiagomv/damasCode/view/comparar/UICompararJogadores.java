package br.com.thiagomv.damasCode.view.comparar;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.GroupLayout.Alignment;

import br.com.thiagomv.damasCode.constantes.IndicadorJogador;
import br.com.thiagomv.damasCode.estruturas.RelatorioComparacao;
import br.com.thiagomv.damasCode.view.damas.UIDamas;

public class UICompararJogadores extends JFrame {

	/**
	 * Versão 1.0.
	 */
	private static final long serialVersionUID = 1L;

	private static final String NL = "\n";
	private static final String NP = "\n\n";

	private static double ESCALA_TEMPO = 1.0 / Math.pow(10, 6);
	private static final String _MS = " ms";

	private static double ESCALA_MEMORIA = 1.0 / Math.pow(1024, 1);
	private static final String _KB = " KB";

	private final JFrame uiDamas;

	private final JPanel panelInfoJogadores;
	private final PanelInfoJogador infoJogador1;
	private final PanelInfoJogador infoJogador2;
	private final JTextArea texto;

	// private final RelatorioComparacao relatorio;

	public UICompararJogadores(JFrame frame, RelatorioComparacao rc) {
		uiDamas = frame;
		// relatorio = rc;

		panelInfoJogadores = new JPanel();
		infoJogador1 = new PanelInfoJogador("Jogador 1",
				IndicadorJogador.JOGADOR1);
		infoJogador2 = new PanelInfoJogador("Jogador 2",
				IndicadorJogador.JOGADOR2);

		// Layout algoritmos...
		UIDamas.criarGroupLayoutHorizontal(panelInfoJogadores, Short.MAX_VALUE,
				Alignment.LEADING, infoJogador1, infoJogador2);

		texto = new JTextArea();
		texto.setText(getTextoRelatorio(rc));

		// Layout algoritmos...
		UIDamas.criarGroupLayoutVertical(this.getContentPane(),
				Short.MAX_VALUE, panelInfoJogadores, texto);

		addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				uiDamas.setVisible(true);
				uiDamas.setEnabled(true);
			};
		});

		pack();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	private String getTextoRelatorio(RelatorioComparacao rc) {
		rc.setEscalaTempo(ESCALA_TEMPO);
		rc.setEscalaMemoria(ESCALA_MEMORIA);

		StringBuilder sb = new StringBuilder();

		sb.append("Jogos analizados: ").append(rc.numJogos).append(NP);

		sb.append("Vitórias J1 / J2: ")
				.append(rc.getVitorias(IndicadorJogador.JOGADOR1))
				.append(" / ")
				.append(rc.getVitorias(IndicadorJogador.JOGADOR2)).append(NL);
		sb.append("Empates: ").append(rc.getEmpates()).append(NL);
		sb.append("Percentuais (Vitórias J1 / Vitórias J2 / Empates): ")
				.append(UIDamas.valorComCasasDecimais((float) rc
						.getVitorias(IndicadorJogador.JOGADOR1)
						/ (float) rc.numJogos))
				.append(" / ")
				.append(UIDamas.valorComCasasDecimais((float) rc
						.getVitorias(IndicadorJogador.JOGADOR2)
						/ (float) rc.numJogos))
				.append(" / ")
				.append(UIDamas.valorComCasasDecimais((float) rc.getEmpates()
						/ (float) rc.numJogos)).append(NP);

		sb.append("### Valores absolutos ###").append(NL);

		sb.append("Maior tempo J1: ")
				.append(UIDamas.valorComCasasDecimais(rc
						.getMaiorTempo(IndicadorJogador.JOGADOR1))).append(_MS)
				.append(NL);
		sb.append("Maior tempo J2: ")
				.append(UIDamas.valorComCasasDecimais(rc
						.getMaiorTempo(IndicadorJogador.JOGADOR2))).append(_MS)
				.append(NP);
		if (rc.analiseMemoria) {
			sb.append("Maior memória J1: ")
					.append(UIDamas.valorComCasasDecimais(rc
							.getMaiorMemoria(IndicadorJogador.JOGADOR1)))
					.append(_KB).append(NL);
			sb.append("Maior memória J2: ")
					.append(UIDamas.valorComCasasDecimais(rc
							.getMaiorMemoria(IndicadorJogador.JOGADOR2)))
					.append(_KB).append(NP);
		}

		sb.append("### Valores médios ###").append(NL);
		sb.append("Total de jogadas: ")
				.append(UIDamas.valorComCasasDecimais(rc.getMediaTotalJogadas()))
				.append(NL);

		sb.append("Maior tempo J1: ")
				.append(UIDamas.valorComCasasDecimais(rc
						.getMediaMaioresTempos(IndicadorJogador.JOGADOR1)))
				.append(_MS).append(NL);
		sb.append("Maior tempo J2: ")
				.append(UIDamas.valorComCasasDecimais(rc
						.getMediaMaioresTempos(IndicadorJogador.JOGADOR2)))
				.append(_MS).append(NL);
		sb.append("Tempo médio J1: ")
				.append(UIDamas.valorComCasasDecimais(rc
						.getMediaTemposMedios(IndicadorJogador.JOGADOR1)))
				.append(_MS).append(NL);
		sb.append("Tempo médio J2: ")
				.append(UIDamas.valorComCasasDecimais(rc
						.getMediaTemposMedios(IndicadorJogador.JOGADOR2)))
				.append(_MS).append(NL);
		sb.append("Desvio padrão médio J1: ")
				.append(UIDamas.valorComCasasDecimais(rc
						.getMediaTemposDesvioPadrao(IndicadorJogador.JOGADOR1)))
				.append(_MS).append(NL);
		sb.append("Desvio padrão médio J2: ")
				.append(UIDamas.valorComCasasDecimais(rc
						.getMediaTemposDesvioPadrao(IndicadorJogador.JOGADOR2)))
				.append(_MS).append(NP);

		if (rc.analiseMemoria) {
			sb.append("Maior memória J1: ")
					.append(UIDamas.valorComCasasDecimais(rc
							.getMediaMaioresMemorias(IndicadorJogador.JOGADOR1)))
					.append(_KB).append(NL);
			sb.append("Maior memória J2: ")
					.append(UIDamas.valorComCasasDecimais(rc
							.getMediaMaioresMemorias(IndicadorJogador.JOGADOR2)))
					.append(_KB).append(NL);
			sb.append("Memória média J1: ")
					.append(UIDamas.valorComCasasDecimais(rc
							.getMediaMemoriasMedias(IndicadorJogador.JOGADOR1)))
					.append(_KB).append(NL);
			sb.append("Memória média J2: ")
					.append(UIDamas.valorComCasasDecimais(rc
							.getMediaMemoriasMedias(IndicadorJogador.JOGADOR2)))
					.append(_KB).append(NL);
			sb.append("Desvio padrão médio J1: ")
					.append(UIDamas.valorComCasasDecimais(rc
							.getMediaMemoriasDesvioPadrao(IndicadorJogador.JOGADOR1)))
					.append(_KB).append(NL);
			sb.append("Desvio padrão médio J2: ")
					.append(UIDamas.valorComCasasDecimais(rc
							.getMediaMemoriasDesvioPadrao(IndicadorJogador.JOGADOR2)))
					.append(_KB).append(NP);
		}

		return sb.toString();
	}

}
