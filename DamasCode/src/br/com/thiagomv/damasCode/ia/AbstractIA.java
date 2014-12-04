package br.com.thiagomv.damasCode.ia;

import java.util.List;

import br.com.thiagomv.damasCode.estruturas.EstadoJogo;
import br.com.thiagomv.damasCode.estruturas.Jogada;

public abstract class AbstractIA {
	private boolean interrompido = false;

	public abstract Jogada escolherJogada(List<Jogada> jogadasPermitidas,
			EstadoJogo estadoJogo);

	public void configurarIAParaNovoJogo() {
		interrompido = false;
	}

	public void interromper() {
		interrompido = true;
	}

	public void finalizar() {
	}

	protected boolean isInterrompido() {
		return this.interrompido;
	}
}
