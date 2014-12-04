package br.com.thiagomv.damasCode.controle;

import java.util.List;

import br.com.thiagomv.damasCode.constantes.IndicadorJogador;
import br.com.thiagomv.damasCode.constantes.IndicadorResultadoJogo;
import br.com.thiagomv.damasCode.estruturas.EstadoJogo;
import br.com.thiagomv.damasCode.estruturas.Jogada;
import br.com.thiagomv.damasCode.estruturas.RelatorioComparacao;
import br.com.thiagomv.damasCode.view.damas.UIDamas;

public class CompararJogadoresController implements GameLogicListener {
	private static final long LIMITE_ESPERA = Long.MAX_VALUE;

	private final UIDamas ui;
	private final GameLogic gameLogic;

	private IndicadorResultadoJogo resultado;
	private int numJogadas;

	public CompararJogadoresController(UIDamas ui) {
		this.ui = ui;
		this.gameLogic = new GameLogic(this);
	}

	public void iniciarComparacao(final int numJogos) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				ArchitectureSettings.setMinTimeUpdate(0);
				RelatorioComparacao rc = null;
				try {
					rc = realizarJogo(numJogos);
				} catch (Exception e) {
					ui.configurarLayoutParaJogoFinalizado(0);
					ArchitectureSettings.resetMinTimeUpdate();
					
					ui.throwException(e);
				}
				ui.configurarLayoutParaJogoFinalizado(0);
				ArchitectureSettings.resetMinTimeUpdate();

				ui.exibirRelatorio(rc);
			}
		}).start();
	}

	public void interromperComparacao() {
		gameLogic.interromperJogo();
	}

	private final Object lock = new Object();

	private RelatorioComparacao realizarJogo(int numJogos) throws Exception {
		IndicadorResultadoJogo resultadoTemp;

		float intervaloImpressao = 1F;
		float proximaImpressao = 0;

		IndicadorResultadoJogo[] resultados = new IndicadorResultadoJogo[numJogos];
		int[] totalJogadas = new int[numJogos];
		long[][] maiorTempo = new long[2][numJogos];
		long[][] tempoMedio = new long[2][numJogos];
		long[][] tempoDesvioPadrao = new long[2][numJogos];
		double[][] maiorMemoria = new double[2][numJogos];
		double[][] memoriaMedia = new double[2][numJogos];
		IndicadorJogador[] jogadores = IndicadorJogador.values();

		for (int i = 0; i < numJogos; i++) {
			if (i >= proximaImpressao || i == (numJogos - 1)) {
				proximaImpressao += intervaloImpressao;
				int value = (int) (100F * (float) (i) / (float) (numJogos));
				ui.updateBarra(value);
			}

			this.resetResultado();
			this.numJogadas = 0;
			esperarJogo();
			resultadoTemp = this.getResultado();

			resultados[i] = resultadoTemp;
			totalJogadas[i] = this.numJogadas;
			for (int j = 0; j < 2; j++) {
				maiorTempo[j][i] = TimeController.getInstance().getMaiorTempo(
						jogadores[j]);
				tempoMedio[j][i] = TimeController.getInstance().getMediaTempo(
						jogadores[j]);
				tempoDesvioPadrao[j][i] = TimeController.getInstance()
						.getDesvioPadrao(jogadores[j]);
				maiorMemoria[j][i] = MemoriaController
						.getMaiorUso(jogadores[j]);
				memoriaMedia[j][i] = MemoriaController
						.getMediaUso(jogadores[j]);
			}

			if (resultadoTemp == null) {
				throw new Exception("Erro na comparação!");
			} else if (IndicadorResultadoJogo.JOGO_INTERROMPIDO
					.equals(resultadoTemp)) {
				throw new Exception("Comparação interrompida!");
			}
		}
		ui.updateBarra(100);

		return new RelatorioComparacao(numJogos, resultados, totalJogadas,
				maiorTempo, tempoMedio, tempoDesvioPadrao, maiorMemoria, memoriaMedia,
				MemoriaController.isMemoryAnalysisEnabled());
	}

	/**
	 * Espera o controle de fluxo de jogo realizar as jogadas até o jogo
	 * terminar, ou até o limite de tempo de espera ser atingido.
	 * 
	 * @throws Exception
	 */
	private void esperarJogo() throws Exception {
		gameLogic.iniciarJogo();
		synchronized (lock) {
			if (this.getResultado() == null) {
				try {
					lock.wait(LIMITE_ESPERA);
				} catch (InterruptedException e) {
					throw new Exception("A comparação foi interrompida!");
				}
				if (this.getResultado() == null) {
					gameLogic.interromperJogo();
				}
			}
		}
	}

	public void resetResultado() {
		synchronized (lock) {
			this.resultado = null;
		}
	}

	public IndicadorResultadoJogo getResultado() {
		synchronized (lock) {
			return this.resultado;
		}
	}

	@Override
	public void onGameLogic_JogoIniciado(EstadoJogo estadoJogo) {
	}

	@Override
	public void onGameLogic_JogoAtualizado(EstadoJogo estadoJogo) {
	}

	@Override
	public void onGameLogic_JogoAcabado(IndicadorResultadoJogo resultadoJogo,
			int numTotalJogadas) {
		synchronized (lock) {
			this.resultado = resultadoJogo;
			this.numJogadas = numTotalJogadas;
			lock.notify();
		}
	}

	@Override
	public Jogada onGameLogic_EscolherJogada(EstadoJogo estadoJogo,
			List<Jogada> jogadasPermitidas) throws UIDamasControllerException {
		throw new UIDamasControllerException(
				"Ocorreu um erro inesperado no sistema!");
	}
}
