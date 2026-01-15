package br.com.flightontime.controller;

import br.com.flightontime.dto.StatsResponse;
import br.com.flightontime.service.StatsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/stats")
    public StatsResponse stats() {
        return statsService.getStats();
    }
}
