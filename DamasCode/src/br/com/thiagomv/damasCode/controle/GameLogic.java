package br.com.thiagomv.damasCode.controle;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import br.com.thiagomv.damasCode.constantes.IndicadorAlgoritmo;
import br.com.thiagomv.damasCode.constantes.IndicadorJogador;
import br.com.thiagomv.damasCode.constantes.IndicadorPedraJogador;
import br.com.thiagomv.damasCode.constantes.IndicadorResultadoJogo;
import br.com.thiagomv.damasCode.constantes.IndicadorTipoPedra;
import br.com.thiagomv.damasCode.estruturas.EstadoJogo;
import br.com.thiagomv.damasCode.estruturas.Jogada;
import br.com.thiagomv.damasCode.estruturas.PosicaoTabuleiro;
import br.com.thiagomv.damasCode.estruturas.Tabuleiro;

/**
 * Esta classe controla a parte lógica do jogo. O laço principal de controle de
 * jogo é estabelecido aqui. Algumas regras de jogo também são controladas aqui,
 * entre elas estão: verificação de empates, verificação de vencedores e
 * verificação de jogadas dos jogadores. Regras que definem quais jogadas podem
 * ser realizadas são controladas pela classe {@link Regras}. Esta classe
 * redireciona a escolha de jogada para a interface gráfica, caso o jogador
 * atual seja do tipo Humano, ou para o módulo de Inteligência Artificial, caso
 * contrário.
 * 
 * @author Thiago Mendes Vieira
 * 
 *         20/09/2014
 */
public final class GameLogic {
	private static final String MSG_ERRO_RUN_GAME_LOGIC = "Houve um erro no controle lógico do jogo e por isso o jogo foi finalizado!:\n";
	private static final String MSG_ERRO_INICIAR_JOGO = "GameLogic error: O jogo já foi iniciado!";
	private static final String MSG_ERRO_JOGAR_FRAGMENTO1 = "Erro interno em GameLogic: A jogada fornecida pelo jogador ";
	private static final String MSG_ERRO_JOGAR_FRAGMENTO2 = " não é válida!";
	private static final String MSG_ERRO_LISTENER_NULL = "O GameLogicListener fornecido a GameLogic não pode ser nulo.";

	// Variáveis principais que definem o comportamento do jogo...
	private Regras regras;
	private final EstadoJogo estadoJogo;
	private final GameLogicListener listener;
	private Thread thread = null;

	// Variáveis que controlam o estado do jogo...
	private boolean jogoIniciado = false;
	private boolean jogoAcabado = false;
	private IndicadorResultadoJogo resultadoJogo = null;

	// Variáveis responsáveis por identificar empates de jogo...
	private IndicadorTipoPedra tipoPedraUltimaJogada = null;
	private int numLancesDamas = 0;
	private IndicadorResultadoJogo provavelEmpateFinal = null;
	private int numLancesFinais = 0;

	// Controle de interrupção de jogo...
	private final Object lockGameLoop = new Object();

	// Estatisticas...
	private int numTotalJogadas = 0;

	/**
	 * Cria uma instância de GameLogic que envia eventos a um
	 * {@link GameLogicListener}.
	 * 
	 * @param listener
	 *            Objeto que escuta os eventos desta instância.
	 */
	public GameLogic(GameLogicListener listener) {
		if (listener == null) {
			throw new RuntimeException(MSG_ERRO_LISTENER_NULL);
		}

		this.regras = new Regras();
		this.estadoJogo = new EstadoJogo();
		this.listener = listener;
	}

	/**
	 * Inicia um novo jogo.
	 */
	public void iniciarJogo() {
		synchronized (lockGameLoop) {
			if (jogoIniciado) {
				throw new RuntimeException(MSG_ERRO_INICIAR_JOGO);
			}

			// Limpa controles globais de tempo, memória e IA...
			TimeController.getInstance().clear();
			MemoriaController.clear();
			IAController.configurarIAParaNovoJogo();

			jogoIniciado = true;

			numTotalJogadas = 0;
		}

		jogoAcabado = false;
		numLancesDamas = numLancesFinais = 0;
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				runGameLogic();
			}
		});

		Tabuleiro tabuleiro = new Tabuleiro(
				ArchitectureSettings.getNumLinhas(),
				ArchitectureSettings.getNumColunas());
		tabuleiro.configurarParaNovoJogo();
		estadoJogo.setTabuleiro(tabuleiro);
		estadoJogo.setJogadorAtual(IndicadorJogador.JOGADOR1);

		tipoPedraUltimaJogada = null;
		numLancesDamas = 0;
		provavelEmpateFinal = null;
		numLancesFinais = 0;
		resultadoJogo = null;

		thread.start();
	}

	public void interromperJogo() {
		synchronized (lockGameLoop) {
			resultadoJogo = IndicadorResultadoJogo.JOGO_INTERROMPIDO;
			jogoAcabado = true;

			if (jogoIniciado) {
				if (thread != null && thread.isAlive()) {
					IAController.interromper();
					thread.interrupt();
					try {
						lockGameLoop.wait(500);
					} catch (InterruptedException e) {
						throw new RuntimeException(
								"Houve um erro ao tentar interromper o jogo!");
					}
					if (jogoIniciado) {
						forcarFinalizacao();
					} else {
						ArchitectureSettings.logLine("JOGO INTERROMPIDO",
								"Finalizado automaticamente!");
					}
				}
			}
		}

		this.listener.onGameLogic_JogoAcabado(resultadoJogo, numTotalJogadas);
	}

	private void jogoAcabado() {
		TimeController.getInstance().processar();
	}

	@SuppressWarnings("deprecation")
	private void forcarFinalizacao() {
		ArchitectureSettings.logLine("MATAR THREAD", "...");

		thread.stop();

		IAController.finalizarIA();
		jogoIniciado = false;

		/*
		 * Instancia um novo objeto para evitar a reutilização de sua
		 * propriedade "private final List<Jogada> jogadasPermitidas" que pode
		 * ter sido interrompida em meio de uma iteração, o que poderia causar
		 * um @{java.util.ConcurrentModificationException}.
		 */
		this.regras = new Regras();

		this.jogoAcabado();
	}

	/**
	 * Laço principal de controle do jogo. É a partir deste ponto que os fluxos
	 * do jogo são iniciados. Primeiro atualizamos o jogo para depois avisar ao
	 * controlador que pode ser redesenhado todos os gráficos. É verificado,
	 * então, se o jogo foi finalizado. Caso não seja, voltamos a atualizar e
	 * redesenhar, até que o jogo seja finalizado ou interrompido.
	 */
	private void runGameLogic() {
		System.gc();

		long tempoInicio, tempoFim, intervalo;
		long nanoScale = Math.round(Math.pow(10, 9));

		long minTimeUpdate = ArchitectureSettings.getMinTimeUpdate();

		this.listener.onGameLogic_JogoIniciado(estadoJogo);
		try {
			try {
				Thread.sleep(minTimeUpdate);
			} catch (InterruptedException e) {
				ArchitectureSettings.logLine("ERRO", e.getMessage());
				ArchitectureSettings.logLine("THROW",
						"Throw exception in GameLogic.runGameLogic.");
				throw new GameLogicException(e);
			}
			do {
				tempoInicio = System.nanoTime();

				atualizarJogo();
				this.listener.onGameLogic_JogoAtualizado(estadoJogo);

				tempoFim = System.nanoTime();
				intervalo = (tempoFim - tempoInicio) / nanoScale;
				if (intervalo >= 0 && intervalo < minTimeUpdate) {
					try {
						Thread.sleep(minTimeUpdate - intervalo);
					} catch (InterruptedException e) {
						ArchitectureSettings.logLine("ERRO", e.getMessage());
						ArchitectureSettings.logLine("THROW",
								"Throw exception in GameLogic.runGameLogic.");
						throw new GameLogicException(e);
					}
				}
			} while (!jogoAcabado);
		} catch (GameLogicException e) {
			ArchitectureSettings.logLine("ERRO", e.getMessage());
			if (!IndicadorResultadoJogo.JOGO_INTERROMPIDO.equals(resultadoJogo)) {
				JOptionPane.showMessageDialog(null,
						MSG_ERRO_RUN_GAME_LOGIC + e.getMessage());
			}
		}

		synchronized (lockGameLoop) {
			IAController.finalizarIA();
			jogoIniciado = false;

			this.jogoAcabado();

			if (!IndicadorResultadoJogo.JOGO_INTERROMPIDO.equals(resultadoJogo)) {
				this.listener.onGameLogic_JogoAcabado(resultadoJogo,
						numTotalJogadas);
			}

			lockGameLoop.notify();
		}
	}

	/**
	 * Atualiza o jogo.
	 * 
	 * @throws GameLogicException
	 */
	private void atualizarJogo() throws GameLogicException {
		atualizarRegras();
		escolherJogada();
		verificarJogo();
		proximoJogador();
	}

	/**
	 * Atualiza as regras do jogo. As regras não mudam. O que é feito aqui é a
	 * atualização de tudo que esteja relacionado às regras do jogo, como por
	 * exemplo, a lista de jogadas permitidas neste momento, segundo as regras.
	 */
	private void atualizarRegras() {
		regras.atualizarRegras(estadoJogo);
	}

	/**
	 * Escolhe uma jogada. Caso o jogador atual seja do tipo Humano, este método
	 * será bloqueado até que sua jogada seja finalizada (a jogada do jogador
	 * Humano é controlada pela interface gráfica). Caso o jogador seja
	 * controlado por alguma Inteligencia Artificial, os algoritmos apropriados
	 * serão executados para a realização da jogada.
	 * 
	 * @throws GameLogicException
	 */
	private void escolherJogada() throws GameLogicException {
		List<Jogada> jogadasPermitidas = regras.getJogadasPermitidas();

		if (jogadasPermitidas.size() == 0) {
			// Este jogador não possui movimentos válidos!
			// Portanto a vitória é dada ao adversário!
			if (IndicadorJogador.JOGADOR1.equals(estadoJogo.getJogadorAtual())) {
				resultadoJogo = IndicadorResultadoJogo.VENCE_JOGADOR2;
			} else {
				resultadoJogo = IndicadorResultadoJogo.VENCE_JOGADOR1;
			}
			jogoAcabado = true;
			return;
		}

		IndicadorAlgoritmo algoritmo = estadoJogo.getJogadorAtual()
				.getAlgoritmo();
		Jogada jogada = null;

		long mem;
		mem = MemoriaController.antesJogar(estadoJogo.getJogadorAtual());

		long tempoInicio = -1;
		long tempoFim = -1;

		if (IndicadorAlgoritmo.HUMANO.equals(algoritmo)) {
			try {
				tempoInicio = System.nanoTime();
				jogada = listener.onGameLogic_EscolherJogada(estadoJogo,
						jogadasPermitidas);
				tempoFim = System.nanoTime();

				mem = MemoriaController.aposJogar(estadoJogo.getJogadorAtual())
						- mem;

				if (ArchitectureSettings.isMemoriaControllerLogEnabled()) {
					ArchitectureSettings.logLine("Total", mem + "\n");
				}
			} catch (UIDamasControllerException e) {
				mem = MemoriaController.aposJogar(estadoJogo.getJogadorAtual())
						- mem;
				if (ArchitectureSettings.isMemoriaControllerLogEnabled()) {
					ArchitectureSettings.logLine("Total", mem + "\n");
				}

				ArchitectureSettings.logLine("ERRO", e.getMessage());
				ArchitectureSettings.logLine("THROW",
						"Throw exception in GameLogic.escolherJogada.");
				throw new GameLogicException(e);
			}
		} else {
			try {
				/*
				 * São retornadas apenas cópias das informações originais, para
				 * proteger os dados contra modificações que possam ser feitas
				 * nestas estruturas por implementações de inteligências
				 * artificiais mal-intencionadas.
				 */
				List<Jogada> cloneJogadas = new ArrayList<>(
						jogadasPermitidas.size());
				for (Jogada j : jogadasPermitidas) {
					cloneJogadas.add((Jogada) j.clone());
				}

				tempoInicio = System.nanoTime();
				jogada = IAController.escolherJogada(cloneJogadas,
						(EstadoJogo) estadoJogo.clone());
				tempoFim = System.nanoTime();

				mem = MemoriaController.aposJogar(estadoJogo.getJogadorAtual())
						- mem;
				if (ArchitectureSettings.isMemoriaControllerLogEnabled()) {
					ArchitectureSettings.logLine("Total", mem + "\n");
				}
			} catch (Exception e) {
				mem = MemoriaController.aposJogar(estadoJogo.getJogadorAtual())
						- mem;
				if (ArchitectureSettings.isMemoriaControllerLogEnabled()) {
					ArchitectureSettings.logLine("Total", mem + "\n");
				}

				if (IndicadorJogador.JOGADOR1.equals(estadoJogo
						.getJogadorAtual())) {
					resultadoJogo = IndicadorResultadoJogo.VENCE_JOGADOR2;
				} else {
					resultadoJogo = IndicadorResultadoJogo.VENCE_JOGADOR1;
				}
				jogoAcabado = true;

				ArchitectureSettings.logLine("ERRO", e.toString());
				e.printStackTrace();
				ArchitectureSettings.logLine("THROW",
						"Throw exception in GameLogic.escolherJogada.");
				throw new GameLogicException(
						"Falha no processamento no módulo de Inteligência Artificial (IA). "
								+ "Confira as implementações da IA utilizada: \n"
								+ "Algoritmo: "
								+ estadoJogo.getJogadorAtual().getAlgoritmo()
										.getLabel()
								+ ". Heurística: "
								+ estadoJogo.getJogadorAtual().getHeuristica()
										.getLabel());
			}
		}

		numTotalJogadas++;

		assert (tempoInicio >= 0);
		assert (tempoFim >= 0);
		assert (tempoFim >= tempoInicio);
		long elapsedTime = tempoFim - tempoInicio;
		TimeController.getInstance().logTime(estadoJogo.getJogadorAtual(),
				elapsedTime);

		synchronized (lockGameLoop) {
			if (jogoAcabado) {
				// Esta condição será satisfeita caso o jogo tenha sido
				// interrompido.
				return;
			}
		}

		jogar(jogada);
	}

	/**
	 * Realiza a jogada escolhida pelo jogador atual. Caso a jogada escolhida
	 * não seja válida, o jogo será finalizado e o jogador adversário será
	 * considerado o vencedor da partida.
	 * 
	 * @param jogada
	 *            Jogada escolhida pelo jogador atual.
	 * @throws GameLogicException
	 */
	private void jogar(Jogada jogada) throws GameLogicException {
		if (!regras.isJogadaPermitida(jogada)) {
			if (IndicadorJogador.JOGADOR1.equals(estadoJogo.getJogadorAtual())) {
				resultadoJogo = IndicadorResultadoJogo.VENCE_JOGADOR2;
			} else {
				resultadoJogo = IndicadorResultadoJogo.VENCE_JOGADOR1;
			}
			jogoAcabado = true;

			ArchitectureSettings.logLine("THROW",
					"Throw exception in GameLogic.jogar.");
			throw new GameLogicException(MSG_ERRO_JOGAR_FRAGMENTO1
					+ estadoJogo.getJogadorAtual().toString()
					+ MSG_ERRO_JOGAR_FRAGMENTO2);
		}

		// Retira do tabuleiro todas as pedras capturadas...
		Tabuleiro tabuleiro = estadoJogo.getTabueiro();
		for (PosicaoTabuleiro posicaoCaptura : jogada.getPedrasCapturadas()) {
			tabuleiro.setPedraJogador(posicaoCaptura, null);
		}
		IndicadorPedraJogador pedra = tabuleiro.getPedraJogador(jogada
				.getPosicaoInicial());

		// Atualiza a posição da pedra de origem no movimento...
		this.tipoPedraUltimaJogada = pedra.getTipoPedra();
		tabuleiro.setPedraJogador(jogada.getPosicaoInicial(), null);
		if (IndicadorTipoPedra.NORMAL.equals(pedra.getTipoPedra())
				&& tabuleiro.isPosicaoDama(pedra.getJogador(),
						jogada.getPosicaoFinal())) {
			pedra = pedra.promoverDama();
		}
		tabuleiro.setPedraJogador(jogada.getPosicaoFinal(), pedra);
	}

	/**
	 * Verifica se o jogo foi finalizado ou empatado.
	 */
	private void verificarJogo() {
		if (jogoAcabado) {
			// O jogador não possui mais movimentos válidos.
			return;
		}
		verificarQuantidadePecas();
		if (jogoAcabado) {
			// O adversário não possui mais pedras.
			return;
		}
		verificarEmpateMovimentoDamas();
		if (jogoAcabado) {
			// Empate por 20 movimentos consecutivod de damas.
			return;
		}
		verificarFinaisDeEmpate();
	}

	/**
	 * Verifica a quantidade de pedras dos jogadores.
	 */
	private void verificarQuantidadePecas() {
		Tabuleiro tabuleiro = estadoJogo.getTabueiro();

		int numLinhas = tabuleiro.getNumLinhas();
		int numColunas = tabuleiro.getNumColunas();

		// Verificando quantidade de peças dos jogadores...
		int numPecasJogador1 = 0;
		int numPecasJogador2 = 0;
		IndicadorPedraJogador pedraAux;
		for (int L = 0; L < numLinhas; L++) {
			for (int C = 0; C < numColunas; C++) {
				pedraAux = tabuleiro.getPedraJogador(L, C);
				if (pedraAux != null) {
					switch (pedraAux.getJogador()) {
					case JOGADOR1:
						numPecasJogador1++;
						break;
					case JOGADOR2:
						numPecasJogador2++;
						break;
					}
				}
			}
		}

		if (numPecasJogador1 == 0) {
			resultadoJogo = IndicadorResultadoJogo.VENCE_JOGADOR2;
			jogoAcabado = true;
		} else if (numPecasJogador2 == 0) {
			resultadoJogo = IndicadorResultadoJogo.VENCE_JOGADOR1;
			jogoAcabado = true;
		}
	}

	/**
	 * Verifica empates relacionados ao movimento de damas, segundo a regra:
	 * Após 20 lances sucessivos de damas de cada jogador, sem captura ou
	 * deslocamento de pedra, a partida é declarada empatada
	 */
	private void verificarEmpateMovimentoDamas() {
		if (IndicadorTipoPedra.DAMA.equals(tipoPedraUltimaJogada)) {
			numLancesDamas++;
		} else {
			numLancesDamas = 0;
		}

		if (numLancesDamas == 40) {
			// 20 lances sucessivos de damas de cada jogador...
			resultadoJogo = IndicadorResultadoJogo.EMPATE_MOVIMENTO_DAMAS;
			jogoAcabado = true;
		}
	}

	/**
	 * Verifica empates relacionados a finais de partidas, segundo a regra:
	 * Finais de 2 damas contra 2 damas; 2 damas contra uma; 2 damas contra uma
	 * dama e uma pedra; uma dama contra uma dama; e uma dama contra uma dama e
	 * uma pedra, são declarados empatados após 5 lances de cada jogador.
	 */
	private void verificarFinaisDeEmpate() {
		int numDamasJ1 = getNumPedrasJogador(IndicadorPedraJogador.DAMA_J1);
		int numNormaisJ1 = getNumPedrasJogador(IndicadorPedraJogador.NORMAL_J1);
		int numDamasJ2 = getNumPedrasJogador(IndicadorPedraJogador.DAMA_J2);
		int numNormaisJ2 = getNumPedrasJogador(IndicadorPedraJogador.NORMAL_J2);

		if (((numDamasJ1 + numNormaisJ1) > 2)
				|| ((numDamasJ2 + numNormaisJ2 > 2))
				|| ((numNormaisJ1 + numNormaisJ2) > 1)) {
			/*
			 * Casos mais frequêntes eliminados para aumentar a performace! O
			 * caso onde algum jogador não possui nenhuma pedra já foi
			 * verificado no método verificarQuantidadePecas().
			 */
			provavelEmpateFinal = null;
			numLancesFinais = 0;
			return;
		}

		// Verificando empates de finais...
		IndicadorResultadoJogo provavelEmpate = null;
		if (numDamasJ1 == 2) {
			// Consequentemente numNormaisJ1 == 0
			if (numDamasJ2 == 2) {
				// Consequentemente numNormaisJ2 == 0
				provavelEmpate = IndicadorResultadoJogo.EMPATE_2D_2D;
			} else if (numDamasJ2 == 1) {
				if (numNormaisJ2 == 0) {
					provavelEmpate = IndicadorResultadoJogo.EMPATE_2D_1D;
				} else {
					// Consequentemente numNormaisJ2 == 1
					provavelEmpate = IndicadorResultadoJogo.EMPATE_2D_1D_1N;
				}
			}
		} else if (numDamasJ1 == 1) {
			if (numNormaisJ1 == 0) {
				if (numDamasJ2 == 2) {
					// Consequentemente numNormaisJ2 == 0
					provavelEmpate = IndicadorResultadoJogo.EMPATE_2D_1D;
				} else if (numDamasJ2 == 1) {
					if (numNormaisJ2 == 0) {
						provavelEmpate = IndicadorResultadoJogo.EMPATE_1D_1D;
					} else {
						// Consequentemente numNormaisJ2 == 1
						provavelEmpate = IndicadorResultadoJogo.EMPATE_1D_1D_1N;
					}
				}
			} else {
				// Consequentemente numNormaisJ1 == 1
				if (numDamasJ2 == 2) {
					// Consequentemente numNormaisJ2 == 0
					provavelEmpate = IndicadorResultadoJogo.EMPATE_2D_1D_1N;
				} else if (numDamasJ2 == 1) {
					// Consequentemente numNormaisJ2 == 1
					provavelEmpate = IndicadorResultadoJogo.EMPATE_1D_1D_1N;
				}
			}
		}

		if (provavelEmpate != null) {
			if (provavelEmpate.equals(provavelEmpateFinal)) {
				numLancesFinais++;
				if (numLancesFinais == 10) {
					// 5 lances de cada jogador...
					resultadoJogo = provavelEmpateFinal;
					jogoAcabado = true;
				}
			} else {
				provavelEmpateFinal = provavelEmpate;
				numLancesFinais = 0;
			}
			return;
		}

		// Casos onde um jogador possui 2 Damas e o outro 1 Normal.
		// Este caso não define um final de empate!
		provavelEmpateFinal = null;
		numLancesFinais = 0;
	}

	/**
	 * Retorna a quantidade de pedras no tabuleiro que são de determinado tipo e
	 * jogador.
	 * 
	 * @param pedraJogador
	 *            Indicador que define o tipo da pedra e o jogador ao qual ela
	 *            pertence.
	 * @return Quantidade de pedras no tabuleiro que correspondem ao filtro
	 *         informado por parâmetro.
	 */
	private int getNumPedrasJogador(IndicadorPedraJogador pedraJogador) {
		int numPedras = 0;

		Tabuleiro tabuleiro = estadoJogo.getTabueiro();
		int numLinhas = tabuleiro.getNumLinhas();
		int numColunas = tabuleiro.getNumColunas();

		IndicadorPedraJogador pedraAux;
		for (int L = 0; L < numLinhas; L++) {
			for (int C = 0; C < numColunas; C++) {
				pedraAux = tabuleiro.getPedraJogador(L, C);
				if ((pedraAux != null) && pedraJogador.equals(pedraAux)) {
					numPedras++;
				}
			}
		}

		return numPedras;
	}

	/**
	 * Passa a vez ao outro jogador. Se o jogo estiver finalizado, o indicador
	 * de jogador atual é estabelecido como null.
	 */
	private void proximoJogador() {
		if (jogoAcabado) {
			estadoJogo.setJogadorAtual(null);
			return;
		}

		IndicadorJogador jogadorAtual = estadoJogo.getJogadorAtual();
		if (IndicadorJogador.JOGADOR1.equals(jogadorAtual)) {
			estadoJogo.setJogadorAtual(IndicadorJogador.JOGADOR2);
		} else {
			estadoJogo.setJogadorAtual(IndicadorJogador.JOGADOR1);
		}
	}
}
