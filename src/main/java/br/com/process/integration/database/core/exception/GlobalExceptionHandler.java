package br.com.process.integration.database.core.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(CheckedException.class)
	public ResponseEntity<ErrorResponse> handleCustomCheckedException(CheckedException ccex) {
		ErrorResponse errorResponse = new ErrorResponse(ccex.getMessage(), ccex.getCustomMessage(), HttpStatus.BAD_REQUEST);
	    if (LOGGER.isErrorEnabled()) {
	        LOGGER.error(String.format("Failed code: '%s'", HttpStatus.BAD_REQUEST), ccex);
	    }
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
	
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException( DataIntegrityViolationException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse("Integrity constraint violation: " + ex.getMostSpecificCause().getMessage(), HttpStatus.CONFLICT);
        if (LOGGER.isErrorEnabled()) {
	    	LOGGER.error(String.format("Failed code: '%s'", HttpStatus.CONFLICT), ex);
	    }
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
	
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException ex) {
		ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
	    if (LOGGER.isErrorEnabled()) {
	        LOGGER.error(String.format("Failed code: '%s'", HttpStatus.NOT_FOUND), ex);
	    }
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
    	ErrorResponse errorResponse = new ErrorResponse("Ocorreu um erro desconhecido", HttpStatus.INTERNAL_SERVER_ERROR);
    	if (LOGGER.isErrorEnabled()) {
    		LOGGER.error(String.format("Failed code: '%s'", HttpStatus.INTERNAL_SERVER_ERROR), ex);
    	}
    	return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
