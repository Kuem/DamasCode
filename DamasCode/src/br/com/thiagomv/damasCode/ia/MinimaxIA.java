package br.com.thiagomv.damasCode.ia;

import java.util.List;

import br.com.thiagomv.damasCode.constantes.IndicadorHeuristica;
import br.com.thiagomv.damasCode.constantes.IndicadorJogador;
import br.com.thiagomv.damasCode.controle.MemoriaController;
import br.com.thiagomv.damasCode.controle.Regras;
import br.com.thiagomv.damasCode.estruturas.EstadoJogo;
import br.com.thiagomv.damasCode.estruturas.Jogada;

public class MinimaxIA extends AbstractIA {
	private Regras regras = new Regras();

	@Override
	public Jogada escolherJogada(List<Jogada> jogadasPermitidas,
			EstadoJogo estadoJogo) {
		int profundidade = definirNivelProfundidade(estadoJogo
				.getJogadorAtual().getHeuristica());

		Nodo nodoInicial = new Nodo(estadoJogo, jogadasPermitidas, false);

		double valor;
		if (IndicadorJogador.JOGADOR1.equals(estadoJogo.getJogadorAtual())) {
			valor = valorMax(nodoInicial, profundidade);
		} else {
			valor = valorMin(nodoInicial, profundidade);
		}

		MemoriaController.contabilizarMemoria();

		return nodoInicial.getJogadaSucessoraAvaliandoMaiorNivelTerminal(valor);
	}

	private int definirNivelProfundidade(IndicadorHeuristica heuristica) {
		return heuristica.getFlag();
	}

	private double valorMax(Nodo nodo, int profundidade) {
		if (isInterrompido()) {
			return 0;
		}

		double utilidade = Double.NEGATIVE_INFINITY;

		if (nodo.isTerminal()) {
			utilidade = nodo.getUtilidade();
			nodo.setNivelTerminal(0);
		} else {
			int proximaProfundidade = profundidade - 1;
			boolean proximosNiveisSaoTerminais = (proximaProfundidade <= 0);
			EstadoJogo estadoJogo = nodo.getEstadoJogo();
			List<Jogada> novasJogadasPermitidas;
			Nodo sucessor;

			for (Jogada jogada : nodo.getJogadasPermitidas()) {
				EstadoJogo novoEstado = Utils.getEstadoSucessor(estadoJogo,
						jogada);

				if (proximosNiveisSaoTerminais) {
					novasJogadasPermitidas = null;
				} else {
					novasJogadasPermitidas = regras
							.calcularJogadasPermitidas(novoEstado);
				}

				sucessor = new Nodo(novoEstado, novasJogadasPermitidas,
						proximosNiveisSaoTerminais);
				nodo.addSucessor(sucessor, jogada);

				utilidade = Math.max(utilidade,
						valorMin(sucessor, proximaProfundidade));

				nodo.setNivelTerminal(Math.max(nodo.getNivelTerminal(),
						1 + sucessor.getNivelTerminal()));
			}
		}

		MemoriaController.contabilizarMemoria();

		nodo.setValor(utilidade);
		return utilidade;
	}

	private double valorMin(Nodo nodo, int profundidade) {
		if (isInterrompido()) {
			return 0;
		}

		double utilidade = Double.POSITIVE_INFINITY;

		if (nodo.isTerminal()) {
			utilidade = nodo.getUtilidade();
			nodo.setNivelTerminal(0);
		} else {
			int proximaProfundidade = profundidade - 1;
			boolean proximosNiveisSaoTerminais = (proximaProfundidade <= 0);
			EstadoJogo estadoJogo = nodo.getEstadoJogo();
			List<Jogada> jogadasPermitidasNovas;
			Nodo sucessor;

			for (Jogada jogada : nodo.getJogadasPermitidas()) {
				EstadoJogo novoEstado = Utils.getEstadoSucessor(estadoJogo,
						jogada);

				if (proximosNiveisSaoTerminais) {
					jogadasPermitidasNovas = null;
				} else {
					jogadasPermitidasNovas = regras
							.calcularJogadasPermitidas(novoEstado);
				}

				sucessor = new Nodo(novoEstado, jogadasPermitidasNovas,
						proximosNiveisSaoTerminais);
				nodo.addSucessor(sucessor, jogada);

				utilidade = Math.min(utilidade,
						valorMax(sucessor, proximaProfundidade));

				nodo.setNivelTerminal(Math.max(nodo.getNivelTerminal(),
						1 + sucessor.getNivelTerminal()));
			}
		}

		MemoriaController.contabilizarMemoria();

		nodo.setValor(utilidade);
		return utilidade;
	}
}
