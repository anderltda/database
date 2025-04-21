package br.com.process.integration.database.core.exception;

/**
 * Exceção Verificada (Checked Exception) 
 * 
 * Criar uma exceção que precisa ser
 * declarada ou tratada explicitamente (checked exception), estenda a classe
 * Exception:
 */
public class CheckedException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private final String customMessage;

	public CheckedException(String message) {
		super(message);
		this.customMessage = "";
	}

	public CheckedException(String message, String customMessage, Throwable cause) {
		super(message, cause);
		this.customMessage = customMessage;
	}
	
	public CheckedException(String message, Throwable cause) {
		super(message, cause);
		this.customMessage = "";
	}

	public String getCustomMessage() {
		return customMessage;
	}
	
}
