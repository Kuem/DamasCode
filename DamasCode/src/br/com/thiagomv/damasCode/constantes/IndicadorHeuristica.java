package br.com.thiagomv.damasCode.constantes;

/**
 * Indica o tipo de heur�stica que cada tipo de algoritmo implementa para os
 * jogadores. Quando o jogador tem o tipo de algoritmo igual a HUMANO este
 * indicador � desconsiderado. A implementa��o de uma heur�stica geralmente ser�
 * diferente para cada tipo de algoritmo. Cabe ao programador definir a
 * implementa���o de cada {@link IndicadorHeuristica} para cada
 * {@link IndicadorAlgoritmo} escolhido.
 * 
 * @author Thiago Mendes Vieira
 * 
 *         20/09/2014
 */
public enum IndicadorHeuristica {
	H1_P3("H1_P3", 3), H1_P4("H1_P4", 4), H1_P5("H1_P5", 5), H1_P6("H1_P6", 6);

	/**
	 * Label para exibi��o na interface gr�fica.
	 */
	private final String label;
	private final int flag;

	private IndicadorHeuristica(String label, int flag) {
		this.label = label;
		this.flag = flag;
	}

	public String getLabel() {
		return this.label;
	}

	public int getFlag() {
		return this.flag;
	}
}
