package br.com.thiagomv.damasCode.controle;

/**
 * Representa o tipo de exce��o lan�ada por {@link GameLogic}.
 * 
 * @author Thiago Mendes Vieira
 * 
 *         20/09/2014
 */
public class GameLogicException extends Exception {
	/**
	 * Vers�o 1.0.
	 */
	private static final long serialVersionUID = 1L;

	public GameLogicException(String message) {
		super(message);
	}

	public GameLogicException(Exception e) {
		super(e);
	}
}
