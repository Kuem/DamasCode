package br.com.thiagomv.damasCode.controle;

import java.util.List;

import br.com.thiagomv.damasCode.constantes.IndicadorAlgoritmo;
import br.com.thiagomv.damasCode.constantes.IndicadorJogador;
import br.com.thiagomv.damasCode.estruturas.EstadoJogo;
import br.com.thiagomv.damasCode.estruturas.Jogada;
import br.com.thiagomv.damasCode.ia.AbstractIA;
import br.com.thiagomv.damasCode.ia.AleatorioIA;
import br.com.thiagomv.damasCode.ia.AlfaBetaIA;
import br.com.thiagomv.damasCode.ia.MinimaxIA;

public class IAController {
	private static AbstractIA instanciaIAJogador1 = null;
	private static AbstractIA instanciaIAJogador2 = null;

	public static void configurarIAParaNovoJogo() {
		IndicadorAlgoritmo algoritmoJogador1 = IndicadorJogador.JOGADOR1
				.getAlgoritmo();
		IndicadorAlgoritmo algoritmoJogador2 = IndicadorJogador.JOGADOR2
				.getAlgoritmo();

		long mem;
		mem = MemoriaController.antesCriarJogador1();

		if (IndicadorAlgoritmo.HUMANO.equals(algoritmoJogador1)) {
			instanciaIAJogador1 = null;
		} else {
			instanciaIAJogador1 = getIA(algoritmoJogador1);
			instanciaIAJogador1.configurarIAParaNovoJogo();
		}

		mem = MemoriaController.aposCriarJogador1() - mem;
		if (ArchitectureSettings.isMemoriaControllerLogEnabled()) {
			ArchitectureSettings.logLine("Total", mem + "\n");
		}

		mem = MemoriaController.antesCriarJogador2();

		if (IndicadorAlgoritmo.HUMANO.equals(algoritmoJogador2)) {
			instanciaIAJogador2 = null;
		} else {
			instanciaIAJogador2 = getIA(algoritmoJogador2);
			instanciaIAJogador2.configurarIAParaNovoJogo();
		}

		mem = MemoriaController.aposCriarJogador2() - mem;
		if (ArchitectureSettings.isMemoriaControllerLogEnabled()) {
			ArchitectureSettings.logLine("Total", mem + "\n");
		}
	}

	private static AbstractIA getIA(IndicadorAlgoritmo algoritmoJogador) {
		AbstractIA instancia = null;
		switch (algoritmoJogador) {
		case ALEATORIO:
			instancia = new AleatorioIA();
			break;
		case MINIMAX:
			instancia = new MinimaxIA();
			break;
		case ALPHA_BETA:
			instancia = new AlfaBetaIA();
			break;
		default:
			throw new RuntimeException(
					"Inteligência Artificial não implementada!");
		}
		return instancia;
	}

	public static void interromper() {
		if (instanciaIAJogador1 != null) {
			instanciaIAJogador1.interromper();
			instanciaIAJogador1 = null;
		}
		if (instanciaIAJogador2 != null) {
			instanciaIAJogador2.interromper();
			instanciaIAJogador2 = null;
		}
	}

	public static void finalizarIA() {
		if (instanciaIAJogador1 != null) {
			instanciaIAJogador1.finalizar();
			instanciaIAJogador1 = null;
		}
		if (instanciaIAJogador2 != null) {
			instanciaIAJogador2.finalizar();
			instanciaIAJogador2 = null;
		}
	}

	public static Jogada escolherJogada(List<Jogada> jogadasPermitidas,
			EstadoJogo ej) {
		Jogada jogada = null;
		if (IndicadorJogador.JOGADOR1.equals(ej.getJogadorAtual())) {
			if (instanciaIAJogador1 != null) {
				jogada = instanciaIAJogador1.escolherJogada(jogadasPermitidas,
						ej);
			}
		} else {
			if (instanciaIAJogador2 != null) {
				jogada = instanciaIAJogador2.escolherJogada(jogadasPermitidas,
						ej);
			}
		}
		return jogada;
	}
}
