package br.com.thiagomv.damasCode.estruturas;

import br.com.thiagomv.damasCode.constantes.IndicadorDirecao;
import br.com.thiagomv.damasCode.constantes.IndicadorJogador;
import br.com.thiagomv.damasCode.constantes.IndicadorPedraJogador;
import br.com.thiagomv.damasCode.controle.ArchitectureSettings;

/**
 * Representa o tabuleiro do jogo. É definido por uma matriz de tamanho fixo que
 * representa cada posição do tabuleiro, indicando a pedra que ocupa a posição
 * (se houver pedra) e a qual jogador a pedra pertence.
 * 
 * @author Thiago Mendes Vieira
 * 
 *         20/09/2014
 */
public class Tabuleiro implements Cloneable {
	/**
	 * Número de linhas deste tabuleiro.
	 */
	private final int numLinhas;

	/**
	 * Número de colunas deste tabuleiro.
	 */
	private final int numColunas;

	/**
	 * Mapa de posições do tabuleiro no formato [linha][coluna]. Se a posição
	 * possuir uma pedra, um indicador que descreve a pedra será retornado. Caso
	 * contrário, o valor retornado será "null".
	 */
	private final IndicadorPedraJogador[][] pedrasPosicoes;

	/**
	 * Indica se determinada posição do tabuleiro é uma casa habitável,
	 * geralmente indicada pela cor preta no tabuleiro.
	 * 
	 * @param linha
	 *            Linha no tabuleiro.
	 * @param coluna
	 *            Coluna no tabuleiro.
	 * @return "true" se a cada indicada é habitável.
	 */
	public static boolean isPosicaoValidaParaHabitar(int linha, int coluna) {
		return ((linha + coluna) % 2 == 0);
	}

	/**
	 * Cria um tabuleiro vazio com tamanho definido.
	 * 
	 * @param linhas
	 *            Número de linhas definido para o tabuleiro.
	 * @param colunas
	 *            Número de colunas definido para o tabuleiro.
	 */
	public Tabuleiro(int linhas, int colunas) {
		this.numLinhas = linhas;
		this.numColunas = colunas;
		this.pedrasPosicoes = new IndicadorPedraJogador[numLinhas][numColunas];
	}

	/**
	 * Estabelece as pedras de cada jogador em suas posições iniciais para dar
	 * início a uma nova partida.
	 */
	public void configurarParaNovoJogo() {
		for (int L = 0; L < numLinhas; L++) {
			for (int C = 0; C < numColunas; C++) {
				if (isPosicaoValidaParaHabitar(L, C)) {
					if (isLinhaRegiaoBrancas(L)) {
						pedrasPosicoes[L][C] = IndicadorPedraJogador.NORMAL_J1;
						continue;
					} else if (isLinhasRegiaoPretas(L)) {
						pedrasPosicoes[L][C] = IndicadorPedraJogador.NORMAL_J2;
						continue;
					}
				}
				pedrasPosicoes[L][C] = null;
			}
		}
	}

	/**
	 * Verifica se determinada linha do tabuleiro pertence ao conjunto de linhas
	 * que possuem pedras brancas no inicio da partida.
	 * 
	 * @param linha
	 *            Índice da linha.
	 * @return "true" se a linha pertence, ou "false" caso contrário.
	 */
	private boolean isLinhaRegiaoBrancas(int linha) {
		return (linha < ((ArchitectureSettings.getNumLinhas() - 2) / 2));
	}

	/**
	 * Verifica se determinada linha do tabuleiro pertence ao conjunto de linhas
	 * que possuem pedras pretas no inicio da partida.
	 * 
	 * @param linha
	 *            Índice da linha.
	 * @return "true" se a linha pertence, ou "false" caso contrário.
	 */
	private boolean isLinhasRegiaoPretas(int linha) {
		return (linha > ((ArchitectureSettings.getNumLinhas() - 2) / 2) + 1);
	}

	/**
	 * Indica se a posição informada é uma posição na qual determinado jogador
	 * promove sua peça para Dama ao parar sobre a posição.
	 * 
	 * @param jogador
	 *            Jogador que promove sua peça para DAMA.
	 * @param posicao
	 *            Posição a ser analizada.
	 * @return "true" se quando a peça NORMAL do jogador informado deve ser
	 *         promovida a DAMA ao parar sobre a posição informada.
	 */
	public boolean isPosicaoDama(IndicadorJogador jogador,
			PosicaoTabuleiro posicao) {
		if (isPosicaoValidaParaHabitar(posicao.getLinha(), posicao.getColuna())) {
			switch (jogador) {
			case JOGADOR1:
				return (posicao.getLinha() == (this.numLinhas - 1));
			case JOGADOR2:
				return (posicao.getLinha() == 0);
			}
		}
		return false;
	}

	/**
	 * Retorna um indicador que descreve a pedra que ocupa a posição informada,
	 * ou "null" caso não exista pedra.
	 * 
	 * @param linha
	 *            Linha no tabuleiro.
	 * @param coluna
	 *            Coluna no tabuleiro.
	 * @return Indicador da pedra que ocupa a posição no tabuleiro informada, ou
	 *         "null" se não existir pedra na posição.
	 */
	public IndicadorPedraJogador getPedraJogador(int linha, int coluna) {
		return pedrasPosicoes[linha][coluna];
	}

	/**
	 * Retorna um indicador que descreve a pedra que ocupa a posição informada,
	 * ou "null" caso não exista pedra.
	 * 
	 * @param posicao
	 *            Posição no tabuleiro.
	 * @return Indicador da pedra que ocupa a posição no tabuleiro informada, ou
	 *         "null" se não existir pedra na posição.
	 */
	public IndicadorPedraJogador getPedraJogador(PosicaoTabuleiro posicao) {
		return pedrasPosicoes[posicao.getLinha()][posicao.getColuna()];
	}

	/**
	 * Obtém o número de linhas deste tabuleiro.
	 * 
	 * @return Número de linhas.
	 */
	public int getNumLinhas() {
		return this.numLinhas;
	}

	/**
	 * Obtém o número de colunas deste tabuleiro.
	 * 
	 * @return Número de colunas.
	 */
	public int getNumColunas() {
		return this.numColunas;
	}

	/**
	 * Calcula qual a posição do tabuleiro que está logo após uma posição de
	 * origem, seguindo uma determinada direção.
	 * 
	 * @param origem
	 *            Posição de origem.
	 * @param direcao
	 *            Direção.
	 * @return Uma {@link PosicaoTabuleiro} representando a posição calculada,
	 *         ou "null" caso as coordenadas da posição calculada estejam fora
	 *         dos limites do tabuleiro.
	 */
	public PosicaoTabuleiro calcularPosicao(PosicaoTabuleiro origem,
			IndicadorDirecao direcao) {
		int linha = origem.getLinha();
		int coluna = origem.getColuna();

		switch (direcao) {
		case NOROESTE:
			linha++;
			coluna--;
			break;
		case NORDESTE:
			linha++;
			coluna++;
			break;
		case SUDESTE:
			linha--;
			coluna++;
			break;
		case SUDOESTE:
			linha--;
			coluna--;
			break;
		}

		if (linha < 0 || linha >= numLinhas || coluna < 0
				|| coluna >= numColunas) {
			return null;
		} else {
			return new PosicaoTabuleiro(linha, coluna);
		}
	}

	/**
	 * Configura determinada posição do tabuleiro.
	 * 
	 * @param posicao
	 *            Posição válida do tabuleiro.
	 * @param pedra
	 *            Indicador que representa o tipo da pedra e o jogador ao qual
	 *            ela pertence. Esta será a nova configuração na posição
	 *            informada.
	 */
	public void setPedraJogador(PosicaoTabuleiro posicao,
			IndicadorPedraJogador pedra) {
		this.pedrasPosicoes[posicao.getLinha()][posicao.getColuna()] = pedra;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Tabuleiro novoTabuleiro = new Tabuleiro(this.numLinhas, this.numColunas);
		for (int linha = 0; linha < numLinhas; linha++) {
			for (int coluna = 0; coluna < numColunas; coluna++) {
				novoTabuleiro.pedrasPosicoes[linha][coluna] = this.pedrasPosicoes[linha][coluna];
			}
		}
		return novoTabuleiro;
	}
}
