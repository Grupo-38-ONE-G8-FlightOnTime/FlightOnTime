package br.com.flightontime.service;

import br.com.flightontime.dto.PredictRequest;
import br.com.flightontime.dto.PredictResponse;
import br.com.flightontime.exception.UpstreamServiceException;
import br.com.flightontime.model.PredictionRecord;
import br.com.flightontime.repository.PredictionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Service
public class PredictService {

    private final RestTemplate restTemplate;
    private final String predictPath;
    private final PredictionRepository predictionRepository;
    private final ObjectMapper objectMapper;

    public PredictService(
            @Value("${datascience.base-url}") String baseUrl,
            @Value("${datascience.predict-path:/predict}") String predictPath,
            PredictionRepository predictionRepository,
            ObjectMapper objectMapper,
            RestTemplateBuilder restTemplateBuilder
    ) {
        String safeBaseUrl = Objects.requireNonNull(baseUrl, "baseUrl");
        this.restTemplate = restTemplateBuilder.rootUri(safeBaseUrl).build();
        this.predictPath = Objects.requireNonNull(predictPath, "predictPath");
        this.predictionRepository = Objects.requireNonNull(predictionRepository, "predictionRepository");
        this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper");
    }

    public PredictResponse predict(PredictRequest request) {
        try {
            String payload = toPayload(request);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(payload, headers);

            ResponseEntity<PredictResponse> responseEntity = restTemplate.postForEntity(
                    predictPath,
                    entity,
                    PredictResponse.class
            );

            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                throw new UpstreamServiceException(
                        HttpStatus.BAD_GATEWAY,
                        "Data science service returned an error.",
                        "Status: " + responseEntity.getStatusCode(),
                        null
                );
            }

            PredictResponse response = responseEntity.getBody();
            if (response == null) {
                throw new UpstreamServiceException(
                        HttpStatus.BAD_GATEWAY,
                        "Data science service returned an empty response.",
                        null,
                        null
                );
            }

            predictionRepository.save(toRecord(request, response));
            return response;
        } catch (RestClientResponseException ex) {
            throw new UpstreamServiceException(
                    HttpStatus.BAD_GATEWAY,
                    "Data science service returned an error.",
                    ex.getResponseBodyAsString(),
                    ex
            );
        } catch (RestClientException ex) {
            throw new UpstreamServiceException(
                    HttpStatus.BAD_GATEWAY,
                    "Failed to reach data science service.",
                    null,
                    ex
            );
        }
    }

    private String toPayload(PredictRequest request) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("companhia", request.companhia());
        payload.put("origem", request.origem());
        payload.put("destino", request.destino());
        payload.put("data_partida", request.dataPartida());
        payload.put("distancia_km", request.distanciaKm());

        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Failed to serialize prediction payload.", ex);
        }
    }

    private PredictionRecord toRecord(PredictRequest request, PredictResponse response) {
        PredictionRecord record = new PredictionRecord();
        record.setCreatedAt(LocalDateTime.now());
        record.setPrevisao(response.previsao());
        record.setProbabilidade(response.probabilidade());
        record.setCompanhia(request.companhia());
        record.setOrigem(request.origem());
        record.setDestino(request.destino());
        record.setDataPartida(request.dataPartida());
        record.setDistanciaKm(request.distanciaKm());
        return record;
    }
}
