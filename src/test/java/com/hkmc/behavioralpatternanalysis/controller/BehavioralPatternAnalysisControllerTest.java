package com.hkmc.behavioralpatternanalysis.controller;

import com.google.gson.Gson;
import com.hkmc.behavioralpatternanalysis.behavioral.model.UbiSafetyReqDTO;
import com.hkmc.behavioralpatternanalysis.behavioral.service.BehavioralPatternService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@EmbeddedKafka
@DirtiesContext
@ActiveProfiles("local")
@AutoConfigureMockMvc
@Slf4j
class BehavioralPatternAnalysisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BehavioralPatternService behavioralPatternService;

    private String vin;
    private HttpHeaders httpHeaders;

    @BeforeEach
    void before() {
        vin = "KMOSTEST020052701";

        httpHeaders = new HttpHeaders() {
            {
                add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            }
        };
    }

    @Test
    @DisplayName("지능형 가혹운전 점수 조회")
    void testItlCarBreakpadScore() throws Exception {
        mockMvc.perform(get("/behavioralpatternanalysis/v1/breakpad/{vinPath}", vin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(httpHeaders))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("안전운전 점수 조회")
    void testUbiSafetyDrivingScore() throws Exception {
        UbiSafetyReqDTO ubiSafetyReq = UbiSafetyReqDTO.builder()
                .ServiceNo("A26")
                .CCID("43318BC2-6DE2-4F87-8396-4DFDBAB66B9A_BLU")
                .carID("411e9a8a-f48f-4c82-8b9b-ef3e96ea1b06")
                .mtsNo("01229168761")
                .build();

        mockMvc.perform(post("/behavioralpatternanalysis/v1/ubiscore/{vinPath}", vin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(httpHeaders)
                        .content(new Gson().toJson(ubiSafetyReq)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

}
