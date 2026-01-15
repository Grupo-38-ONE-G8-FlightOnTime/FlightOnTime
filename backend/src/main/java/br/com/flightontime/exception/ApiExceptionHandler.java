package br.com.flightontime.exception;

import br.com.flightontime.dto.ErrorResponse;
import java.util.List;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<String> details = ex.getBindingResult().getFieldErrors().stream()
                .map(ApiExceptionHandler::formatFieldError)
                .toList();
        ErrorResponse body = new ErrorResponse("VALIDATION_ERROR", "Invalid request body.", details);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMalformedJson(HttpMessageNotReadableException ex) {
        ErrorResponse body = new ErrorResponse(
                "INVALID_JSON",
                "Malformed JSON or invalid date format.",
                List.of()
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(UpstreamServiceException.class)
    public ResponseEntity<ErrorResponse> handleUpstream(UpstreamServiceException ex) {
        List<String> details = (ex.getDetails() == null || ex.getDetails().isBlank())
                ? List.of()
                : List.of(ex.getDetails());
        ErrorResponse body = new ErrorResponse("UPSTREAM_ERROR", ex.getMessage(), details);
        return ResponseEntity.status(Objects.requireNonNull(ex.getStatus(), "status")).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
        ErrorResponse body = new ErrorResponse("INTERNAL_ERROR", "Unexpected error.", List.of());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private static String formatFieldError(FieldError error) {
        return error.getField() + ": " + error.getDefaultMessage();
    }
}


