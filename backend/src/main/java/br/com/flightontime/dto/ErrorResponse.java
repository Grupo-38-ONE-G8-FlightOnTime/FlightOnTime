package br.com.flightontime.dto;

import java.util.List;

public record ErrorResponse(String error, String message, List<String> details) {
}


