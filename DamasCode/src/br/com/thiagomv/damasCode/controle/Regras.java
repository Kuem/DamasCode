package br.com.thiagomv.damasCode.controle;

import java.util.ArrayList;
import java.util.List;

import br.com.thiagomv.damasCode.constantes.IndicadorDirecao;
import br.com.thiagomv.damasCode.constantes.IndicadorJogador;
import br.com.thiagomv.damasCode.constantes.IndicadorPedraJogador;
import br.com.thiagomv.damasCode.constantes.IndicadorTipoPedra;
import br.com.thiagomv.damasCode.estruturas.EstadoJogo;
import br.com.thiagomv.damasCode.estruturas.Jogada;
import br.com.thiagomv.damasCode.estruturas.MovimentoSimples;
import br.com.thiagomv.damasCode.estruturas.PosicaoTabuleiro;
import br.com.thiagomv.damasCode.estruturas.Tabuleiro;
import br.com.thiagomv.damasCode.ia.Utils;

/**
 * Esta classe implementa todas as regras que definem quais jogadas podem ser
 * realizadas, de acordo com o estado atual do jogo.
 * 
 * @author Thiago Mendes Vieira
 * 
 *         20/09/2014
 */
public class Regras {
	private final List<Jogada> jogadasPermitidas = new ArrayList<Jogada>();

	/**
	 * Atualiza a lista de jogadas permitidas de acordo com o estado do jogo
	 * passado por parâmetro e as regras de jogo definidas nesta classe.
	 * 
	 * @param estadoJogo
	 *            Estado atual do jogo.
	 */
	public void atualizarRegras(final EstadoJogo estadoJogo) {
		jogadasPermitidas.clear();

		Tabuleiro tabuleiro = estadoJogo.getTabueiro();
		List<PosicaoTabuleiro> posicoes = mapearPosicoesPecasJogador(tabuleiro,
				estadoJogo.getJogadorAtual());

		int numMovimentosMaiorJogada = 0;
		int numMovimentosJogadaAtual;

		// Adiciona na lista as jogadas que são permitidas, pulando jogadas com
		// número de movimento inferiores ao número de movimento máximo
		// encontrado.
		List<Jogada> jogadasTemp;
		boolean temCaptura = false;
		for (PosicaoTabuleiro origem : posicoes) {
			jogadasTemp = mapearJogadas(tabuleiro, origem);
			for (Jogada jogada : jogadasTemp) {
				numMovimentosJogadaAtual = jogada.getNumMovimentos();
				if (numMovimentosJogadaAtual < numMovimentosMaiorJogada) {
					continue;
				}
				numMovimentosMaiorJogada = numMovimentosJogadaAtual;
				temCaptura = (temCaptura || jogada.isMovimentoComCaptura());
				jogadasPermitidas.add(jogada);
			}
		}

		// Passa novamente pela lista eliminando as jogadas cujo número de
		// movimentos são inferiores ao número de movimento da maior jogada.
		// Elimina também jogadas que não possue captura, caso exista alguma
		// jogada com captura.
		List<Jogada> jogadasParaRemoverDaLista = new ArrayList<Jogada>();
		for (Jogada jogada : jogadasPermitidas) {
			numMovimentosJogadaAtual = jogada.getNumMovimentos();
			if (numMovimentosJogadaAtual < numMovimentosMaiorJogada) {
				jogadasParaRemoverDaLista.add(jogada);
				continue;
			}
			if (temCaptura && !jogada.isMovimentoComCaptura()) {
				jogadasParaRemoverDaLista.add(jogada);
				continue;
			}
		}
		jogadasPermitidas.removeAll(jogadasParaRemoverDaLista);
		jogadasParaRemoverDaLista.clear();
	}

	/**
	 * Encontra todas as peças de determinado jogador no tabuleiro.
	 * 
	 * @param tabuleiro
	 *            Descrição atual do tabuleiro.
	 * @param jogador
	 *            Jogador usado na busca pelas peças.
	 * @return Lista de posições no tabuleiro onde se encontram todas as peças
	 *         do jogador informado.
	 */
	private static List<PosicaoTabuleiro> mapearPosicoesPecasJogador(
			final Tabuleiro tabuleiro, final IndicadorJogador jogador) {
		List<PosicaoTabuleiro> posicoes = new ArrayList<PosicaoTabuleiro>();

		IndicadorPedraJogador pedraAux;
		int numLinhas = tabuleiro.getNumLinhas();
		int numColunas = tabuleiro.getNumColunas();
		for (int L = 0; L < numLinhas; L++) {
			for (int C = 0; C < numColunas; C++) {
				pedraAux = tabuleiro.getPedraJogador(L, C);
				if ((pedraAux != null) && (pedraAux.getJogador() == jogador)) {
					posicoes.add(new PosicaoTabuleiro(L, C));
				}
			}
		}

		return posicoes;
	}

	/**
	 * Define o conjunto de jogadas possíveis de serem realizadas no tabuleiro
	 * por determinada pedra.
	 * 
	 * @param tabuleiro
	 *            Descrição atual do tabuleiro.
	 * @param origem
	 *            Posição de origem da peça, cujas jogadas possíveis para ela
	 *            serão definidas.
	 * @return Lista de jogadas possíveis de serem realizadas no tabuleiro pela
	 *         peça que se encontra na posição informada.
	 */
	private static List<Jogada> mapearJogadas(final Tabuleiro tabuleiro,
			final PosicaoTabuleiro origem) {
		List<Jogada> movimentosComCaptura = new ArrayList<Jogada>();
		List<Jogada> movimentosSemCaptura = new ArrayList<Jogada>();
		IndicadorPedraJogador pedraJogador;

		pedraJogador = tabuleiro.getPedraJogador(origem);
		List<MovimentoSimples> movimentosIniciais = mapearMovimentosValidos(
				tabuleiro, origem, null, pedraJogador, false);

		Jogada jogadaAux;
		for (MovimentoSimples ms : movimentosIniciais) {
			jogadaAux = new Jogada();
			jogadaAux.adicionarMovimentoSimples(ms);
			if (ms.getPosicaoCaptura() != null) {
				movimentosComCaptura.add(jogadaAux);
			} else {
				movimentosSemCaptura.add(jogadaAux);
			}
		}

		if (movimentosComCaptura.size() > 0) {
			List<MovimentoSimples> movimentosSequenciais;
			List<Jogada> jogadasParaRemover = new ArrayList<Jogada>();
			List<Jogada> jogadasParaAdicionar = new ArrayList<Jogada>();
			boolean movimentoAdicionado;
			do {
				movimentoAdicionado = false;
				for (Jogada j : movimentosComCaptura) {
					pedraJogador = tabuleiro.getPedraJogador(j
							.getPosicaoInicial());
					movimentosSequenciais = mapearMovimentosValidos(tabuleiro,
							j.getPosicaoFinal(), j.getPedrasCapturadas(),
							pedraJogador, true);
					if (movimentosSequenciais.size() > 0) {
						movimentoAdicionado = true;
						jogadasParaRemover.add(j);
						for (MovimentoSimples ms : movimentosSequenciais) {
							jogadaAux = (Jogada) j.clone();
							jogadaAux.adicionarMovimentoSimples(ms);
							jogadasParaAdicionar.add(jogadaAux);
						}
					}
				}
				if (movimentoAdicionado) {
					movimentosComCaptura.removeAll(jogadasParaRemover);
					movimentosComCaptura.addAll(jogadasParaAdicionar);
					jogadasParaRemover.clear();
					jogadasParaAdicionar.clear();
				}
			} while (movimentoAdicionado);
			return movimentosComCaptura;
		} else {
			return movimentosSemCaptura;
		}
	}

	/**
	 * Busca a lista de movimentos simples que são válidos para uma pedra do
	 * tipo NORMAL ou DAMA posicionada no tabuleiro.
	 * 
	 * @param tabuleiro
	 *            Descrição atual do tabuleiro.
	 * @param origem
	 *            Posição de origem da peça no tabuleiro. Esta é a peça que
	 *            executa os movimentos simples.
	 * @param pecasJaCapturadas
	 *            Lista de posições de peças que já foram capturadas em
	 *            movimentos simples de captura anteriores no qual os novos
	 *            movimentos dão continuidade.
	 * @param pedra
	 *            Indicador que representa a pedra que executa o movimento.
	 * @param somenteCaptura
	 *            Este parâmetro é usado para buscar apenas movimentos de
	 *            captura.
	 * @return Lista de movimentos simples que são válidos para uma peça
	 *         posicionada no tabuleiro diante das condições fonecidas..
	 */
	private static List<MovimentoSimples> mapearMovimentosValidos(
			final Tabuleiro tabuleiro, final PosicaoTabuleiro origem,
			final List<PosicaoTabuleiro> pecasJaCapturadas,
			final IndicadorPedraJogador pedra, final boolean somenteCaptura) {
		if (IndicadorTipoPedra.NORMAL.equals(pedra.getTipoPedra())) {
			// Peças normais andam somente para "frente", baseado no jogador que
			// ela pertence, ou andam para "trás" em movimentos de captura.
			return mapearMovimentosValidosNormal(tabuleiro, origem,
					pecasJaCapturadas, pedra, somenteCaptura);
		} else {
			// Dama pode andar em todas as direções!
			return mapearMovimentosValidosDama(tabuleiro, origem,
					pecasJaCapturadas, pedra, somenteCaptura);
		}
	}

	/**
	 * Busca a lista de movimentos simples que são válidos para uma peça do tipo
	 * NORMAL posicionada no tabuleiro. Peças normais andam para "frente",
	 * baseado no jogador que ela pertence. Elas podem andar para "trás" em
	 * movimentos de captura.
	 * 
	 * @param tabuleiro
	 *            Descrição atual do tabuleiro.
	 * @param origem
	 *            Posição de origem da peça no tabuleiro. Esta é a peça que
	 *            executa os movimentos simples.
	 * @param pecasJaCapturadas
	 *            Lista de posições de peças que já foram capturadas em
	 *            movimentos simples de captura anteriores no qual os novos
	 *            movimentos dão continuidade.
	 * @param pedra
	 *            Indicador que representa a pedra que executa o movimento.
	 * @param somenteCaptura
	 *            Este parâmetro é usado para buscar apenas movimentos de
	 *            captura.
	 * @return Lista de movimentos simples que são válidos para uma peça
	 *         posicionada no tabuleiro diante das condições fonecidas.
	 */
	private static List<MovimentoSimples> mapearMovimentosValidosNormal(
			final Tabuleiro tabuleiro, final PosicaoTabuleiro origem,
			final List<PosicaoTabuleiro> pecasJaCapturadas,
			final IndicadorPedraJogador pedra, final boolean somenteCaptura) {
		List<MovimentoSimples> movimentosValidos = new ArrayList<MovimentoSimples>();
		boolean jogador1 = IndicadorJogador.JOGADOR1.equals(pedra.getJogador());
		boolean jogador2 = IndicadorJogador.JOGADOR2.equals(pedra.getJogador());

		MovimentoSimples movimento;

		// Jogador 2 só executa movimentos para NORDESTE se forem movimentos de
		// captura...
		if ((movimento = mapearMovimentoValidoNormal(tabuleiro, origem,
				pecasJaCapturadas, IndicadorDirecao.NOROESTE, pedra, jogador2
						|| somenteCaptura)) != null) {
			movimentosValidos.add(movimento);
		}
		if ((movimento = mapearMovimentoValidoNormal(tabuleiro, origem,
				pecasJaCapturadas, IndicadorDirecao.NORDESTE, pedra, jogador2
						|| somenteCaptura)) != null) {
			movimentosValidos.add(movimento);
		}

		// Jogador 1 só executa movimentos para NORDESTE se forem movimentos de
		// captura...
		if ((movimento = mapearMovimentoValidoNormal(tabuleiro, origem,
				pecasJaCapturadas, IndicadorDirecao.SUDESTE, pedra, jogador1
						|| somenteCaptura)) != null) {
			movimentosValidos.add(movimento);
		}
		if ((movimento = mapearMovimentoValidoNormal(tabuleiro, origem,
				pecasJaCapturadas, IndicadorDirecao.SUDOESTE, pedra, jogador1
						|| somenteCaptura)) != null) {
			movimentosValidos.add(movimento);
		}

		return movimentosValidos;
	}

	/**
	 * Retorna um objeto que descreve o movimento simples de peça NORMAL
	 * informada, se ela for válida, ou null, caso contrário.
	 * 
	 * @param tabuleiro
	 *            Descrição atual do tabuleiro.
	 * @param origem
	 *            Posição de origem da peça no tabuleiro. Esta é a peça que
	 *            executa o movimento simples.
	 * @param pecasJaCapturadas
	 *            Lista de posições de peças que já foram capturadas em
	 *            movimentos simples anteriores no qual este movimento dá
	 *            continuidade.
	 * @param direcao
	 *            Direçãoo do movimento, partindo da posição de origem
	 *            informada.
	 * @param pedra
	 *            Indicador que representa a pedra que executa o movimento.
	 * @param somenteCaptura
	 *            Este parâmetro é usado para buscar apenas movimentos de
	 *            captura.
	 * @return Objeto que descreve o movimento simples informado, ou null se o
	 *         movimento informado não for válido diante das condições
	 *         fonecidas.
	 */
	private static MovimentoSimples mapearMovimentoValidoNormal(
			final Tabuleiro tabuleiro, final PosicaoTabuleiro origem,
			final List<PosicaoTabuleiro> pecasJaCapturadas,
			final IndicadorDirecao direcao, final IndicadorPedraJogador pedra,
			final boolean somenteCaptura) {
		PosicaoTabuleiro posicaoCaptura;
		PosicaoTabuleiro posicaoDestino;
		IndicadorPedraJogador pedraAux;
		boolean pecaNaoCapturada;

		posicaoCaptura = tabuleiro.calcularPosicao(origem, direcao);
		if (posicaoCaptura != null) {
			// A posição solicitada existe no tabuleiro!
			pedraAux = tabuleiro.getPedraJogador(posicaoCaptura);
			if (pedraAux != null) {
				// Existe uma pedra nesta posição!
				// Analizando movimento com captura...
				pecaNaoCapturada = ((pecasJaCapturadas == null) || !pecasJaCapturadas
						.contains(posicaoCaptura));
				if (pecaNaoCapturada
						&& !pedraAux.getJogador().equals(pedra.getJogador())) {
					posicaoDestino = tabuleiro.calcularPosicao(posicaoCaptura,
							direcao);
					if ((posicaoDestino != null)
							&& (tabuleiro.getPedraJogador(posicaoDestino) == null)) {
						// A posição de destino existe e não possui pedra nela!
						// Movimento de captura válido na direção informada!
						return new MovimentoSimples(origem, posicaoCaptura,
								posicaoDestino);
					}
				}
			} else {
				// Não existe pedra nesta posição!
				// Movimento sem captura...
				if (!somenteCaptura) {
					// Movimento válido na direção informada!
					return new MovimentoSimples(origem, posicaoCaptura);
				}
			}
		}

		return null;
	}

	/**
	 * Busca a lista de movimentos simples que são válidos para uma peça do tipo
	 * DAMA posicionada no tabuleiro. Peças do tipo dama andam em todas as
	 * direções, quantas casas quiser. Elas podem capturar outras pedras andando
	 * quantas casas quiser, mas pulando apenas uma pedra a cada movimento.
	 * 
	 * @param tabuleiro
	 *            Descrição atual do tabuleiro.
	 * @param origem
	 *            Posição de origem da peça no tabuleiro. Esta é a peça que
	 *            executa os movimentos simples.
	 * @param pecasJaCapturadas
	 *            Lista de posições de peças que já foram capturadas em
	 *            movimentos simples de captura anteriores no qual os novos
	 *            movimentos dão continuidade.
	 * @param pedra
	 *            Indicador que representa a pedra que executa o movimento.
	 * @param somenteCaptura
	 *            Este parâmetro é usado para buscar apenas movimentos de
	 *            captura.
	 * @return Lista de movimentos simples que são válidos para uma peça
	 *         posicionada no tabuleiro diante das condições fonecidas.
	 */
	private static List<MovimentoSimples> mapearMovimentosValidosDama(
			final Tabuleiro tabuleiro, final PosicaoTabuleiro origem,
			final List<PosicaoTabuleiro> pecasJaCapturadas,
			final IndicadorPedraJogador pedra, final boolean somenteCaptura) {
		List<MovimentoSimples> movimentosValidos = new ArrayList<MovimentoSimples>();

		List<MovimentoSimples> movimentos;
		movimentos = mapearMovimentoValidoDama(tabuleiro, origem,
				pecasJaCapturadas, IndicadorDirecao.NOROESTE, pedra,
				somenteCaptura);
		movimentosValidos.addAll(movimentos);

		movimentos = mapearMovimentoValidoDama(tabuleiro, origem,
				pecasJaCapturadas, IndicadorDirecao.NORDESTE, pedra,
				somenteCaptura);
		movimentosValidos.addAll(movimentos);

		movimentos = mapearMovimentoValidoDama(tabuleiro, origem,
				pecasJaCapturadas, IndicadorDirecao.SUDESTE, pedra,
				somenteCaptura);
		movimentosValidos.addAll(movimentos);

		movimentos = mapearMovimentoValidoDama(tabuleiro, origem,
				pecasJaCapturadas, IndicadorDirecao.SUDOESTE, pedra,
				somenteCaptura);
		movimentosValidos.addAll(movimentos);

		return movimentosValidos;
	}

	/**
	 * Retorna uma lista de movimentos simples válidos para a peça do tipo DAMA
	 * na direção informada.
	 * 
	 * @param tabuleiro
	 *            Descrição atual do tabuleiro.
	 * @param origem
	 *            Posição de origem da peça no tabuleiro. Esta é a peça que
	 *            executa o movimento simples.
	 * @param pecasJaCapturadas
	 *            Lista de posições de peças que já foram capturadas em
	 *            movimentos simples anteriores no qual este movimento dá
	 *            continuidade.
	 * @param direcao
	 *            Direçãoo do movimento, partindo da posição de origem
	 *            informada.
	 * @param pedra
	 *            Indicador que representa a pedra que executa o movimento.
	 * @param somenteCaptura
	 *            Este parâmetro é usado para buscar apenas movimentos de
	 *            captura.
	 * @return Lista de movimentos simples válidos para a peça do tipo DAMA na
	 *         direção informada.
	 */
	private static List<MovimentoSimples> mapearMovimentoValidoDama(
			final Tabuleiro tabuleiro, final PosicaoTabuleiro origem,
			final List<PosicaoTabuleiro> pecasJaCapturadas,
			final IndicadorDirecao direcao, final IndicadorPedraJogador pedra,
			final boolean somenteCaptura) {
		PosicaoTabuleiro posicaoCaptura;
		PosicaoTabuleiro posicaoDestino;
		IndicadorPedraJogador pedraAux;
		boolean pecaNaoCapturada;

		List<MovimentoSimples> movimentosValidos = new ArrayList<MovimentoSimples>();

		posicaoDestino = origem;
		do {
			posicaoDestino = tabuleiro.calcularPosicao(posicaoDestino, direcao);
			if (posicaoDestino == null) {
				// Se a posição não existe para a busca, pois chegou ao limite
				// do tabuleiro!
				break;
			}

			pedraAux = tabuleiro.getPedraJogador(posicaoDestino);
			if (pedraAux != null) {
				// Existe uma pedra nesta posição!
				// Analizando movimento com captura...
				posicaoCaptura = posicaoDestino;
				pecaNaoCapturada = ((pecasJaCapturadas == null) || !pecasJaCapturadas
						.contains(posicaoCaptura));
				if (pecaNaoCapturada
						&& !pedraAux.getJogador().equals(pedra.getJogador())) {
					do {
						posicaoDestino = tabuleiro.calcularPosicao(
								posicaoDestino, direcao);
						if (posicaoDestino == null) {
							// Se a posição não existe para a busca, pois chegou
							// ao limite do tabuleiro!
							break;
						}

						pedraAux = tabuleiro.getPedraJogador(posicaoDestino);
						if (pedraAux != null) {
							// Existe uma pedra nesta posição!
							// Para a busca pois não se pode capturar duas peças
							// na mesma jogada...
							break;
						} else {
							// Não existe pedra nesta posição!
							// Movimento de captura válido!
							movimentosValidos.add(new MovimentoSimples(origem,
									posicaoCaptura, posicaoDestino));
						}
					} while (true);
				}
				break;
			} else {
				// Não existe pedra nesta posição!
				// Movimento sem captura...
				if (!somenteCaptura) {
					// Movimento válido na direção informada!
					movimentosValidos.add(new MovimentoSimples(origem,
							posicaoDestino));
				}
			}
		} while (true);

		return movimentosValidos;
	}

	/**
	 * Obtém a lista de jogadas válidas para o último estado de jogo atualizado.
	 * 
	 * @return Lista de jogadas permitidas segundo as regras.
	 */
	public List<Jogada> getJogadasPermitidas() {
		return this.jogadasPermitidas;
	}

	/**
	 * Verifica se determinada jogada é permitida, ou seja, se esta jogada está
	 * presente na lista de jogadas permitidas recentemente calculadas.
	 * 
	 * @param jogada
	 *            Jogada a ser verificada.
	 * @return "true" se a jogada é permitida, ou "false" caso contrário.
	 */
	public boolean isJogadaPermitida(final Jogada jogada) {
		return this.jogadasPermitidas.contains(jogada);
	}

	/**
	 * Calcula as jogadas permitidas a partir de um novo estado de jogo.
	 * 
	 * @param estadoJogo
	 *            Estado de jogo a partir do qual as jogadas permitidas serão
	 *            calculadas.
	 * @return Cópia de nova lista de jogadas permitidas calculadas.
	 */
	public List<Jogada> calcularJogadasPermitidas(final EstadoJogo estadoJogo) {
		atualizarRegras(estadoJogo);
		return Utils.copiarJogadas(getJogadasPermitidas());
	}
}
