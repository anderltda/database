package br.com.process.integration.database.core.exception;

/**
 * Exceção Não Verificada (Unchecked Exception)
 * 
 * Criar uma exceção que não precisa ser declarada ou tratada
 * explicitamente (unchecked exception), estenda a classe RuntimeException:
 * 
 */
public class UncheckedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UncheckedException(String message) {
		super(message);
	}

	public UncheckedException(String message, Throwable cause) {
		super(message, cause);
	}
}
