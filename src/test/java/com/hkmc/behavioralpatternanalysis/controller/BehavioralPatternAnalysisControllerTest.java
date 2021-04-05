package com.hkmc.behavioralpatternanalysis.controller;

import com.hkmc.behavioralpatternanalysis.common.Const;
import com.hkmc.behavioralpatternanalysis.common.client.InterfaceBluelinkDspClient;
import com.hkmc.behavioralpatternanalysis.common.client.InterfaceGenesisConnectedDspClient;
import com.hkmc.behavioralpatternanalysis.common.client.InterfaceUvoDspClient;
import com.hkmc.behavioralpatternanalysis.common.util.JsonUtil;
import com.hkmc.behavioralpatternanalysis.safetyscore.model.DrivingScoreReqDTO;
import com.hkmc.behavioralpatternanalysis.safetyscore.model.DrivingScoreResDTO;
import com.hkmc.behavioralpatternanalysis.safetyscore.service.SafetyScoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        R2dbcAutoConfiguration.class,
        MongoAutoConfiguration.class
})
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
class BehavioralPatternAnalysisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SafetyScoreService safetyScoreService;

    @Mock
    private InterfaceBluelinkDspClient interfaceBluelinkDspClient;
    @Mock
    private InterfaceGenesisConnectedDspClient genesisConnectedClient;
    @Mock
    private InterfaceUvoDspClient interfaceUvoDspClient;

    private String vinPath;

    private HttpHeaders httpHeaders;
    private DrivingScoreReqDTO drivingScoreReqDTO;

    @BeforeEach
    void before() {
        this.vinPath = "someVinPath";
        this.httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        this.drivingScoreReqDTO = new DrivingScoreReqDTO();
    }

    @Test
    public void getUbiSafetyDrivingScoreTest_200() throws Exception {
        this.drivingScoreReqDTO.setMtsNo("someMtsNo");
        this.drivingScoreReqDTO.setServiceNo("A26");
        this.drivingScoreReqDTO.setCCID("someCCID");
        this.drivingScoreReqDTO.setCarID("someCarId");
        this.drivingScoreReqDTO.setFromHost(Const.System.PHONE);

        when(safetyScoreService.ubiSafetyDrivingScoreRequest(any()))
                .thenReturn(DrivingScoreResDTO.builder().build());

        MvcResult response = this.mockMvc.perform(
                post(Const.BehavioralPatternAnalysis.VERSION_V1 + "/ubiscore/{vinpath}", this.vinPath)
                        .headers(this.httpHeaders)
                        .content(JsonUtil.obj2str(this.drivingScoreReqDTO))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print()).andReturn();

        assertEquals(HttpStatus.OK.value(), response.getResponse().getStatus());
    }

    @Test
    public void getUbiSafetyDrivingScoreTest_500_bodynull() throws Exception {
        when(safetyScoreService.ubiSafetyDrivingScoreRequest(any()))
                .thenReturn(null);

        MvcResult response = this.mockMvc.perform(
                post(Const.BehavioralPatternAnalysis.VERSION_V1 + "/ubiscore/{vinpath}", this.vinPath)
                        .headers(this.httpHeaders)
                        .content(JsonUtil.obj2str(this.drivingScoreReqDTO))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print()).andReturn();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getResponse().getStatus());
    }
}
