package br.com.thiagomv.damasCode.estruturas;

import br.com.thiagomv.damasCode.constantes.IndicadorJogador;
import br.com.thiagomv.damasCode.constantes.IndicadorResultadoJogo;

public class RelatorioComparacao {

	// Dados brutos...
	public final int numJogos;
	private final IndicadorResultadoJogo[] resultados;
	private final int[] totalJogadas;
	private final long[][] maiorTempo;
	private final long[][] tempoMedio;
	private final long[][] tempoDesvioPadrao;
	private final double[][] maiorMemoria;
	private final double[][] memoriaMedia;
	public final boolean analiseMemoria;

	// Dados computados...
	private long[] _numVitorias;
	private long _numEmpates;
	private long[] _maiorTempoAbsoluto;
	private long[] _mediaMaioresTempos;
	private long[] _mediaTemposMedios;
	private long[] _mediaTemposDesvioPadrao;
	private double[] _maiorMemoriaAbsoluto;
	private double[] _mediaMaioresMemorias;
	private double[] _mediaMemoriasMedias;
	private double[] _mediaMemoriasDesvioPadrao;
	private double _mediaTotalJogadas;

	private double escalaTempo = 1.0;
	private double escalaMemoria = 1.0;

	public RelatorioComparacao(int numJogos,
			IndicadorResultadoJogo[] resultados, int[] totalJogadas,
			long[][] maiorTempo, long[][] tempoMedio,
			long[][] tempoDesvioPadrao, double[][] maiorMemoria,
			double[][] memoriaMedia, boolean analiseMemoria) {
		this.numJogos = numJogos;
		this.resultados = resultados;
		this.totalJogadas = totalJogadas;
		this.maiorTempo = maiorTempo;
		this.tempoMedio = tempoMedio;
		this.tempoDesvioPadrao = tempoDesvioPadrao;
		this.maiorMemoria = maiorMemoria;
		this.memoriaMedia = memoriaMedia;
		this.analiseMemoria = analiseMemoria;

		this.processar();
	}

	private void processar() {
		_numVitorias = new long[2];
		computarResultados();
		compputarMaximosEMedias();
	}

	private void computarResultados() {
		long v1 = 0, v2 = 0, e = 0, tj = 0;

		for (int i = 0; i < numJogos; i++) {
			switch (resultados[i]) {
			case VENCE_JOGADOR1:
				v1++;
				break;
			case VENCE_JOGADOR2:
				v2++;
				break;
			case JOGO_INTERROMPIDO:
				assert (false);
			default:
				e++;
			}
			tj += totalJogadas[i];
		}

		_numVitorias[IndicadorJogador.JOGADOR1.ordinal()] = v1;
		_numVitorias[IndicadorJogador.JOGADOR2.ordinal()] = v2;
		_numEmpates = e;

		_mediaTotalJogadas = (double) tj / (double) numJogos;
	}

	private void compputarMaximosEMedias() {
		int numJogadores = IndicadorJogador.values().length;
		_maiorTempoAbsoluto = new long[numJogadores];
		_mediaMaioresTempos = new long[numJogadores];
		_mediaTemposMedios = new long[numJogadores];
		_mediaTemposDesvioPadrao = new long[numJogadores];

		_maiorMemoriaAbsoluto = new double[numJogadores];
		_mediaMaioresMemorias = new double[numJogadores];
		_mediaMemoriasMedias = new double[numJogadores];
		_mediaMemoriasDesvioPadrao = new double[numJogadores];

		for (int j = 0; j < numJogadores; j++) {
			_maiorTempoAbsoluto[j] = 0;
			_mediaMaioresTempos[j] = 0;
			_mediaTemposMedios[j] = 0;
			_mediaTemposDesvioPadrao[j] = 0;

			_maiorMemoriaAbsoluto[j] = 0;
			_mediaMaioresMemorias[j] = 0;
			_mediaMemoriasMedias[j] = 0;
			_mediaMemoriasDesvioPadrao[j] = 0;

			for (int i = 0; i < numJogos; i++) {
				_maiorTempoAbsoluto[j] = Math.max(_maiorTempoAbsoluto[j],
						maiorTempo[j][i]);
				_mediaMaioresTempos[j] += maiorTempo[j][i];
				_mediaTemposMedios[j] += tempoMedio[j][i];
				_mediaTemposDesvioPadrao[j] += tempoDesvioPadrao[j][i];

				_maiorMemoriaAbsoluto[j] = Math.max(_maiorMemoriaAbsoluto[j],
						maiorMemoria[j][i]);
				_mediaMaioresMemorias[j] += maiorMemoria[j][i];
				_mediaMemoriasMedias[j] += memoriaMedia[j][i];
			}

			_mediaMaioresTempos[j] /= numJogos;
			_mediaTemposMedios[j] /= numJogos;
			_mediaTemposDesvioPadrao[j] /= numJogos;

			_mediaMaioresMemorias[j] /= numJogos;
			_mediaMemoriasMedias[j] /= numJogos;

			double dp = 0;
			if (numJogos > 1) {
				for (double d : memoriaMedia[j]) {
					dp += Math.pow(d - _mediaMemoriasMedias[j], 2);
				}
				dp /= (numJogos - 1);
			}
			_mediaMemoriasDesvioPadrao[j] = Math.round(Math.sqrt(dp));
		}
	}

	public void setEscalaTempo(double escala) {
		escalaTempo = escala;
	}

	public void setEscalaMemoria(double escala) {
		escalaMemoria = escala;
	}

	public double getMediaTotalJogadas() {
		return _mediaTotalJogadas;
	}

	public long getVitorias(IndicadorJogador jogador) {
		return _numVitorias[jogador.ordinal()];
	}

	public long getEmpates() {
		return _numEmpates;
	}

	public double getMaiorTempo(IndicadorJogador jogador) {
		return _maiorTempoAbsoluto[jogador.ordinal()] * escalaTempo;
	}

	public double getMediaMaioresTempos(IndicadorJogador jogador) {
		return _mediaMaioresTempos[jogador.ordinal()] * escalaTempo;
	}

	public double getMediaTemposMedios(IndicadorJogador jogador) {
		return _mediaTemposMedios[jogador.ordinal()] * escalaTempo;
	}

	public double getMediaTemposDesvioPadrao(IndicadorJogador jogador) {
		return _mediaTemposDesvioPadrao[jogador.ordinal()] * escalaTempo;
	}

	public double getMaiorMemoria(IndicadorJogador jogador) {
		return _maiorMemoriaAbsoluto[jogador.ordinal()] * escalaMemoria;
	}

	public double getMediaMaioresMemorias(IndicadorJogador jogador) {
		return _mediaMaioresMemorias[jogador.ordinal()] * escalaMemoria;
	}

	public double getMediaMemoriasMedias(IndicadorJogador jogador) {
		return _mediaMemoriasMedias[jogador.ordinal()] * escalaMemoria;
	}

	public double getMediaMemoriasDesvioPadrao(IndicadorJogador jogador) {
		return _mediaMemoriasDesvioPadrao[jogador.ordinal()] * escalaTempo;
	}

}
