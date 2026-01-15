package br.com.flightontime.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StatsResponse(
        long total,
        long atrasados,
        long pontuais,
        @JsonProperty("percentual_atraso") double percentualAtraso
) {
}
