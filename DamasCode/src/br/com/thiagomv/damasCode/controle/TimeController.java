package br.com.thiagomv.damasCode.controle;

import java.util.ArrayList;
import java.util.List;

import br.com.thiagomv.damasCode.constantes.IndicadorJogador;

public class TimeController {
	private static TimeController instance = null;

	public static TimeController getInstance() {
		if (instance == null) {
			instance = new TimeController();
		}
		return instance;
	}

	@SuppressWarnings("unchecked")
	private final List<Long> temposJogador[] = new List[] {
			new ArrayList<Long>(), new ArrayList<Long>() };
	private final long[] somaTotal = new long[2];
	private final long[] maiorTempo = new long[2];
	private final long[] media = new long[2];
	private final long[] desvioPadrao = new long[2];

	private TimeController() {
		clear();
	}

	public void clear() {
		for (int i = 0; i < 2; i++) {
			temposJogador[i].clear();
			somaTotal[i] = 0;
			maiorTempo[i] = 0;
			media[i] = 0;
			desvioPadrao[i] = 0;
		}
	}

	/**
	 * Adiciona contagem de tempo de jogada do jogador.
	 * 
	 * @param jogador
	 *            Jogador.
	 * @param elapsedTime
	 *            Tempo em nanosegundos.
	 */
	public void logTime(IndicadorJogador jogador, long elapsedTime) {
		temposJogador[jogador.ordinal()].add(new Long(elapsedTime));
	}

	public void processar() {
		long soma;
		long maior;
		for (int i = 0; i < 2; i++) {
			soma = 0;
			maior = -1;
			for (long l : temposJogador[i]) {
				soma += l;
				maior = Math.max(maior, l);
			}
			somaTotal[i] = soma;
			maiorTempo[i] = maior;
			if (temposJogador[i].size() == 0) {
				media[i] = 0;
			} else {
				media[i] = soma / temposJogador[i].size();
			}

			double dp = 0;
			if (temposJogador[i].size() > 1) {
				for (long l : temposJogador[i]) {
					dp += Math.pow(l - media[i], 2);
				}
				dp /= (temposJogador[i].size() - 1);
			}
			desvioPadrao[i] = Math.round(Math.sqrt(dp));
		}
	}

	public long getSomaTotal(IndicadorJogador jogador) {
		return somaTotal[jogador.ordinal()];
	}

	public long getMaiorTempo(IndicadorJogador jogador) {
		return maiorTempo[jogador.ordinal()];
	}

	public long getMediaTempo(IndicadorJogador jogador) {
		return media[jogador.ordinal()];
	}

	public long getDesvioPadrao(IndicadorJogador jogador) {
		return desvioPadrao[jogador.ordinal()];
	}

}
