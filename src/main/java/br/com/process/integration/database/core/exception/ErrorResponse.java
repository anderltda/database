package br.com.process.integration.database.core.exception;

import org.springframework.http.HttpStatus;

public class ErrorResponse {
	
	private HttpStatus status;
	private String message;
	private String customMessage;

	public ErrorResponse() { }

	public ErrorResponse(String message, HttpStatus status) {
		this.message = message;
		this.status = status;
	}

	public ErrorResponse(String message, String customMessage, HttpStatus status) {
		this.message = message;
		this.customMessage = customMessage;
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
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