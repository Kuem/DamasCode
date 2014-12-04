package br.com.thiagomv.damasCode.ia;

import java.util.ArrayList;
import java.util.List;

import br.com.thiagomv.damasCode.constantes.IndicadorJogador;
import br.com.thiagomv.damasCode.constantes.IndicadorPedraJogador;
import br.com.thiagomv.damasCode.constantes.IndicadorTipoPedra;
import br.com.thiagomv.damasCode.estruturas.EstadoJogo;
import br.com.thiagomv.damasCode.estruturas.Jogada;
import br.com.thiagomv.damasCode.estruturas.PosicaoTabuleiro;
import br.com.thiagomv.damasCode.estruturas.Tabuleiro;

public class Utils {
	/**
	 * Cria uma cópia da lista de jogadas informadas. Cada jogada é copiada em
	 * uma nova instância.
	 * 
	 * @param jogadasPermitidas
	 * @return
	 */
	public static List<Jogada> copiarJogadas(
			final List<Jogada> jogadasPermitidas) {
		List<Jogada> copia = new ArrayList<>(jogadasPermitidas.size());
		for (Jogada j : jogadasPermitidas) {
			copia.add((Jogada) j.clone());
		}
		return copia;
	}

	/**
	 * Obtém o estado sucessor a um determinado estado de origem, após a
	 * realização de uma jogada sobre este estado. Não é feita nenhuma
	 * verificação sobre a validade da jogada informada. As estruturas
	 * informadas or parâmetros não são modificadas.
	 * 
	 * @param estadoJogo
	 *            Estado de origem.
	 * @param jogada
	 *            Jogada a ser realizada sobre o estado de origem.
	 * @return Estado obtido após a realização da jogada sobre o estado de
	 *         origem.
	 */
	public static EstadoJogo getEstadoSucessor(final EstadoJogo estadoJogo,
			final Jogada jogada) {
		// Retira do tabuleiro todas as pedras capturadas...
		Tabuleiro tabuleiro = null;
		try {
			tabuleiro = (Tabuleiro) (estadoJogo.getTabueiro().clone());
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("Erro em Utils.getEstadoSucessor().");
		}

		for (PosicaoTabuleiro posicaoCaptura : jogada.getPedrasCapturadas()) {
			tabuleiro.setPedraJogador(posicaoCaptura, null);
		}
		IndicadorPedraJogador pedra = tabuleiro.getPedraJogador(jogada
				.getPosicaoInicial());

		// Atualiza a posição da pedra de origem no movimento...
		tabuleiro.setPedraJogador(jogada.getPosicaoInicial(), null);
		if (IndicadorTipoPedra.NORMAL.equals(pedra.getTipoPedra())
				&& tabuleiro.isPosicaoDama(pedra.getJogador(),
						jogada.getPosicaoFinal())) {
			pedra = pedra.promoverDama();
		}
		tabuleiro.setPedraJogador(jogada.getPosicaoFinal(), pedra);

		// Cria novo estado...
		EstadoJogo novoEstado = new EstadoJogo();
		novoEstado.setTabuleiro(tabuleiro);
		novoEstado.setJogadorAtual(Utils.getAdversario(estadoJogo
				.getJogadorAtual()));

		return novoEstado;
	}

	/**
	 * Obtém o jogador adversário, com base em um jogador "atual" informado por
	 * parâmetro.
	 * 
	 * @param jogadorAtual
	 * @return
	 */
	public static IndicadorJogador getAdversario(
			final IndicadorJogador jogadorAtual) {
		if (IndicadorJogador.JOGADOR1.equals(jogadorAtual)) {
			return IndicadorJogador.JOGADOR2;
		} else {
			return IndicadorJogador.JOGADOR1;
		}
	}

	/**
	 * Avalia o estado do jogo de acordo com a equação (b-p)/(b+p), onde b é o
	 * valor de bn+bd e p é o valor de pn+pd, dado que bn representa a
	 * quantidade de pedras brancas normais; bd representa a quantidade de damas
	 * brancas; pn representa a quantidade de pedras pretas normais; pd
	 * representa a quantidade de damas pretas.
	 * 
	 * @param estadoJogo
	 * @return
	 */
	public static double avaliarEstadoJogo_equacao1(EstadoJogo estadoJogo) {
		Tabuleiro tabuleiro = estadoJogo.getTabueiro();
		int brancasNormal = Utils.getNumPedras(tabuleiro,
				IndicadorPedraJogador.NORMAL_J1);
		int pretasNormal = Utils.getNumPedras(tabuleiro,
				IndicadorPedraJogador.NORMAL_J2);
		int brancasDama = Utils.getNumPedras(tabuleiro,
				IndicadorPedraJogador.DAMA_J1);
		int pretasDama = Utils.getNumPedras(tabuleiro,
				IndicadorPedraJogador.DAMA_J2);
		double brancas = brancasNormal + 3 * brancasDama;
		double pretas = pretasNormal + 3 * pretasDama;

		return (brancas - pretas) / (brancas + pretas);
	}

	/**
	 * Obtém a quantidade de pedras em um tabuleiro que combinam com o
	 * {@link IndicadorTipoPedra} informado.
	 * 
	 * @param tabuleiro
	 * @param pedra
	 * @return
	 */
	public static int getNumPedras(final Tabuleiro tabuleiro,
			final IndicadorPedraJogador pedra) {
		int numLinhas = tabuleiro.getNumLinhas();
		int numColunas = tabuleiro.getNumColunas();
		int numPeras = 0;
		for (int linha = 0; linha < numLinhas; linha++) {
			for (int coluna = 0; coluna < numColunas; coluna++) {
				if (pedra.equals(tabuleiro.getPedraJogador(linha, coluna))) {
					numPeras++;
				}
			}
		}
		return numPeras;
	}

	/**
	 * Gera um valor inteiro aleatório entre o intervalo [min, max].
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int gerarIntAleatorio(int min, int max) {
		return (int) gerarLongAleatorio(min, max);
	}

	/**
	 * Gera um valor inteiro aleatório entre o intervalo [min, max].
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static long gerarLongAleatorio(long min, long max) {
		double inicio = min;
		double largura = max - min + 1;
		return Math.round(Math.floor(inicio + Math.random() * largura));
	}

}
