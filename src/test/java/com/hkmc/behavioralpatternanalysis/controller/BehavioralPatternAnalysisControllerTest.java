package com.hkmc.behavioralpatternanalysis.controller;

import ccs.core.db.repository.postgre.GenericPostgreRepository;
import ccs.core.db.repository.redis.GenericRedisRepository;
import com.google.gson.Gson;
import com.hkmc.behavioralpatternanalysis.behavioral.model.BehaSvdvHist;
import com.hkmc.behavioralpatternanalysis.behavioral.model.CarTmuBasicInfo;
import com.hkmc.behavioralpatternanalysis.behavioral.model.UbiSafetyReqDTO;
import com.hkmc.behavioralpatternanalysis.behavioral.service.BehavioralPatternService;
import com.hkmc.behavioralpatternanalysis.behavioral.service.impl.BehavioralPatternServiceImpl;
import com.hkmc.behavioralpatternanalysis.common.Const;
import com.hkmc.behavioralpatternanalysis.common.client.DspServerBLClient;
import com.hkmc.behavioralpatternanalysis.common.client.DspServerGCClient;
import com.hkmc.behavioralpatternanalysis.common.client.DspServerUVOClient;
import com.hkmc.behavioralpatternanalysis.common.client.MSAServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
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
    private BehavioralPatternServiceImpl behavioralPatternService;

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
