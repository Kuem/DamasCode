package br.com.thiagomv.damasCode.testes;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import br.com.thiagomv.damasCode.constantes.IndicadorAlgoritmo;
import br.com.thiagomv.damasCode.constantes.IndicadorJogador;
import br.com.thiagomv.damasCode.constantes.IndicadorPedraJogador;
import br.com.thiagomv.damasCode.constantes.IndicadorResultadoJogo;
import br.com.thiagomv.damasCode.controle.ArchitectureSettings;
import br.com.thiagomv.damasCode.controle.GameLogic;
import br.com.thiagomv.damasCode.controle.GameLogicListener;
import br.com.thiagomv.damasCode.controle.UIDamasControllerException;
import br.com.thiagomv.damasCode.estruturas.EstadoJogo;
import br.com.thiagomv.damasCode.estruturas.Jogada;
import br.com.thiagomv.damasCode.estruturas.MovimentoSimples;
import br.com.thiagomv.damasCode.estruturas.PosicaoTabuleiro;
import br.com.thiagomv.damasCode.estruturas.Tabuleiro;

/**
 * Testa o controle do jogo.
 * 
 * As regras do jogo podem ser consultadas em {@link RegrasTest}.
 * 
 * @author Thiago Mendes Vieira
 * 
 *         21/09/2014
 */
public class GameLogicTest {
	private final Object lock = new Object();
	private Tabuleiro tabuleiro = null;

	/**
	 * Simula uma partida na qual o jogador 1 vence bloqueando os movimentos de
	 * pedras do jogador 2.
	 * 
	 * Testa as regras 1, 2, 3, 4, 5, 7, 9. @see {@link RegrasTest}.
	 * 
	 * Testa vitória do jogador 1.
	 * 
	 * Testa controle de fluxos para jogadores Humanos.
	 */
	@Test
	public void partidaVenceJogador1BloqueandoPedrasOponente() {
		List<Jogada> listaJogadas = new ArrayList<>();
		listaJogadas.add(criarJogada(new PosicaoTabuleiro(2, 2),
				new PosicaoTabuleiro(3, 1)));
		listaJogadas.add(criarJogada(new PosicaoTabuleiro(5, 5),
				new PosicaoTabuleiro(4, 6)));
		listaJogadas.add(criarJogada(new PosicaoTabuleiro(2, 4),
				new PosicaoTabuleiro(3, 3)));
		listaJogadas.add(criarJogada(new PosicaoTabuleiro(5, 3),
				new PosicaoTabuleiro(4, 2)));
		listaJogadas.add(criarJogada(new PosicaoTabuleiro(3, 1),
				new PosicaoTabuleiro(4, 2), new PosicaoTabuleiro(5, 3)));
		listaJogadas.add(criarJogada(new PosicaoTabuleiro(6, 4),
				new PosicaoTabuleiro(5, 3), new PosicaoTabuleiro(4, 2),
				new PosicaoTabuleiro(4, 2), new PosicaoTabuleiro(3, 3),
				new PosicaoTabuleiro(2, 4)));
		listaJogadas.add(criarJogada(new PosicaoTabuleiro(1, 5),
				new PosicaoTabuleiro(2, 4), new PosicaoTabuleiro(3, 3)));
		listaJogadas.add(criarJogada(new PosicaoTabuleiro(4, 6),
				new PosicaoTabuleiro(3, 5)));
		listaJogadas.add(criarJogada(new PosicaoTabuleiro(2, 6),
				new PosicaoTabuleiro(3, 5), new PosicaoTabuleiro(4, 4)));
		listaJogadas.add(criarJogada(new PosicaoTabuleiro(6, 2),
				new PosicaoTabuleiro(5, 3)));
		listaJogadas.add(criarJogada(new PosicaoTabuleiro(4, 4),
				new PosicaoTabuleiro(5, 3), new PosicaoTabuleiro(6, 2),
				new PosicaoTabuleiro(6, 2), new PosicaoTabuleiro(5, 1),
				new PosicaoTabuleiro(4, 0)));
		listaJogadas.add(criarJogada(new PosicaoTabuleiro(7, 5),
				new PosicaoTabuleiro(6, 4)));
		listaJogadas.add(criarJogada(new PosicaoTabuleiro(3, 3),
				new PosicaoTabuleiro(4, 2)));
		listaJogadas.add(criarJogada(new PosicaoTabuleiro(5, 7),
				new PosicaoTabuleiro(4, 6)));
		listaJogadas.add(criarJogada(new PosicaoTabuleiro(4, 0),
				new PosicaoTabuleiro(5, 1)));
		listaJogadas.add(criarJogada(new PosicaoTabuleiro(7, 3),
				new PosicaoTabuleiro(6, 2)));
		listaJogadas.add(criarJogada(new PosicaoTabuleiro(5, 1),
				new PosicaoTabuleiro(6, 2), new PosicaoTabuleiro(7, 3),
				new PosicaoTabuleiro(7, 3), new PosicaoTabuleiro(6, 4),
				new PosicaoTabuleiro(5, 5), new PosicaoTabuleiro(5, 5),
				new PosicaoTabuleiro(4, 6), new PosicaoTabuleiro(3, 7)));
		listaJogadas.add(criarJogada(new PosicaoTabuleiro(6, 6),
				new PosicaoTabuleiro(5, 5)));
		listaJogadas.add(criarJogada(new PosicaoTabuleiro(4, 2),
				new PosicaoTabuleiro(5, 3)));
		listaJogadas.add(criarJogada(new PosicaoTabuleiro(7, 1),
				new PosicaoTabuleiro(6, 2)));
		listaJogadas.add(criarJogada(new PosicaoTabuleiro(5, 3),
				new PosicaoTabuleiro(6, 2), new PosicaoTabuleiro(7, 1)));
		listaJogadas.add(criarJogada(new PosicaoTabuleiro(5, 5),
				new PosicaoTabuleiro(4, 4)));
		listaJogadas.add(criarJogada(new PosicaoTabuleiro(7, 1),
				new PosicaoTabuleiro(4, 4), new PosicaoTabuleiro(2, 6)));
		listaJogadas.add(criarJogada(new PosicaoTabuleiro(6, 0),
				new PosicaoTabuleiro(5, 1)));
		listaJogadas.add(criarJogada(new PosicaoTabuleiro(3, 7),
				new PosicaoTabuleiro(4, 6)));
		listaJogadas.add(criarJogada(new PosicaoTabuleiro(5, 1),
				new PosicaoTabuleiro(4, 2)));
		listaJogadas.add(criarJogada(new PosicaoTabuleiro(2, 6),
				new PosicaoTabuleiro(1, 5)));
		listaJogadas.add(criarJogada(new PosicaoTabuleiro(4, 2),
				new PosicaoTabuleiro(3, 3)));
		listaJogadas.add(criarJogada(new PosicaoTabuleiro(1, 5),
				new PosicaoTabuleiro(3, 3), new PosicaoTabuleiro(5, 1)));
		listaJogadas.add(criarJogada(new PosicaoTabuleiro(7, 7),
				new PosicaoTabuleiro(6, 6)));
		listaJogadas.add(criarJogada(new PosicaoTabuleiro(5, 1),
				new PosicaoTabuleiro(2, 4)));
		listaJogadas.add(criarJogada(new PosicaoTabuleiro(6, 6),
				new PosicaoTabuleiro(5, 7)));
		listaJogadas.add(criarJogada(new PosicaoTabuleiro(2, 4),
				new PosicaoTabuleiro(3, 5)));

		ListenerJogador jogador = new ListenerJogador(listaJogadas);
		GameLogic gameLogic = new GameLogic(jogador);

		IndicadorJogador.JOGADOR1.setAlgoritmo(IndicadorAlgoritmo.HUMANO);
		IndicadorJogador.JOGADOR2.setAlgoritmo(IndicadorAlgoritmo.HUMANO);

		esperarJogo(gameLogic, jogador);

		Assert.assertThat(jogador.getNumTotalJogadas(),
				CoreMatchers.is(listaJogadas.size()));
		Assert.assertThat(jogador.getResultado(),
				CoreMatchers.is(IndicadorResultadoJogo.VENCE_JOGADOR1));

		tabuleiro = jogador.getEstadoJogoAtualizado().getTabueiro();

		IndicadorPedraJogador branca = IndicadorPedraJogador.NORMAL_J1;

		assertTabuleiro(0, 0, branca);
		assertTabuleiro(0, 1, null);
		assertTabuleiro(0, 2, branca);
		assertTabuleiro(0, 3, null);
		assertTabuleiro(0, 4, branca);
		assertTabuleiro(0, 5, null);
		assertTabuleiro(0, 6, branca);
		assertTabuleiro(0, 7, null);

		assertTabuleiro(1, 0, null);
		assertTabuleiro(1, 1, branca);
		assertTabuleiro(1, 2, null);
		assertTabuleiro(1, 3, branca);
		assertTabuleiro(1, 4, null);
		assertTabuleiro(1, 5, null);
		assertTabuleiro(1, 6, null);
		assertTabuleiro(1, 7, branca);

		assertTabuleiro(2, 0, branca);
		assertTabuleiro(2, 1, null);
		assertTabuleiro(2, 2, null);
		assertTabuleiro(2, 3, null);
		assertTabuleiro(2, 4, null);
		assertTabuleiro(2, 5, null);
		assertTabuleiro(2, 6, null);
		assertTabuleiro(2, 7, null);

		assertTabuleiro(3, 0, null);
		assertTabuleiro(3, 1, null);
		assertTabuleiro(3, 2, null);
		assertTabuleiro(3, 3, null);
		assertTabuleiro(3, 4, null);
		assertTabuleiro(3, 5, IndicadorPedraJogador.DAMA_J1);
		assertTabuleiro(3, 6, null);
		assertTabuleiro(3, 7, null);

		assertTabuleiro(4, 0, null);
		assertTabuleiro(4, 1, null);
		assertTabuleiro(4, 2, null);
		assertTabuleiro(4, 3, null);
		assertTabuleiro(4, 4, null);
		assertTabuleiro(4, 5, null);
		assertTabuleiro(4, 6, branca);
		assertTabuleiro(4, 7, null);

		assertTabuleiro(5, 0, null);
		assertTabuleiro(5, 1, null);
		assertTabuleiro(5, 2, null);
		assertTabuleiro(5, 3, null);
		assertTabuleiro(5, 4, null);
		assertTabuleiro(5, 5, null);
		assertTabuleiro(5, 6, null);
		assertTabuleiro(5, 7, IndicadorPedraJogador.NORMAL_J2);

		assertTabuleiro(6, 0, null);
		assertTabuleiro(6, 1, null);
		assertTabuleiro(6, 2, null);
		assertTabuleiro(6, 3, null);
		assertTabuleiro(6, 4, null);
		assertTabuleiro(6, 5, null);
		assertTabuleiro(6, 6, null);
		assertTabuleiro(6, 7, null);

		assertTabuleiro(7, 0, null);
		assertTabuleiro(7, 1, null);
		assertTabuleiro(7, 2, null);
		assertTabuleiro(7, 3, null);
		assertTabuleiro(7, 4, null);
		assertTabuleiro(7, 5, null);
		assertTabuleiro(7, 6, null);
		assertTabuleiro(7, 7, null);
	}

	/**
	 * Espera o controle de fluxo de jogo realizar as jogadas até o jogo
	 * terminar, ou até o limite de tempo de espera ser atingido.
	 * 
	 * @param jogador
	 */
	private void esperarJogo(GameLogic gameLogic, ListenerJogador jogador) {
		ArchitectureSettings.setMinTimeUpdate(0);
		gameLogic.iniciarJogo();
		synchronized (lock) {
			if (jogador.getResultado() == null) {
				try {
					lock.wait(10000);
				} catch (InterruptedException e) {
					Assert.fail();
				}
			}
		}
	}

	/**
	 * Cria uma jogada com um movimento sem captura.
	 * 
	 * @param posicaoOrigem
	 *            Posição de origem do movimento.
	 * @param posicaoDestino
	 *            Posição de destino do movimento.
	 * @return {@link Jogada}.
	 */
	private Jogada criarJogada(PosicaoTabuleiro posicaoOrigem,
			PosicaoTabuleiro posicaoDestino) {
		Jogada jogada = new Jogada();
		jogada.adicionarMovimentoSimples(new MovimentoSimples(posicaoOrigem,
				posicaoDestino));
		return jogada;
	}

	/**
	 * Cria uma jogada com 1 ou mais movimentos com captura.
	 * 
	 * @param posicoes
	 *            Lista de posições que indicam as posições de origem, captura e
	 *            destino, nesta ordem. Assim, a lista deve ter uma quantidade
	 *            de elementos múltipla de 3.
	 * @return {@link Jogada}.
	 */
	private Jogada criarJogada(PosicaoTabuleiro... posicoes) {
		Jogada jogada = new Jogada();

		int index = 0;
		do {
			jogada.adicionarMovimentoSimples(new MovimentoSimples(
					posicoes[index], posicoes[index + 1], posicoes[index + 2]));
			index += 3;
		} while (index < posicoes.length);

		return jogada;
	}

	/**
	 * Verifica se determinada posição do tabuleiro está com o valor esperado.
	 * 
	 * @param linha
	 *            Coordenada da linha da posição.
	 * @param coluna
	 *            Coordenada da coluna da posição.
	 * @param valorEsperado
	 *            Valor esperado para a posição definida.
	 */
	private void assertTabuleiro(int linha, int coluna,
			IndicadorPedraJogador valorEsperado) {
		Assert.assertThat(tabuleiro.getPedraJogador(linha, coluna),
				CoreMatchers.is(valorEsperado));
	}

	/**
	 * Esta classe implementa um {@link GameLogicListener} para realizar as
	 * jogadas dos jogadores Humanos.
	 * 
	 * @author Thiago Mendes Vieira
	 * 
	 *         21/09/2014
	 */
	private class ListenerJogador implements GameLogicListener {
		private int index;
		private final List<Jogada> listaJogadas;
		private IndicadorResultadoJogo resultado;
		private EstadoJogo estadoJogoAtualizado;

		public ListenerJogador(final List<Jogada> jogadas) {
			this.listaJogadas = jogadas;
			this.index = 0;
			this.resultado = null;
			this.estadoJogoAtualizado = null;
		}

		@Override
		public void onGameLogic_JogoIniciado(EstadoJogo estadoJogo) {
		}

		@Override
		public void onGameLogic_JogoAtualizado(EstadoJogo estadoJogo) {
			this.estadoJogoAtualizado = estadoJogo;
		}

		@Override
		public void onGameLogic_JogoAcabado(
				IndicadorResultadoJogo resultadoJogo, int numTotalJogadas) {
			this.resultado = resultadoJogo;
			synchronized (lock) {
				lock.notify();
			}
		}

		@Override
		public Jogada onGameLogic_EscolherJogada(EstadoJogo estadoJogo,
				List<Jogada> jogadasPermitidas)
				throws UIDamasControllerException {
			return this.listaJogadas.get(this.index++);
		}

		public int getNumTotalJogadas() {
			return this.index;
		}

		public IndicadorResultadoJogo getResultado() {
			return this.resultado;
		}

		public EstadoJogo getEstadoJogoAtualizado() {
			return this.estadoJogoAtualizado;
		}
	}
}
