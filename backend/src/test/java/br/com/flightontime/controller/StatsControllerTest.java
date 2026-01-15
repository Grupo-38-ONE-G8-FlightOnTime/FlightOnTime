package br.com.flightontime.controller;

import br.com.flightontime.dto.StatsResponse;
import br.com.flightontime.service.StatsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatsController.class)
class StatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatsService statsService;

    @Test
    void statsReturnsOk() throws Exception {
        when(statsService.getStats()).thenReturn(new StatsResponse(10, 3, 7, 0.3));

        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(10))
                .andExpect(jsonPath("$.atrasados").value(3))
                .andExpect(jsonPath("$.pontuais").value(7))
                .andExpect(jsonPath("$.percentual_atraso").value(0.3));
    }
}
