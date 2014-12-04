package br.com.thiagomv.damasCode.ia;

import java.util.List;

import br.com.thiagomv.damasCode.controle.MemoriaController;
import br.com.thiagomv.damasCode.estruturas.EstadoJogo;
import br.com.thiagomv.damasCode.estruturas.Jogada;

public class AleatorioIA extends AbstractIA {

	@Override
	public Jogada escolherJogada(List<Jogada> jogadasPermitidas, EstadoJogo estadoJogo) {
		int indexJogada = (int) Math.round(Math.floor(Math.random()
				* (double) jogadasPermitidas.size()));
		assert (indexJogada < jogadasPermitidas.size());
		
		MemoriaController.contabilizarMemoria();
		
		return jogadasPermitidas.get(indexJogada);
	}

}
