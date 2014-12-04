package br.com.thiagomv.damasCode.estruturas;

import br.com.thiagomv.damasCode.constantes.IndicadorDirecao;

/**
 * Representa um movimento simples de uma pedra no tabuleiro. Um movimento
 * simples � definido por uma posi��o de origem e uma posi��o de destino.
 * Opcionalmente pode haver uma posi��o de captura, indicando que este � um
 * movimento com captura. Movimentos s� podem ser efetuados na diagonal. Isso
 * significa que as posi��es de origem, captura e destino devem estar sempre
 * aninhadas em uma das poss�veis dire��es, a partir da origem. As dire��es s�o
 * definidas em {@link IndicadorDirecao}.
 * 
 * @author Thiago Mendes Vieira
 * 
 *         20/09/2014
 */
public class MovimentoSimples {
	private final PosicaoTabuleiro posicaoOrigem;
	private final PosicaoTabuleiro posicaoCaptura;
	private final PosicaoTabuleiro posicaoDestino;

	/**
	 * Cria um Movimento Simples sem captura.
	 * 
	 * @param posicaoOrigem
	 *            Posi��o de origem do movimento.
	 * @param posicaoDestino
	 *            PPosi��o de destino do movimento.
	 */
	public MovimentoSimples(PosicaoTabuleiro posicaoOrigem,
			PosicaoTabuleiro posicaoDestino) {
		// Assegura que as posi��es de origem e destino sejam diferentes.
		assert (!posicaoDestino.equals(posicaoOrigem));

		// Assegura que seja um movimento na diagonal.
		assert (Math
				.abs(posicaoDestino.getColuna() - posicaoOrigem.getColuna()) == Math
				.abs(posicaoDestino.getLinha() - posicaoOrigem.getLinha()));

		this.posicaoOrigem = new PosicaoTabuleiro(posicaoOrigem.getLinha(),
				posicaoOrigem.getColuna());
		this.posicaoCaptura = null;
		this.posicaoDestino = new PosicaoTabuleiro(posicaoDestino.getLinha(),
				posicaoDestino.getColuna());
	}

	/**
	 * Cria um Movimento Simples com captura.
	 * 
	 * @param posicaoOrigem
	 *            Posi��o de origem do movimento.
	 * @param posicaoCaptura
	 *            Posi��o de captura do movimento.
	 * @param posicaoDestino
	 *            PPosi��o de destino do movimento.
	 */
	public MovimentoSimples(PosicaoTabuleiro posicaoOrigem,
			PosicaoTabuleiro posicaoCaptura, PosicaoTabuleiro posicaoDestino) {
		// Assegura que as posi��es de origem, destino e captura sejam
		// diferentes.
		assert (!posicaoDestino.equals(posicaoOrigem)
				&& !posicaoDestino.equals(posicaoCaptura) && !posicaoCaptura
					.equals(posicaoOrigem));

		// Assegura que seja um movimento na diagonal.
		assert (Math
				.abs(posicaoDestino.getColuna() - posicaoOrigem.getColuna()) == Math
				.abs(posicaoDestino.getLinha() - posicaoOrigem.getLinha()));
		// Assegura que a captura esteja na diagonal.
		assert (Math
				.abs(posicaoCaptura.getColuna() - posicaoOrigem.getColuna()) == Math
				.abs(posicaoCaptura.getLinha() - posicaoOrigem.getLinha()));

		// Assegura que a posi��o de captura est� entre as posi��es de origem e
		// destino.
		assert ((Math.signum(posicaoDestino.getColuna()
				- posicaoOrigem.getColuna()) == Math.signum(posicaoCaptura
				.getColuna() - posicaoOrigem.getColuna()))
				&& (Math.signum(posicaoDestino.getLinha()
						- posicaoOrigem.getLinha()) == Math
						.signum(posicaoCaptura.getLinha()
								- posicaoOrigem.getLinha())) && (Math
				.abs(posicaoDestino.getColuna() - posicaoOrigem.getColuna()) > Math
				.abs(posicaoCaptura.getColuna() - posicaoOrigem.getColuna())));

		this.posicaoOrigem = new PosicaoTabuleiro(posicaoOrigem.getLinha(),
				posicaoOrigem.getColuna());
		this.posicaoCaptura = new PosicaoTabuleiro(posicaoCaptura.getLinha(),
				posicaoCaptura.getColuna());
		this.posicaoDestino = new PosicaoTabuleiro(posicaoDestino.getLinha(),
				posicaoDestino.getColuna());
	}

	/**
	 * Obt�m a posi��o de origem deste movimento.
	 * 
	 * @return {@link PosicaoTabuleiro}.
	 */
	public PosicaoTabuleiro getPosicaoOrigem() {
		return this.posicaoOrigem;
	}

	/**
	 * Obt�m a posi��o de captura deste movimento.
	 * 
	 * @return {@link PosicaoTabuleiro}.
	 */
	public PosicaoTabuleiro getPosicaoCaptura() {
		return this.posicaoCaptura;
	}

	/**
	 * Obt�m a posi��o de destino deste movimento.
	 * 
	 * @return {@link PosicaoTabuleiro}.
	 */
	public PosicaoTabuleiro getPosicaoDestino() {
		return this.posicaoDestino;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MovimentoSimples) {
			MovimentoSimples movimentoSimples = (MovimentoSimples) obj;
			boolean posicaoCapturaIguais = false;

			if (this.posicaoCaptura == null) {
				posicaoCapturaIguais = (movimentoSimples.posicaoCaptura == null);
			} else {
				posicaoCapturaIguais = this.posicaoCaptura
						.equals(movimentoSimples.posicaoCaptura);
			}

			if (movimentoSimples.posicaoOrigem.equals(this.posicaoOrigem)
					&& movimentoSimples.posicaoDestino
							.equals(this.posicaoDestino)
					&& (posicaoCapturaIguais)) {
				return true;
			}
		}
		return false;
	}
}
