package br.com.thiagomv.damasCode.controle;

import java.util.List;

import br.com.thiagomv.damasCode.constantes.IndicadorResultadoJogo;
import br.com.thiagomv.damasCode.estruturas.EstadoJogo;
import br.com.thiagomv.damasCode.estruturas.Jogada;

public interface GameLogicListener {
	public void onGameLogic_JogoIniciado(EstadoJogo estadoJogo);

	public void onGameLogic_JogoAtualizado(EstadoJogo estadoJogo);

	public void onGameLogic_JogoAcabado(IndicadorResultadoJogo resultadoJogo,
			int numTotalJogadas);

	public Jogada onGameLogic_EscolherJogada(EstadoJogo estadoJogo,
			List<Jogada> jogadasPermitidas) throws UIDamasControllerException;
}
