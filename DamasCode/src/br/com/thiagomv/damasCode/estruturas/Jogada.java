package br.com.thiagomv.damasCode.estruturas;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma jogada. Uma jogada � definida por uma sequ�ncia de movimentos
 * simples, contendo no m�nimo 1 movimento simples. Caso o comprimento da
 * sequ�ncia de movimentos simples seja maior que 1, todos os movimentos
 * necessariamente ser�o movimentos de captura. Uma jogada contendo somente 1
 * movimento simples poder� ter captura ou n�o. Caso a jogada possua capturas,
 * esta conter� uma sequ�ncia de Posi��es nas quais ocorreram as capturas. As
 * sequ�ncias de movimentos simples e de posi��es de capturas s�o listadas na
 * ordem em que elas ocorreram. Caso exista captura, ambas as sequ�ncias ter�o o
 * mesmo comprimento.
 * 
 * @author Thiago Mendes Vieira
 * 
 *         20/09/2014
 */
public class Jogada implements Cloneable {
	/**
	 * Sequ�ncia de movimentos definida para esta jogada.
	 */
	private final List<MovimentoSimples> movimentos = new ArrayList<MovimentoSimples>();

	/**
	 * Sequ�ncia de posi��es de capturas definida para esta jogada.
	 */
	private final List<PosicaoTabuleiro> pecasCapturadas = new ArrayList<PosicaoTabuleiro>();

	/**
	 * Obt�m o n�mero de movimentos simples desta jogada.
	 * 
	 * @return N�mero de movimentos.
	 */
	public int getNumMovimentos() {
		return movimentos.size();
	}

	/**
	 * Obt�m um movimento simples desta jogada.
	 * 
	 * @param index
	 *            �ndice do movimento simples na ordem em que ocorre, come�ando
	 *            em zero.
	 * @return Um movimento simples.
	 */
	public MovimentoSimples getMovimento(int index) {
		return this.movimentos.get(index);
	}

	/**
	 * Adiciona um movimento simples no final da lista de movimentos simples
	 * desta jogada.
	 * 
	 * @param movimentoSimples
	 *            Movimento simples para ser adicionado.
	 */
	public void adicionarMovimentoSimples(MovimentoSimples movimentoSimples) {
		// Assegura que jogadas com mais de um movimentos dever�o sempre ter
		// capturas.
		assert (this.movimentos.size() == 0 ? true : movimentoSimples
				.getPosicaoCaptura() != null
				&& this.pecasCapturadas.size() == this.movimentos.size());

		// Assegura que os movimentos simples est�o interligados de forma que a
		// posi��o inicial do movimento prestes a ser adicionado seja igual �
		// posi��o final do �ltimo movimento da lista.
		assert (this.movimentos.size() == 0 ? true : movimentoSimples
				.getPosicaoOrigem().equals(
						this.movimentos.get(this.movimentos.size() - 1)
								.getPosicaoDestino()));

		this.movimentos.add(movimentoSimples);
		if (movimentoSimples.getPosicaoCaptura() != null) {
			this.pecasCapturadas.add(movimentoSimples.getPosicaoCaptura());
		}
	}

	/**
	 * Obt�m a lista de posi��es de pedras capturadas por esta jogada.
	 * 
	 * @return Lista de {@link PosicaoTabuleiro}.
	 */
	public List<PosicaoTabuleiro> getPedrasCapturadas() {
		return this.pecasCapturadas;
	}

	/**
	 * Obt�m a posi��o inicial desta jogada.
	 * 
	 * @return {@link PosicaoTabuleiro}.
	 */
	public PosicaoTabuleiro getPosicaoInicial() {
		if (movimentos.size() == 0) {
			return null;
		}
		return movimentos.get(0).getPosicaoOrigem();
	}

	/**
	 * Obt�m a posi��o final desta jogada.
	 * 
	 * @return {@link PosicaoTabuleiro}.
	 */
	public PosicaoTabuleiro getPosicaoFinal() {
		if (movimentos.size() == 0) {
			return null;
		}
		return movimentos.get(movimentos.size() - 1).getPosicaoDestino();
	}

	/**
	 * Verifica se a jogada captura alguma pedra.
	 * 
	 * @return "true" se a jogada captura alguma pedra, e "false" caso
	 *         contr�rio.
	 */
	public boolean isMovimentoComCaptura() {
		return (this.pecasCapturadas.size() > 0);
	}

	@Override
	public Object clone() {
		Jogada jogada = new Jogada();
		jogada.movimentos.addAll(this.movimentos);
		jogada.pecasCapturadas.addAll(this.pecasCapturadas);
		return jogada;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Jogada) {
			Jogada jogada = (Jogada) obj;

			if ((jogada.movimentos.size() != this.movimentos.size())
					|| (jogada.pecasCapturadas.size() != this.pecasCapturadas
							.size())) {
				return false;
			}

			for (int I = 0; I < this.movimentos.size(); I++) {
				if (!this.movimentos.get(I).equals(jogada.movimentos.get(I))) {
					return false;
				}
			}
			for (int I = 0; I < this.pecasCapturadas.size(); I++) {
				if (!this.pecasCapturadas.get(I).equals(
						jogada.pecasCapturadas.get(I))) {
					return false;
				}
			}

			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Imprimindo jogada:\n");
		sb.append("Origem: ");
		for (MovimentoSimples ms : movimentos) {
			sb.append(ms.getPosicaoOrigem().toString() + " > ");
		}
		sb.append(getPosicaoFinal().toString());
		sb.append(" Fim\n");
		sb.append("Capturas: ");
		for (PosicaoTabuleiro pt : pecasCapturadas) {
			sb.append(pt.toString() + " > ");
		}
		sb.append(" Fim\n\n");
		return sb.toString();
	}

}
