package br.com.flightontime.controller;

import br.com.flightontime.dto.PredictResponse;
import br.com.flightontime.service.PredictService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PredictController.class)
class PredictControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PredictService predictService;

    @Test
    void predictReturnsOk() throws Exception {
        PredictResponse response = new PredictResponse("Pontual", 0.78);
        when(predictService.predict(any())).thenReturn(response);

        String body = "{" +
                "\"companhia\": \"AZ\"," +
                "\"origem\": \"GIG\"," +
                "\"destino\": \"GRU\"," +
                "\"data_partida\": \"2025-11-10T14:30:00\"," +
                "\"distancia_km\": 350" +
                "}";

        mockMvc.perform(post("/predict")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.previsao").value("Pontual"))
                .andExpect(jsonPath("$.probabilidade").value(0.78));
    }

    @Test
    void predictValidationError() throws Exception {
        mockMvc.perform(post("/predict")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
