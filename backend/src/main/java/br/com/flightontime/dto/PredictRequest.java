package br.com.flightontime.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record PredictRequest(
        @NotBlank @Size(min = 2, max = 3) String companhia,
        @NotBlank @Size(min = 3, max = 3) String origem,
        @NotBlank @Size(min = 3, max = 3) String destino,
        @NotNull @JsonProperty("data_partida") LocalDateTime dataPartida,
        @NotNull @Positive @JsonProperty("distancia_km") Double distanciaKm
) {
}


