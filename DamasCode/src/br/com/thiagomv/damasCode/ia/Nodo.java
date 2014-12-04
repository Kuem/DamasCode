package br.com.thiagomv.damasCode.ia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.thiagomv.damasCode.estruturas.EstadoJogo;
import br.com.thiagomv.damasCode.estruturas.Jogada;

/**
 * Esta classe define um nodo usado nos algoritmos Minimax e AlfaBeta durante a
 * construção de suas árvores de decisão.
 * 
 * @author Thiago Mendes Vieira
 * 
 *         11/10/2014
 */
public class Nodo {
	private final EstadoJogo estadoJogo;
	private final List<Jogada> jogadasPermitidas;
	private final Map<Nodo, Jogada> estadosSucessores = new HashMap<>();
	private final boolean terminal;
	private double valorNodo;

	/**
	 * Utilizado para saber até que profundidade se pode alcançar a partir deste
	 * nodo.
	 */
	private int nivel = 0;

	public Nodo(EstadoJogo estadoJogo, List<Jogada> jogadasPermitidas,
			boolean isTerminal) {
		this.estadoJogo = estadoJogo;
		this.jogadasPermitidas = jogadasPermitidas;
		this.terminal = (jogadasPermitidas == null
				|| jogadasPermitidas.size() == 0 || isTerminal);
		this.valorNodo = 0;
	}

	public EstadoJogo getEstadoJogo() {
		return this.estadoJogo;
	}

	public double getUtilidade() {
		return Utils.avaliarEstadoJogo_equacao1(estadoJogo);
	}

	public boolean isTerminal() {
		return terminal;
	}

	public List<Jogada> getJogadasPermitidas() {
		return this.jogadasPermitidas;
	}

	public void addSucessor(Nodo sucessor, Jogada jogada) {
		this.estadosSucessores.put(sucessor, jogada);
	}

	public void setValor(double valor) {
		this.valorNodo = valor;
	}

	public double getValor() {
		return this.valorNodo;
	}

	public void setNivelTerminal(int nivel) {
		this.nivel = nivel;
	}

	public int getNivelTerminal() {
		return this.nivel;
	}

	/**
	 * Encontra a jogada sucessora na árvore de decisão cujo nodo sucessor tenha
	 * o valor passado por parâmetro.
	 * 
	 * @param v
	 * @return
	 */
	public Jogada getJogadaSucessora(double v) {
		List<Jogada> listaOpcoes = new ArrayList<>();

		for (Nodo nodo : this.estadosSucessores.keySet()) {
			if (nodo.getValor() == v) {
				listaOpcoes.add(this.estadosSucessores.get(nodo));
			}
		}

		int index = Utils.gerarIntAleatorio(0, listaOpcoes.size() - 1);

		Jogada jogada = listaOpcoes.get(index);
		listaOpcoes.clear();
		return jogada;
	}

	/**
	 * Encontra a jogada sucessora na árvore de decisão cujo nodo sucessor tenha
	 * o valor passado por parâmetro. Caso mais de uma jogada seja encontrada
	 * com o mesmo valor, a escolha será a jogada mais longa, ou seja, a que
	 * possuir maior nível terminal.
	 * 
	 * @param v
	 * @return
	 */
	public Jogada getJogadaSucessoraAvaliandoMaiorNivelTerminal(double v) {
		int maiorNivel = 0;

		List<Jogada> listaOpcoes = new ArrayList<>();

		for (Nodo nodo : this.estadosSucessores.keySet()) {
			if (nodo.getValor() == v) {
				listaOpcoes.add(this.estadosSucessores.get(nodo));
				maiorNivel = Math.max(maiorNivel, nodo.getNivelTerminal());
			}
		}

		List<Jogada> listaParaRemover = new ArrayList<>();

		for (Nodo nodo : this.estadosSucessores.keySet()) {
			if (nodo.getValor() == v) {
				if (nodo.getNivelTerminal() < maiorNivel) {
					listaParaRemover.add(this.estadosSucessores.get(nodo));
				}
			}
		}

		listaOpcoes.removeAll(listaParaRemover);

		int index = Utils.gerarIntAleatorio(0, listaOpcoes.size() - 1);

		Jogada jogada = listaOpcoes.get(index);
		listaOpcoes.clear();
		return jogada;
	}
}
