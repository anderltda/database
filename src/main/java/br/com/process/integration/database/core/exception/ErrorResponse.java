package br.com.process.integration.database.core.exception;

public class ErrorResponse {
	private int status;
	private String message;
	private String customMessage;

	public ErrorResponse() {}
	
	public ErrorResponse(String message, int status) {
		this.message = message;
		this.status = status;
	}

	public ErrorResponse(String message, String customMessage, int status) {
		this.message = message;
		this.customMessage = customMessage;
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCustomMessage() {
		return customMessage;
	}

	public void setCustomMessage(String customMessage) {
		this.customMessage = customMessage;
	}

}