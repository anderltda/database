package br.com.process.integration.database.core.exception;

public class ServiceException extends BaseException {

	private static final long serialVersionUID = 1L;

	public ServiceException() {
		super();
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(String message, String... params) {
		super(message, params);
	}

	public ServiceException(int id, String message) {
		super(id, message);
	}

	public ServiceException(String codigo, String message) {
		super(codigo, message);
	}

	public ServiceException(Throwable t) {
		super(t);
	}

	public ServiceException(String message, Throwable t) {
		super(message, t);
	}

	public ServiceException(int id, Throwable t) {
		super(id, t);
	}

	public ServiceException(int id, String message, Throwable t) {
		super(id, message, t);
	}

	public ServiceException(String codigo, String message, Throwable t) {
		super(codigo, message, t);
	}

	public ServiceException(int id, String codigo, String message, Throwable t) {
		super(id, codigo, message, t);
	}

}