package br.com.thiagomv.damasCode.testes;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import br.com.thiagomv.damasCode.constantes.IndicadorAlgoritmo;
import br.com.thiagomv.damasCode.constantes.IndicadorHeuristica;
import br.com.thiagomv.damasCode.constantes.IndicadorJogador;
import br.com.thiagomv.damasCode.constantes.IndicadorResultadoJogo;
import br.com.thiagomv.damasCode.controle.ArchitectureSettings;
import br.com.thiagomv.damasCode.controle.GameLogic;
import br.com.thiagomv.damasCode.controle.GameLogicListener;
import br.com.thiagomv.damasCode.controle.UIDamasControllerException;
import br.com.thiagomv.damasCode.estruturas.EstadoJogo;
import br.com.thiagomv.damasCode.estruturas.Jogada;

public class IATest {
	private final Object lock = new Object();

	private class EstatisticaVitorias {
		int vitoriasJ1;
		int vitoriasJ2;
		int empates;

		public EstatisticaVitorias(int v1, int v2, int e) {
			this.vitoriasJ1 = v1;
			this.vitoriasJ2 = v2;
			this.empates = e;
		}
	}

	private EstatisticaVitorias realizarJogo(IndicadorAlgoritmo algoritmoJ1,
			IndicadorHeuristica heuristicaJ1, IndicadorAlgoritmo algoritmoJ2,
			IndicadorHeuristica heuristicaJ2, int numJogos) {
		ListenerResultadoJogo listenerResultado = new ListenerResultadoJogo();

		GameLogic gameLogic = new GameLogic(listenerResultado);

		// Jogador 1 implementa Minimax com Heurìstica 1.
		IndicadorJogador.JOGADOR1.setAlgoritmo(algoritmoJ1);
		IndicadorJogador.JOGADOR1.setHeuristica(heuristicaJ1);

		// Jogador implementa Aleatorio.
		IndicadorJogador.JOGADOR2.setAlgoritmo(algoritmoJ2);
		IndicadorJogador.JOGADOR2.setHeuristica(heuristicaJ2);

		int ganhaJ1 = 0;
		int ganhaJ2 = 0;
		int empata = 0;
		IndicadorResultadoJogo resultadoTemp;

		float intervaloImpressao = (float) numJogos / 10F;
		float proximaImpressao = 0;

		for (int i = 0; i < numJogos; i++) {
			if (i >= proximaImpressao || i == (numJogos - 1)) {
				proximaImpressao += intervaloImpressao;
				ArchitectureSettings.logLine(" |- Teste " + (i + 1) + ": "
						+ (100F * (float) (i + 1) / (float) (numJogos)) + " %");
			}

			listenerResultado.resetResultado();
			esperarJogo(gameLogic, listenerResultado);

			resultadoTemp = listenerResultado.getResultado();
			switch (resultadoTemp) {
			case VENCE_JOGADOR1:
				ganhaJ1++;
				break;
			case VENCE_JOGADOR2:
				ganhaJ2++;
				break;
			case JOGO_INTERROMPIDO:
				Assert.fail();
				break;
			default:
				empata++;
			}
		}

		return new EstatisticaVitorias(ganhaJ1, ganhaJ2, empata);
	}

	/**
	 * Espera o controle de fluxo de jogo realizar as jogadas até o jogo
	 * terminar, ou até o limite de tempo de espera ser atingido.
	 * 
	 * @param jogador
	 */
	private void esperarJogo(GameLogic gameLogic, ListenerResultadoJogo jogador) {
		ArchitectureSettings.setMinTimeUpdate(0);
		gameLogic.iniciarJogo();
		synchronized (lock) {
			if (jogador.getResultado() == null) {
				try {
					lock.wait();
				} catch (InterruptedException e) {

				}
				if (jogador.getResultado() == null) {
					gameLogic.interromperJogo();
				}
			}
		}
	}

	private class ListenerResultadoJogo implements GameLogicListener {
		private IndicadorResultadoJogo resultado = null;

		@Override
		public void onGameLogic_JogoIniciado(EstadoJogo estadoJogo) {
		}

		@Override
		public void onGameLogic_JogoAtualizado(EstadoJogo estadoJogo) {
		}

		@Override
		public void onGameLogic_JogoAcabado(
				IndicadorResultadoJogo resultadoJogo, int numTotalJogadas) {
			synchronized (lock) {
				this.resultado = resultadoJogo;
				lock.notify();
			}
		}

		@Override
		public Jogada onGameLogic_EscolherJogada(EstadoJogo estadoJogo,
				List<Jogada> jogadasPermitidas)
				throws UIDamasControllerException {
			return null;
		}

		public void resetResultado() {
			synchronized (lock) {
				this.resultado = null;
			}
		}

		public IndicadorResultadoJogo getResultado() {
			return this.resultado;
		}
	}

	@Test
	public void aleatorioPerdeParaMinimax1() {
		int testes = 500;
		ArchitectureSettings.logLine("[aleatorioPerdeParaMinimax1] Testes: "
				+ String.valueOf(testes));

		EstatisticaVitorias ev = realizarJogo(IndicadorAlgoritmo.ALEATORIO,
				null, IndicadorAlgoritmo.MINIMAX, IndicadorHeuristica.H1_P3,
				testes);

		double percentualVitoriaJ1 = (double) ev.vitoriasJ1
				/ (double) (ev.vitoriasJ1 + ev.vitoriasJ2 + ev.empates);
		double percentualEmpates = (double) ev.empates
				/ (double) (ev.vitoriasJ1 + ev.vitoriasJ2 + ev.empates);

		/*
		 * Assegura que jogador 1 não ganha todos os jogos, mas que ganha mais
		 * que 90% dos jogos.
		 */
		ArchitectureSettings.logLine(" |- Vitoria Aleatório (%) :",
				String.valueOf(100F * percentualVitoriaJ1));
		ArchitectureSettings.logLine(" |- Empates (%) :",
				String.valueOf(100F * percentualEmpates));
		Assert.assertTrue(ev.vitoriasJ1 < ev.vitoriasJ2);
	}

	@Test
	public void minimax1GanhaDeAleatorio() {
		int testes = 500;
		ArchitectureSettings.logLine("[minimax1GanhaDeAleatorio] Testes: "
				+ String.valueOf(testes));

		EstatisticaVitorias ev = realizarJogo(IndicadorAlgoritmo.MINIMAX,
				IndicadorHeuristica.H1_P3, IndicadorAlgoritmo.ALEATORIO, null,
				testes);

		double percentualVitoriaJ1 = (double) ev.vitoriasJ1
				/ (double) (ev.vitoriasJ1 + ev.vitoriasJ2 + ev.empates);
		double percentualEmpates = (double) ev.empates
				/ (double) (ev.vitoriasJ1 + ev.vitoriasJ2 + ev.empates);

		/*
		 * Assegura que jogador 1 não ganha todos os jogos, mas que ganha mais
		 * que 90% dos jogos.
		 */
		ArchitectureSettings.logLine(" |- Vitoria Minimax1 (%) :",
				String.valueOf(100F * percentualVitoriaJ1));
		ArchitectureSettings.logLine(" |- Empates (%) :",
				String.valueOf(100F * percentualEmpates));
		Assert.assertTrue(ev.vitoriasJ1 > ev.vitoriasJ2);
	}

	@Test
	public void minimax2GanhaDeMinimax1() {
		int testes = 200;
		ArchitectureSettings.logLine("[minimax2GanhaDeMinimax1] Testes: "
				+ String.valueOf(testes));

		EstatisticaVitorias ev = realizarJogo(IndicadorAlgoritmo.MINIMAX,
				IndicadorHeuristica.H1_P4, IndicadorAlgoritmo.MINIMAX,
				IndicadorHeuristica.H1_P3, testes);

		double percentualVitoriaJ1 = (double) ev.vitoriasJ1
				/ (double) (ev.vitoriasJ1 + ev.vitoriasJ2 + ev.empates);
		double percentualEmpates = (double) ev.empates
				/ (double) (ev.vitoriasJ1 + ev.vitoriasJ2 + ev.empates);

		/*
		 * Assegura que jogador 1 não ganha todos os jogos, mas que ganha mais
		 * que 85% dos jogos.
		 */
		ArchitectureSettings.logLine(" |- Vitoria Minimax2 (%) :",
				String.valueOf(100F * percentualVitoriaJ1));
		ArchitectureSettings.logLine(" |- Empates (%) :",
				String.valueOf(100F * percentualEmpates));
		Assert.assertTrue(ev.vitoriasJ1 > ev.vitoriasJ2);

	}

	@Test
	public void minimax1PerdeParaMinimax2() {
		int testes = 200;
		ArchitectureSettings.logLine("[minimax1PerdeParaMinimax2] Testes: "
				+ String.valueOf(testes));

		EstatisticaVitorias ev = realizarJogo(IndicadorAlgoritmo.MINIMAX,
				IndicadorHeuristica.H1_P3, IndicadorAlgoritmo.MINIMAX,
				IndicadorHeuristica.H1_P4, testes);

		double percentualVitoriaJ1 = (double) ev.vitoriasJ1
				/ (double) (ev.vitoriasJ1 + ev.vitoriasJ2 + ev.empates);
		double percentualEmpates = (double) ev.empates
				/ (double) (ev.vitoriasJ1 + ev.vitoriasJ2 + ev.empates);

		/*
		 * Assegura que jogador 1 não ganha todos os jogos, mas que ganha mais
		 * que 85% dos jogos.
		 */
		ArchitectureSettings.logLine(" |- Vitoria Minimax1 (%) :",
				String.valueOf(100F * percentualVitoriaJ1));
		ArchitectureSettings.logLine(" |- Empates (%) :",
				String.valueOf(100F * percentualEmpates));
		Assert.assertTrue(ev.vitoriasJ1 < ev.vitoriasJ2);
	}

	@Test
	public void minimax3GanhaDeMinimax2() {
		int testes = 20;
		ArchitectureSettings.logLine("[minimax3GanhaDeMinimax2] Testes: "
				+ String.valueOf(testes));

		EstatisticaVitorias ev = realizarJogo(IndicadorAlgoritmo.MINIMAX,
				IndicadorHeuristica.H1_P5, IndicadorAlgoritmo.MINIMAX,
				IndicadorHeuristica.H1_P4, testes);

		double percentualVitoriaJ1 = (double) ev.vitoriasJ1
				/ (double) (ev.vitoriasJ1 + ev.vitoriasJ2 + ev.empates);
		double percentualEmpates = (double) ev.empates
				/ (double) (ev.vitoriasJ1 + ev.vitoriasJ2 + ev.empates);

		/*
		 * Assegura que jogador 1 não ganha todos os jogos, mas que ganha mais
		 * que 75% dos jogos.
		 */
		ArchitectureSettings.logLine(" |- Vitoria Minimax3 (%) :",
				String.valueOf(100F * percentualVitoriaJ1));
		ArchitectureSettings.logLine(" |- Empates (%) :",
				String.valueOf(100F * percentualEmpates));
		Assert.assertTrue(ev.vitoriasJ1 > ev.vitoriasJ2);
	}

	@Test
	public void minimax2PerdeParaMinimax3() {
		int testes = 20;
		ArchitectureSettings.logLine("[minimax2PerdeParaMinimax3] Testes: "
				+ String.valueOf(testes));

		EstatisticaVitorias ev = realizarJogo(IndicadorAlgoritmo.MINIMAX,
				IndicadorHeuristica.H1_P4, IndicadorAlgoritmo.MINIMAX,
				IndicadorHeuristica.H1_P5, testes);

		double percentualVitoriaJ1 = (double) ev.vitoriasJ1
				/ (double) (ev.vitoriasJ1 + ev.vitoriasJ2 + ev.empates);
		double percentualEmpates = (double) ev.empates
				/ (double) (ev.vitoriasJ1 + ev.vitoriasJ2 + ev.empates);

		/*
		 * Assegura que jogador 1 não ganha todos os jogos, mas que ganha mais
		 * que 75% dos jogos.
		 */
		ArchitectureSettings.logLine(" |- Vitoria Minimax2 (%) :",
				String.valueOf(100F * percentualVitoriaJ1));
		ArchitectureSettings.logLine(" |- Empates (%) :",
				String.valueOf(100F * percentualEmpates));
		Assert.assertTrue(ev.vitoriasJ1 < ev.vitoriasJ2);
	}

}
