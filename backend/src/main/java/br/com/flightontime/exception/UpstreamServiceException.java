package br.com.flightontime.exception;

import org.springframework.http.HttpStatus;

public class UpstreamServiceException extends RuntimeException {

    private final HttpStatus status;
    private final String details;

    public UpstreamServiceException(HttpStatus status, String message, String details, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.details = details;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getDetails() {
        return details;
    }
}


