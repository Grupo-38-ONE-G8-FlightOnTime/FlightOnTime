package br.com.flightontime.service;

import br.com.flightontime.dto.StatsResponse;
import br.com.flightontime.repository.PredictionRepository;
import org.springframework.stereotype.Service;

@Service
public class StatsService {

    private static final String PREVISAO_ATRASADO = "Atrasado";

    private final PredictionRepository predictionRepository;

    public StatsService(PredictionRepository predictionRepository) {
        this.predictionRepository = predictionRepository;
    }

    public StatsResponse getStats() {
        long total = predictionRepository.count();
        long atrasados = predictionRepository.countByPrevisao(PREVISAO_ATRASADO);
        long pontuais = total - atrasados;
        double percentual = total == 0 ? 0.0 : (double) atrasados / (double) total;
        return new StatsResponse(total, atrasados, pontuais, percentual);
    }
}
