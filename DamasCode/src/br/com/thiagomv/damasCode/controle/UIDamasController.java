package br.com.thiagomv.damasCode.controle;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import br.com.thiagomv.damasCode.constantes.IndicadorAlgoritmo;
import br.com.thiagomv.damasCode.constantes.IndicadorHeuristica;
import br.com.thiagomv.damasCode.constantes.IndicadorJogador;
import br.com.thiagomv.damasCode.constantes.IndicadorPedraJogador;
import br.com.thiagomv.damasCode.constantes.IndicadorResultadoJogo;
import br.com.thiagomv.damasCode.estruturas.EstadoJogo;
import br.com.thiagomv.damasCode.estruturas.Jogada;
import br.com.thiagomv.damasCode.estruturas.MovimentoSimples;
import br.com.thiagomv.damasCode.estruturas.PosicaoTabuleiro;
import br.com.thiagomv.damasCode.estruturas.Tabuleiro;
import br.com.thiagomv.damasCode.view.damas.UIDamas;

public final class UIDamasController implements GameLogicListener {

	private final UIDamas ui;

	private boolean jogoIniciado = false;

	private final GameLogic gameLogic;

	// Controle de jogada humana...
	private Object lockJogadaHumana = new Object();
	private EstadoJogo estadoJogo = null;
	private List<Jogada> jogadasPermitidas = null;
	private boolean jogoInterrompido = false;

	private boolean jogadaHumanaHabilitada = false;
	private Jogada jogadaHumana = null;
	private PosicaoTabuleiro posicaoInicialMovimento = null;
	private final List<PosicaoTabuleiro> posicoesPermitidas;

	public UIDamasController(UIDamas ui) {
		this.ui = ui;
		this.posicoesPermitidas = new ArrayList<PosicaoTabuleiro>();

		gameLogic = new GameLogic(this);
	}

	// ################################
	// # Métodos chamados por UIDamas #
	// ################################
	public void iniciarJogo(IndicadorAlgoritmo algoritmoJ1,
			IndicadorHeuristica heuristicaJ1, IndicadorAlgoritmo algoritmoJ2,
			IndicadorHeuristica heuristicaJ2) throws UIDamasControllerException {
		if (jogoIniciado) {
			ArchitectureSettings.logLine("THROW",
					"Throw exception in UIDamasController.iniciarJogo.");
			throw new UIDamasControllerException("O jogo já foi iniciado!");
		}

		// Estabelece configurações dos jogadores...
		IndicadorJogador.JOGADOR1.setAlgoritmo(algoritmoJ1);
		IndicadorJogador.JOGADOR1.setHeuristica(heuristicaJ1);
		IndicadorJogador.JOGADOR2.setAlgoritmo(algoritmoJ2);
		IndicadorJogador.JOGADOR2.setHeuristica(heuristicaJ2);

		this.jogadaHumanaHabilitada = false;
		this.jogoInterrompido = false;

		// Iniciando jogo...
		try {
			gameLogic.iniciarJogo();
		} catch (RuntimeException e) {
			ui.configurarLayoutParaJogoFinalizado(0);
			jogoIniciado = false;
			throw new UIDamasControllerException(e);
		}
	}

	public void interromperJogo() {
		this.jogoInterrompido = true;
		gameLogic.interromperJogo();
	}

	public void onClickCasa(int linha, int coluna)
			throws UIDamasControllerException {
		if (!this.jogadaHumanaHabilitada) {
			ArchitectureSettings.logLine("THROW",
					"Throw exception in UIDamasController.onClickCasa.");
			throw new UIDamasControllerException(
					"A jogada humana não foi habilitada!");
		}

		PosicaoTabuleiro posicaoClick = new PosicaoTabuleiro(linha, coluna);
		if (posicoesPermitidas.contains(posicaoClick)) {
			if (posicaoInicialMovimento == null) {
				// Seleciona a posição incial do movimento
				posicaoInicialMovimento = posicaoClick;
			} else {
				// Realiza o movimento simples!
				PosicaoTabuleiro posicaoInicial;
				if (jogadaHumana.getNumMovimentos() == 0) {
					// Primeiro movimento!
					posicaoInicial = posicaoInicialMovimento;
				} else {
					posicaoInicial = jogadaHumana.getPosicaoFinal();
				}
				jogadaHumana.adicionarMovimentoSimples(buscarMovimento(
						posicaoInicial, posicaoClick,
						jogadaHumana.getNumMovimentos()));
			}
		} else {
			throw new UIDamasControllerException(
					"Ops! Esta jogada não é permitida!");
		}

		desenharEstadoJogo(estadoJogo);
		mapearPosicoesPermitidas();

		if (posicoesPermitidas.size() == 0) {
			// Informa o movimento realizado e passa a vez para o adversário!
			synchronized (lockJogadaHumana) {
				lockJogadaHumana.notify();
			}
		}
	}

	/**
	 * Envia comandos para a camada de Interface Gráfica (através da classe
	 * UIDamas), para que o tabuleiro seja atualizado na tela. Envia comandos
	 * contendo o estado de cada casa do tabuleiro.
	 * 
	 * @param estadoJogo
	 */
	private void desenharEstadoJogo(EstadoJogo estadoJogo) {
		Tabuleiro tabuleiro = estadoJogo.getTabueiro();
		IndicadorPedraJogador pedraAux;
		for (int L = 0; L < tabuleiro.getNumLinhas(); L++) {
			for (int C = 0; C < tabuleiro.getNumColunas(); C++) {
				pedraAux = tabuleiro.getPedraJogador(L, C);
				if (pedraAux == null) {
					ui.setImageCasa(L, C, null, false);
				} else {
					ui.setImageCasa(L, C, pedraAux, false);
				}
			}
		}
	}

	/**
	 * Descobre quais as posições do tabuleiro o jogador humano poderá escolher
	 * para jogar. Ao mesmo tempo que procura as posições válidas, exclui-se as
	 * jogadas que não poderão mais ser escolhidas devido à dinâmica de escolha
	 * de uma jogada. A escolha de uma jogada é feita em duas etapas. A primeira
	 * consiste em escolher a posição inicial do movimento. A segunda em
	 * escolher a posição de destino. Caso mais movimentos possam ser feitos, a
	 * posição de destino do último movimento se transforma na posição inicial
	 * do movimento seguinte, e o usuário deve então escolher outra posição de
	 * destino, e assim sucessivamente até que não existam mais movimentos
	 * possíveis na jogada.
	 */
	private void mapearPosicoesPermitidas() {
		PosicaoTabuleiro posicaoTabuleiro;
		IndicadorPedraJogador pedraAux;
		List<Jogada> jogadasParaRemover = new ArrayList<Jogada>();
		boolean pularJogada;

		posicoesPermitidas.clear();
		for (Jogada j : jogadasPermitidas) {
			pularJogada = false;
			for (int I = 0; I < jogadaHumana.getNumMovimentos(); I++) {
				if (!j.getMovimento(I).equals(jogadaHumana.getMovimento(I))) {
					jogadasParaRemover.add(j);
					pularJogada = true;
					break;
				}
			}
			if (pularJogada) {
				continue;
			}
			// Esta jogada é permitida até o momento!

			if (jogadaHumana.getNumMovimentos() == 0) {
				if (posicaoInicialMovimento == null) {
					/*
					 * Nenhuma peça foi selecionada para iniciar o movimento até
					 * o momento!
					 */

					/*
					 * Encontrou-se uma posição válida para a jogada do usuário
					 * humano e esta posição será informada ao módulo de
					 * Interface Gráfica...
					 */
					posicaoTabuleiro = j.getMovimento(0).getPosicaoOrigem();
					pedraAux = estadoJogo.getTabueiro().getPedraJogador(
							posicaoTabuleiro);
					ui.setImageCasa(posicaoTabuleiro.getLinha(),
							posicaoTabuleiro.getColuna(), pedraAux, true);

					// Adiciona a posição na lista de posições válidas.
					posicoesPermitidas.add(posicaoTabuleiro);
				} else {
					/*
					 * O jogador já selecionou alguma peça do tabuleiro mas
					 * ainda não realizou nenhuma jogada!
					 */
					if (!j.getMovimento(0).getPosicaoOrigem()
							.equals(posicaoInicialMovimento)) {
						jogadasParaRemover.add(j);
						continue;
					}

					/*
					 * Encontrou-se uma posição válida para a jogada do usuário
					 * humano e esta posição será informada ao módulo de
					 * Interface Gráfica...
					 */
					posicaoTabuleiro = j.getMovimento(0).getPosicaoDestino();
					pedraAux = estadoJogo.getTabueiro().getPedraJogador(
							posicaoTabuleiro);
					ui.setImageCasa(posicaoTabuleiro.getLinha(),
							posicaoTabuleiro.getColuna(), pedraAux, true);

					// Adiciona a posição na lista de posições válidas.
					posicoesPermitidas.add(posicaoTabuleiro);
				}
			} else {
				/*
				 * O jogador já realizou um ou mais movimentos durante sua
				 * jogada!
				 */
				if (j.getNumMovimentos() > jogadaHumana.getNumMovimentos()) {
					/*
					 * Só adicionamos uma posição válida se esta jogada possuir
					 * mais movimentos simples, pois caso contrário não haverá
					 * mais movimentos para serem executados, logo não existirão
					 * mais posições válidas para serem escolhidas pelo usuário!
					 */

					/*
					 * Encontrou-se uma posição válida para a jogada do usuário
					 * humano e esta posição será informada ao módulo de
					 * Interface Gráfica...
					 */
					posicaoTabuleiro = j.getMovimento(
							jogadaHumana.getNumMovimentos())
							.getPosicaoDestino();
					pedraAux = estadoJogo.getTabueiro().getPedraJogador(
							posicaoTabuleiro);
					ui.setImageCasa(posicaoTabuleiro.getLinha(),
							posicaoTabuleiro.getColuna(), pedraAux, true);

					// Adiciona a posição na lista de posições válidas.
					posicoesPermitidas.add(posicaoTabuleiro);
				}
			}
		}
		jogadasPermitidas.removeAll(jogadasParaRemover);
	}

	/**
	 * Busca o movimento executado pelo usuário. São analizados os movimentos de
	 * todas as jogadas possíveis. O mesmo movimento pode ser compartilhado por
	 * mais de uma jogada. Se for este o caso, não há problema se o movimento
	 * retornado neste momento pertencer a uma jogada que não será a escolhida
	 * no futuro, pois mesmo que as jogadas sejam diferentes, alguns de seus
	 * movimentos simples poderão ser iguais.
	 * 
	 * @param posicaoInicial
	 *            Posição inicial do movimento.
	 * @param posicaoClick
	 *            Posição final do movimento.
	 * @param indexMovimento
	 *            Índice do movimento na jogada a qual ele pertence.
	 * @return Um {@link MovimentoSimples} que combina com esta jogada.
	 */
	private MovimentoSimples buscarMovimento(PosicaoTabuleiro posicaoInicial,
			PosicaoTabuleiro posicaoClick, int indexMovimento) {
		MovimentoSimples movimento = null;
		for (Jogada j : jogadasPermitidas) {
			movimento = j.getMovimento(indexMovimento);
			if (movimento.getPosicaoOrigem().equals(posicaoInicial)
					&& movimento.getPosicaoDestino().equals(posicaoClick)) {
				break;
			}
		}

		assert (movimento != null);

		return movimento;
	}

	// ##################################
	// # Métodos chamados por GameLogic #
	// ##################################

	@Override
	public void onGameLogic_JogoIniciado(EstadoJogo estadoJogo) {
		jogoIniciado = true;
		ui.configurarLayoutParaJogoIniciado();
		onGameLogic_JogoAtualizado(estadoJogo);
	}

	public void onGameLogic_JogoAtualizado(EstadoJogo estadoJogo) {
		desenharEstadoJogo(estadoJogo);
	}

	@Override
	public void onGameLogic_JogoAcabado(IndicadorResultadoJogo resultadoJogo,
			int numTotalJogadas) {
		if (resultadoJogo != null) {
			if (IndicadorResultadoJogo.JOGO_INTERROMPIDO.equals(resultadoJogo)) {
				ui.desabilitarTabuleiroParaJogadorHumano();
				this.jogadaHumanaHabilitada = false;

				JOptionPane.showMessageDialog(null, "O jogo foi interrompido.");
			} else {
				JOptionPane.showMessageDialog(null,
						"O jogo foi finalizado com o seguinte resultado: "
								+ (resultadoJogo == null ? "Erro"
										: resultadoJogo.toString()));
			}
		}
		ui.configurarLayoutParaJogoFinalizado(numTotalJogadas);
		jogoIniciado = false;
	}

	@Override
	public Jogada onGameLogic_EscolherJogada(EstadoJogo estadoJogo,
			List<Jogada> jogadasPermitidas) throws UIDamasControllerException {
		this.estadoJogo = estadoJogo;
		this.jogadasPermitidas = new ArrayList<Jogada>(jogadasPermitidas);

		this.jogadaHumana = new Jogada();
		this.posicaoInicialMovimento = null;

		mapearPosicoesPermitidas();

		/*
		 * O controle em GameLogic deve identificar se existem jogadas possíveis
		 * para o jogador antes de solicitar uma jogada.
		 */
		assert (posicoesPermitidas.size() > 0);

		synchronized (lockJogadaHumana) {
			this.jogadaHumanaHabilitada = true;
			ui.habilitarTabuleiroParaJogadorHumano();
			try {
				// System.out.println("Esperando jogada do jogador humano...");
				lockJogadaHumana.wait();
				// System.out.println("Jogada do jogador humano efetuada:");
				// System.out.println(jogadaHumana.toString());
			} catch (InterruptedException e) {
				ArchitectureSettings.logLine("ERRO", e.getMessage());
				ArchitectureSettings
						.logLine("THROW",
								"Throw exception in UIDamasController.onGameLogic_EscolherJogada.");
				if (this.jogoInterrompido) {
					throw new UIDamasControllerException(
							"O jogo foi interrompido enquanto aguardava a jogada do jogador humano.");
				} else {
					throw new UIDamasControllerException(
							"Houve um erro enquanto aguardava a jogada do jogador humano.");
				}
			}
			ui.desabilitarTabuleiroParaJogadorHumano();
			this.jogadaHumanaHabilitada = false;
		}

		return jogadaHumana;
	}

}
