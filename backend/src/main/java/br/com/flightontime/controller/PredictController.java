package br.com.flightontime.controller;

import br.com.flightontime.dto.PredictRequest;
import br.com.flightontime.dto.PredictResponse;
import br.com.flightontime.service.PredictService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PredictController {

    private final PredictService predictService;

    public PredictController(PredictService predictService) {
        this.predictService = predictService;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "ok");
    }

    @PostMapping("/predict")
    public PredictResponse predict(@Valid @RequestBody PredictRequest request) {
        return predictService.predict(request);
    }
}


