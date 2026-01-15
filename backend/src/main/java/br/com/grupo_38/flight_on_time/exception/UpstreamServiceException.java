package br.com.grupo_38.flight_on_time.exception;

public class UpstreamServiceException extends RuntimeException {
    
    public UpstreamServiceException(String message) {
        super(message);
    }

    public UpstreamServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
