package br.com.thiagomv.damasCode.controle;

/**
 * Representa o tipo de exce��o lan�ada por {@link UIDamasController}.
 * 
 * @author Thiago Mendes Vieira
 * 
 *         20/09/2014
 */
public class UIDamasControllerException extends Exception {
	/**
	 * Vers�o 1.0.
	 */
	private static final long serialVersionUID = 0100L;

	public UIDamasControllerException(String message) {
		super(message);
	}

	public UIDamasControllerException(Exception e) {
		super(e);
	}
}
