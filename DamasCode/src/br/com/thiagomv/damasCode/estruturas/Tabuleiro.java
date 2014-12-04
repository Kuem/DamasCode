package br.com.thiagomv.damasCode.estruturas;

import br.com.thiagomv.damasCode.constantes.IndicadorDirecao;
import br.com.thiagomv.damasCode.constantes.IndicadorJogador;
import br.com.thiagomv.damasCode.constantes.IndicadorPedraJogador;
import br.com.thiagomv.damasCode.controle.ArchitectureSettings;

/**
 * Representa o tabuleiro do jogo. � definido por uma matriz de tamanho fixo que
 * representa cada posi��o do tabuleiro, indicando a pedra que ocupa a posi��o
 * (se houver pedra) e a qual jogador a pedra pertence.
 * 
 * @author Thiago Mendes Vieira
 * 
 *         20/09/2014
 */
public class Tabuleiro implements Cloneable {
	/**
	 * N�mero de linhas deste tabuleiro.
	 */
	private final int numLinhas;

	/**
	 * N�mero de colunas deste tabuleiro.
	 */
	private final int numColunas;

	/**
	 * Mapa de posi��es do tabuleiro no formato [linha][coluna]. Se a posi��o
	 * possuir uma pedra, um indicador que descreve a pedra ser� retornado. Caso
	 * contr�rio, o valor retornado ser� "null".
	 */
	private final IndicadorPedraJogador[][] pedrasPosicoes;

	/**
	 * Indica se determinada posi��o do tabuleiro � uma casa habit�vel,
	 * geralmente indicada pela cor preta no tabuleiro.
	 * 
	 * @param linha
	 *            Linha no tabuleiro.
	 * @param coluna
	 *            Coluna no tabuleiro.
	 * @return "true" se a cada indicada � habit�vel.
	 */
	public static boolean isPosicaoValidaParaHabitar(int linha, int coluna) {
		return ((linha + coluna) % 2 == 0);
	}

	/**
	 * Cria um tabuleiro vazio com tamanho definido.
	 * 
	 * @param linhas
	 *            N�mero de linhas definido para o tabuleiro.
	 * @param colunas
	 *            N�mero de colunas definido para o tabuleiro.
	 */
	public Tabuleiro(int linhas, int colunas) {
		this.numLinhas = linhas;
		this.numColunas = colunas;
		this.pedrasPosicoes = new IndicadorPedraJogador[numLinhas][numColunas];
	}

	/**
	 * Estabelece as pedras de cada jogador em suas posi��es iniciais para dar
	 * in�cio a uma nova partida.
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
	 *            �ndice da linha.
	 * @return "true" se a linha pertence, ou "false" caso contr�rio.
	 */
	private boolean isLinhaRegiaoBrancas(int linha) {
		return (linha < ((ArchitectureSettings.getNumLinhas() - 2) / 2));
	}

	/**
	 * Verifica se determinada linha do tabuleiro pertence ao conjunto de linhas
	 * que possuem pedras pretas no inicio da partida.
	 * 
	 * @param linha
	 *            �ndice da linha.
	 * @return "true" se a linha pertence, ou "false" caso contr�rio.
	 */
	private boolean isLinhasRegiaoPretas(int linha) {
		return (linha > ((ArchitectureSettings.getNumLinhas() - 2) / 2) + 1);
	}

	/**
	 * Indica se a posi��o informada � uma posi��o na qual determinado jogador
	 * promove sua pe�a para Dama ao parar sobre a posi��o.
	 * 
	 * @param jogador
	 *            Jogador que promove sua pe�a para DAMA.
	 * @param posicao
	 *            Posi��o a ser analizada.
	 * @return "true" se quando a pe�a NORMAL do jogador informado deve ser
	 *         promovida a DAMA ao parar sobre a posi��o informada.
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
	 * Retorna um indicador que descreve a pedra que ocupa a posi��o informada,
	 * ou "null" caso n�o exista pedra.
	 * 
	 * @param linha
	 *            Linha no tabuleiro.
	 * @param coluna
	 *            Coluna no tabuleiro.
	 * @return Indicador da pedra que ocupa a posi��o no tabuleiro informada, ou
	 *         "null" se n�o existir pedra na posi��o.
	 */
	public IndicadorPedraJogador getPedraJogador(int linha, int coluna) {
		return pedrasPosicoes[linha][coluna];
	}

	/**
	 * Retorna um indicador que descreve a pedra que ocupa a posi��o informada,
	 * ou "null" caso n�o exista pedra.
	 * 
	 * @param posicao
	 *            Posi��o no tabuleiro.
	 * @return Indicador da pedra que ocupa a posi��o no tabuleiro informada, ou
	 *         "null" se n�o existir pedra na posi��o.
	 */
	public IndicadorPedraJogador getPedraJogador(PosicaoTabuleiro posicao) {
		return pedrasPosicoes[posicao.getLinha()][posicao.getColuna()];
	}

	/**
	 * Obt�m o n�mero de linhas deste tabuleiro.
	 * 
	 * @return N�mero de linhas.
	 */
	public int getNumLinhas() {
		return this.numLinhas;
	}

	/**
	 * Obt�m o n�mero de colunas deste tabuleiro.
	 * 
	 * @return N�mero de colunas.
	 */
	public int getNumColunas() {
		return this.numColunas;
	}

	/**
	 * Calcula qual a posi��o do tabuleiro que est� logo ap�s uma posi��o de
	 * origem, seguindo uma determinada dire��o.
	 * 
	 * @param origem
	 *            Posi��o de origem.
	 * @param direcao
	 *            Dire��o.
	 * @return Uma {@link PosicaoTabuleiro} representando a posi��o calculada,
	 *         ou "null" caso as coordenadas da posi��o calculada estejam fora
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
	 * Configura determinada posi��o do tabuleiro.
	 * 
	 * @param posicao
	 *            Posi��o v�lida do tabuleiro.
	 * @param pedra
	 *            Indicador que representa o tipo da pedra e o jogador ao qual
	 *            ela pertence. Esta ser� a nova configura��o na posi��o
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
