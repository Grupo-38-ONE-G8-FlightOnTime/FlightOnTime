package br.com.flightontime.repository;

import br.com.flightontime.model.PredictionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PredictionRepository extends JpaRepository<PredictionRecord, Long> {
    long countByPrevisao(String previsao);
}
