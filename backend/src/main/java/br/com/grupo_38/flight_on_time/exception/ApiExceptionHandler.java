package br.com.grupo_38.flight_on_time.exception;

import br.com.grupo_38.flight_on_time.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(UpstreamServiceException.class)
    public ResponseEntity<ErrorResponse> handleUpstreamServiceException(UpstreamServiceException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
