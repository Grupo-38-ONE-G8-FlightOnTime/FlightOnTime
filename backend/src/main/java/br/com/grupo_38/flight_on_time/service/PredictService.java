package br.com.grupo_38.flight_on_time.service;

import br.com.grupo_38.flight_on_time.dto.PredictRequest;
import br.com.grupo_38.flight_on_time.dto.PredictResponse;
import org.springframework.stereotype.Service;

@Service
public class PredictService {

    public PredictResponse predict(PredictRequest request) {
        // Simple prediction logic
        // In a real scenario, this would call the ML model
        boolean onTime = request.getDistance() < 1000;
        double confidence = onTime ? 0.85 : 0.75;
        String message = onTime ? "Flight is likely to be on time" : "Flight may be delayed";
        
        return new PredictResponse(onTime, confidence, message);
    }
}
