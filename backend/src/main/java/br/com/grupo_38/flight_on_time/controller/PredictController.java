package br.com.grupo_38.flight_on_time.controller;

import br.com.grupo_38.flight_on_time.dto.PredictRequest;
import br.com.grupo_38.flight_on_time.dto.PredictResponse;
import br.com.grupo_38.flight_on_time.service.PredictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/predict")
public class PredictController {

    @Autowired
    private PredictService predictService;

    @PostMapping
    public ResponseEntity<PredictResponse> predict(@RequestBody PredictRequest request) {
        PredictResponse response = predictService.predict(request);
        return ResponseEntity.ok(response);
    }
}
