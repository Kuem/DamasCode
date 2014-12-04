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
	// # M�todos chamados por UIDamas #
	// ################################
	public void iniciarJogo(IndicadorAlgoritmo algoritmoJ1,
			IndicadorHeuristica heuristicaJ1, IndicadorAlgoritmo algoritmoJ2,
			IndicadorHeuristica heuristicaJ2) throws UIDamasControllerException {
		if (jogoIniciado) {
			ArchitectureSettings.logLine("THROW",
					"Throw exception in UIDamasController.iniciarJogo.");
			throw new UIDamasControllerException("O jogo j� foi iniciado!");
		}

		// Estabelece configura��es dos jogadores...
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
					"A jogada humana n�o foi habilitada!");
		}

		PosicaoTabuleiro posicaoClick = new PosicaoTabuleiro(linha, coluna);
		if (posicoesPermitidas.contains(posicaoClick)) {
			if (posicaoInicialMovimento == null) {
				// Seleciona a posi��o incial do movimento
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
					"Ops! Esta jogada n�o � permitida!");
		}

		desenharEstadoJogo(estadoJogo);
		mapearPosicoesPermitidas();

		if (posicoesPermitidas.size() == 0) {
			// Informa o movimento realizado e passa a vez para o advers�rio!
			synchronized (lockJogadaHumana) {
				lockJogadaHumana.notify();
			}
		}
	}

	/**
	 * Envia comandos para a camada de Interface Gr�fica (atrav�s da classe
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
	 * Descobre quais as posi��es do tabuleiro o jogador humano poder� escolher
	 * para jogar. Ao mesmo tempo que procura as posi��es v�lidas, exclui-se as
	 * jogadas que n�o poder�o mais ser escolhidas devido � din�mica de escolha
	 * de uma jogada. A escolha de uma jogada � feita em duas etapas. A primeira
	 * consiste em escolher a posi��o inicial do movimento. A segunda em
	 * escolher a posi��o de destino. Caso mais movimentos possam ser feitos, a
	 * posi��o de destino do �ltimo movimento se transforma na posi��o inicial
	 * do movimento seguinte, e o usu�rio deve ent�o escolher outra posi��o de
	 * destino, e assim sucessivamente at� que n�o existam mais movimentos
	 * poss�veis na jogada.
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
			// Esta jogada � permitida at� o momento!

			if (jogadaHumana.getNumMovimentos() == 0) {
				if (posicaoInicialMovimento == null) {
					/*
					 * Nenhuma pe�a foi selecionada para iniciar o movimento at�
					 * o momento!
					 */

					/*
					 * Encontrou-se uma posi��o v�lida para a jogada do usu�rio
					 * humano e esta posi��o ser� informada ao m�dulo de
					 * Interface Gr�fica...
					 */
					posicaoTabuleiro = j.getMovimento(0).getPosicaoOrigem();
					pedraAux = estadoJogo.getTabueiro().getPedraJogador(
							posicaoTabuleiro);
					ui.setImageCasa(posicaoTabuleiro.getLinha(),
							posicaoTabuleiro.getColuna(), pedraAux, true);

					// Adiciona a posi��o na lista de posi��es v�lidas.
					posicoesPermitidas.add(posicaoTabuleiro);
				} else {
					/*
					 * O jogador j� selecionou alguma pe�a do tabuleiro mas
					 * ainda n�o realizou nenhuma jogada!
					 */
					if (!j.getMovimento(0).getPosicaoOrigem()
							.equals(posicaoInicialMovimento)) {
						jogadasParaRemover.add(j);
						continue;
					}

					/*
					 * Encontrou-se uma posi��o v�lida para a jogada do usu�rio
					 * humano e esta posi��o ser� informada ao m�dulo de
					 * Interface Gr�fica...
					 */
					posicaoTabuleiro = j.getMovimento(0).getPosicaoDestino();
					pedraAux = estadoJogo.getTabueiro().getPedraJogador(
							posicaoTabuleiro);
					ui.setImageCasa(posicaoTabuleiro.getLinha(),
							posicaoTabuleiro.getColuna(), pedraAux, true);

					// Adiciona a posi��o na lista de posi��es v�lidas.
					posicoesPermitidas.add(posicaoTabuleiro);
				}
			} else {
				/*
				 * O jogador j� realizou um ou mais movimentos durante sua
				 * jogada!
				 */
				if (j.getNumMovimentos() > jogadaHumana.getNumMovimentos()) {
					/*
					 * S� adicionamos uma posi��o v�lida se esta jogada possuir
					 * mais movimentos simples, pois caso contr�rio n�o haver�
					 * mais movimentos para serem executados, logo n�o existir�o
					 * mais posi��es v�lidas para serem escolhidas pelo usu�rio!
					 */

					/*
					 * Encontrou-se uma posi��o v�lida para a jogada do usu�rio
					 * humano e esta posi��o ser� informada ao m�dulo de
					 * Interface Gr�fica...
					 */
					posicaoTabuleiro = j.getMovimento(
							jogadaHumana.getNumMovimentos())
							.getPosicaoDestino();
					pedraAux = estadoJogo.getTabueiro().getPedraJogador(
							posicaoTabuleiro);
					ui.setImageCasa(posicaoTabuleiro.getLinha(),
							posicaoTabuleiro.getColuna(), pedraAux, true);

					// Adiciona a posi��o na lista de posi��es v�lidas.
					posicoesPermitidas.add(posicaoTabuleiro);
				}
			}
		}
		jogadasPermitidas.removeAll(jogadasParaRemover);
	}

	/**
	 * Busca o movimento executado pelo usu�rio. S�o analizados os movimentos de
	 * todas as jogadas poss�veis. O mesmo movimento pode ser compartilhado por
	 * mais de uma jogada. Se for este o caso, n�o h� problema se o movimento
	 * retornado neste momento pertencer a uma jogada que n�o ser� a escolhida
	 * no futuro, pois mesmo que as jogadas sejam diferentes, alguns de seus
	 * movimentos simples poder�o ser iguais.
	 * 
	 * @param posicaoInicial
	 *            Posi��o inicial do movimento.
	 * @param posicaoClick
	 *            Posi��o final do movimento.
	 * @param indexMovimento
	 *            �ndice do movimento na jogada a qual ele pertence.
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
	// # M�todos chamados por GameLogic #
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
		 * O controle em GameLogic deve identificar se existem jogadas poss�veis
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
