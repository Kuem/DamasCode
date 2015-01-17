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
	 * passado por par�metro e as regras de jogo definidas nesta classe.
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

		// Adiciona na lista as jogadas que s�o permitidas, pulando jogadas com
		// n�mero de movimento inferiores ao n�mero de movimento m�ximo
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

		// Passa novamente pela lista eliminando as jogadas cujo n�mero de
		// movimentos s�o inferiores ao n�mero de movimento da maior jogada.
		// Elimina tamb�m jogadas que n�o possue captura, caso exista alguma
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
	 * Encontra todas as pe�as de determinado jogador no tabuleiro.
	 * 
	 * @param tabuleiro
	 *            Descri��o atual do tabuleiro.
	 * @param jogador
	 *            Jogador usado na busca pelas pe�as.
	 * @return Lista de posi��es no tabuleiro onde se encontram todas as pe�as
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
	 * Define o conjunto de jogadas poss�veis de serem realizadas no tabuleiro
	 * por determinada pedra.
	 * 
	 * @param tabuleiro
	 *            Descri��o atual do tabuleiro.
	 * @param origem
	 *            Posi��o de origem da pe�a, cujas jogadas poss�veis para ela
	 *            ser�o definidas.
	 * @return Lista de jogadas poss�veis de serem realizadas no tabuleiro pela
	 *         pe�a que se encontra na posi��o informada.
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
	 * Busca a lista de movimentos simples que s�o v�lidos para uma pedra do
	 * tipo NORMAL ou DAMA posicionada no tabuleiro.
	 * 
	 * @param tabuleiro
	 *            Descri��o atual do tabuleiro.
	 * @param origem
	 *            Posi��o de origem da pe�a no tabuleiro. Esta � a pe�a que
	 *            executa os movimentos simples.
	 * @param pecasJaCapturadas
	 *            Lista de posi��es de pe�as que j� foram capturadas em
	 *            movimentos simples de captura anteriores no qual os novos
	 *            movimentos d�o continuidade.
	 * @param pedra
	 *            Indicador que representa a pedra que executa o movimento.
	 * @param somenteCaptura
	 *            Este par�metro � usado para buscar apenas movimentos de
	 *            captura.
	 * @return Lista de movimentos simples que s�o v�lidos para uma pe�a
	 *         posicionada no tabuleiro diante das condi��es fonecidas..
	 */
	private static List<MovimentoSimples> mapearMovimentosValidos(
			final Tabuleiro tabuleiro, final PosicaoTabuleiro origem,
			final List<PosicaoTabuleiro> pecasJaCapturadas,
			final IndicadorPedraJogador pedra, final boolean somenteCaptura) {
		if (IndicadorTipoPedra.NORMAL.equals(pedra.getTipoPedra())) {
			// Pe�as normais andam somente para "frente", baseado no jogador que
			// ela pertence, ou andam para "tr�s" em movimentos de captura.
			return mapearMovimentosValidosNormal(tabuleiro, origem,
					pecasJaCapturadas, pedra, somenteCaptura);
		} else {
			// Dama pode andar em todas as dire��es!
			return mapearMovimentosValidosDama(tabuleiro, origem,
					pecasJaCapturadas, pedra, somenteCaptura);
		}
	}

	/**
	 * Busca a lista de movimentos simples que s�o v�lidos para uma pe�a do tipo
	 * NORMAL posicionada no tabuleiro. Pe�as normais andam para "frente",
	 * baseado no jogador que ela pertence. Elas podem andar para "tr�s" em
	 * movimentos de captura.
	 * 
	 * @param tabuleiro
	 *            Descri��o atual do tabuleiro.
	 * @param origem
	 *            Posi��o de origem da pe�a no tabuleiro. Esta � a pe�a que
	 *            executa os movimentos simples.
	 * @param pecasJaCapturadas
	 *            Lista de posi��es de pe�as que j� foram capturadas em
	 *            movimentos simples de captura anteriores no qual os novos
	 *            movimentos d�o continuidade.
	 * @param pedra
	 *            Indicador que representa a pedra que executa o movimento.
	 * @param somenteCaptura
	 *            Este par�metro � usado para buscar apenas movimentos de
	 *            captura.
	 * @return Lista de movimentos simples que s�o v�lidos para uma pe�a
	 *         posicionada no tabuleiro diante das condi��es fonecidas.
	 */
	private static List<MovimentoSimples> mapearMovimentosValidosNormal(
			final Tabuleiro tabuleiro, final PosicaoTabuleiro origem,
			final List<PosicaoTabuleiro> pecasJaCapturadas,
			final IndicadorPedraJogador pedra, final boolean somenteCaptura) {
		List<MovimentoSimples> movimentosValidos = new ArrayList<MovimentoSimples>();
		boolean jogador1 = IndicadorJogador.JOGADOR1.equals(pedra.getJogador());
		boolean jogador2 = IndicadorJogador.JOGADOR2.equals(pedra.getJogador());

		MovimentoSimples movimento;

		// Jogador 2 s� executa movimentos para NORDESTE se forem movimentos de
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

		// Jogador 1 s� executa movimentos para NORDESTE se forem movimentos de
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
	 * Retorna um objeto que descreve o movimento simples de pe�a NORMAL
	 * informada, se ela for v�lida, ou null, caso contr�rio.
	 * 
	 * @param tabuleiro
	 *            Descri��o atual do tabuleiro.
	 * @param origem
	 *            Posi��o de origem da pe�a no tabuleiro. Esta � a pe�a que
	 *            executa o movimento simples.
	 * @param pecasJaCapturadas
	 *            Lista de posi��es de pe�as que j� foram capturadas em
	 *            movimentos simples anteriores no qual este movimento d�
	 *            continuidade.
	 * @param direcao
	 *            Dire��oo do movimento, partindo da posi��o de origem
	 *            informada.
	 * @param pedra
	 *            Indicador que representa a pedra que executa o movimento.
	 * @param somenteCaptura
	 *            Este par�metro � usado para buscar apenas movimentos de
	 *            captura.
	 * @return Objeto que descreve o movimento simples informado, ou null se o
	 *         movimento informado n�o for v�lido diante das condi��es
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
			// A posi��o solicitada existe no tabuleiro!
			pedraAux = tabuleiro.getPedraJogador(posicaoCaptura);
			if (pedraAux != null) {
				// Existe uma pedra nesta posi��o!
				// Analizando movimento com captura...
				pecaNaoCapturada = ((pecasJaCapturadas == null) || !pecasJaCapturadas
						.contains(posicaoCaptura));
				if (pecaNaoCapturada
						&& !pedraAux.getJogador().equals(pedra.getJogador())) {
					posicaoDestino = tabuleiro.calcularPosicao(posicaoCaptura,
							direcao);
					if ((posicaoDestino != null)
							&& (tabuleiro.getPedraJogador(posicaoDestino) == null)) {
						// A posi��o de destino existe e n�o possui pedra nela!
						// Movimento de captura v�lido na dire��o informada!
						return new MovimentoSimples(origem, posicaoCaptura,
								posicaoDestino);
					}
				}
			} else {
				// N�o existe pedra nesta posi��o!
				// Movimento sem captura...
				if (!somenteCaptura) {
					// Movimento v�lido na dire��o informada!
					return new MovimentoSimples(origem, posicaoCaptura);
				}
			}
		}

		return null;
	}

	/**
	 * Busca a lista de movimentos simples que s�o v�lidos para uma pe�a do tipo
	 * DAMA posicionada no tabuleiro. Pe�as do tipo dama andam em todas as
	 * dire��es, quantas casas quiser. Elas podem capturar outras pedras andando
	 * quantas casas quiser, mas pulando apenas uma pedra a cada movimento.
	 * 
	 * @param tabuleiro
	 *            Descri��o atual do tabuleiro.
	 * @param origem
	 *            Posi��o de origem da pe�a no tabuleiro. Esta � a pe�a que
	 *            executa os movimentos simples.
	 * @param pecasJaCapturadas
	 *            Lista de posi��es de pe�as que j� foram capturadas em
	 *            movimentos simples de captura anteriores no qual os novos
	 *            movimentos d�o continuidade.
	 * @param pedra
	 *            Indicador que representa a pedra que executa o movimento.
	 * @param somenteCaptura
	 *            Este par�metro � usado para buscar apenas movimentos de
	 *            captura.
	 * @return Lista de movimentos simples que s�o v�lidos para uma pe�a
	 *         posicionada no tabuleiro diante das condi��es fonecidas.
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
	 * Retorna uma lista de movimentos simples v�lidos para a pe�a do tipo DAMA
	 * na dire��o informada.
	 * 
	 * @param tabuleiro
	 *            Descri��o atual do tabuleiro.
	 * @param origem
	 *            Posi��o de origem da pe�a no tabuleiro. Esta � a pe�a que
	 *            executa o movimento simples.
	 * @param pecasJaCapturadas
	 *            Lista de posi��es de pe�as que j� foram capturadas em
	 *            movimentos simples anteriores no qual este movimento d�
	 *            continuidade.
	 * @param direcao
	 *            Dire��oo do movimento, partindo da posi��o de origem
	 *            informada.
	 * @param pedra
	 *            Indicador que representa a pedra que executa o movimento.
	 * @param somenteCaptura
	 *            Este par�metro � usado para buscar apenas movimentos de
	 *            captura.
	 * @return Lista de movimentos simples v�lidos para a pe�a do tipo DAMA na
	 *         dire��o informada.
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
				// Se a posi��o n�o existe para a busca, pois chegou ao limite
				// do tabuleiro!
				break;
			}

			pedraAux = tabuleiro.getPedraJogador(posicaoDestino);
			if (pedraAux != null) {
				// Existe uma pedra nesta posi��o!
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
							// Se a posi��o n�o existe para a busca, pois chegou
							// ao limite do tabuleiro!
							break;
						}

						pedraAux = tabuleiro.getPedraJogador(posicaoDestino);
						if (pedraAux != null) {
							// Existe uma pedra nesta posi��o!
							// Para a busca pois n�o se pode capturar duas pe�as
							// na mesma jogada...
							break;
						} else {
							// N�o existe pedra nesta posi��o!
							// Movimento de captura v�lido!
							movimentosValidos.add(new MovimentoSimples(origem,
									posicaoCaptura, posicaoDestino));
						}
					} while (true);
				}
				break;
			} else {
				// N�o existe pedra nesta posi��o!
				// Movimento sem captura...
				if (!somenteCaptura) {
					// Movimento v�lido na dire��o informada!
					movimentosValidos.add(new MovimentoSimples(origem,
							posicaoDestino));
				}
			}
		} while (true);

		return movimentosValidos;
	}

	/**
	 * Obt�m a lista de jogadas v�lidas para o �ltimo estado de jogo atualizado.
	 * 
	 * @return Lista de jogadas permitidas segundo as regras.
	 */
	public List<Jogada> getJogadasPermitidas() {
		return this.jogadasPermitidas;
	}

	/**
	 * Verifica se determinada jogada � permitida, ou seja, se esta jogada est�
	 * presente na lista de jogadas permitidas recentemente calculadas.
	 * 
	 * @param jogada
	 *            Jogada a ser verificada.
	 * @return "true" se a jogada � permitida, ou "false" caso contr�rio.
	 */
	public boolean isJogadaPermitida(final Jogada jogada) {
		return this.jogadasPermitidas.contains(jogada);
	}

	/**
	 * Calcula as jogadas permitidas a partir de um novo estado de jogo.
	 * 
	 * @param estadoJogo
	 *            Estado de jogo a partir do qual as jogadas permitidas ser�o
	 *            calculadas.
	 * @return C�pia de nova lista de jogadas permitidas calculadas.
	 */
	public List<Jogada> calcularJogadasPermitidas(final EstadoJogo estadoJogo) {
		atualizarRegras(estadoJogo);
		return Utils.copiarJogadas(getJogadasPermitidas());
	}
}
