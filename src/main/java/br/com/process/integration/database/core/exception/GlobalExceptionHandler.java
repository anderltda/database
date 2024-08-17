package br.com.process.integration.database.core.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(CheckedException.class)
	public ResponseEntity<ErrorResponse> handleCustomCheckedException(CheckedException ccex) {
		ErrorResponse errorResponse = new ErrorResponse(ccex.getMessage(), ccex.getCustomMessage(), HttpStatus.BAD_REQUEST.value());
	    if (LOGGER.isErrorEnabled()) {
	        LOGGER.error(String.format("Failed code: '%s'", HttpStatus.BAD_REQUEST), ccex);
	    }
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
		ErrorResponse errorResponse = new ErrorResponse("Ocorreu um erro desconhecido", HttpStatus.INTERNAL_SERVER_ERROR.value());
	    if (LOGGER.isErrorEnabled()) {
	    	LOGGER.error( String.format("Failed code: '%s'", HttpStatus.INTERNAL_SERVER_ERROR), ex);
	    }
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
