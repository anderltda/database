package br.com.process.integration.database.core.ui.generator;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class FaviconExceptionHandler {

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleFaviconMissing(NoResourceFoundException ex) {
        if (ex.getResourcePath().endsWith("favicon.ico")) {
            return;
        }
    }
}
