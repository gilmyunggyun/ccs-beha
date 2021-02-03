package com.hkmc.behavioralpatternanalysis.controller;

import com.hkmc.behavioralpatternanalysis.common.Const;
import com.hkmc.behavioralpatternanalysis.common.client.InterfaceBluelinkClient;
import com.hkmc.behavioralpatternanalysis.common.client.InterfaceGenesisConnectedClient;
import com.hkmc.behavioralpatternanalysis.common.client.InterfaceUVOClient;
import com.hkmc.behavioralpatternanalysis.common.client.MTGatewayClient;
import com.hkmc.behavioralpatternanalysis.common.util.JsonUtil;
import com.hkmc.behavioralpatternanalysis.safetyscoremanagement.model.DrivingScoreReqDTO;
import com.hkmc.behavioralpatternanalysis.safetyscoremanagement.model.DrivingScoreVO;
import com.hkmc.behavioralpatternanalysis.safetyscoremanagement.service.SafetyScoreManagementService;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
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
        R2dbcAutoConfiguration.class
})
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
class BehavioralPatternAnalysisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SafetyScoreManagementService safetyScoreManagementService;

    @Mock
    private InterfaceBluelinkClient interfaceBluelinkClient;
    @Mock
    private InterfaceGenesisConnectedClient genesisConnectedClient;
    @Mock
    private InterfaceUVOClient interfaceUVOClient;

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

        when(safetyScoreManagementService.ubiSafetyDrivingScoreRequest(any()))
                .thenReturn(DrivingScoreVO.builder().build());

        MvcResult response = this.mockMvc.perform(
                post(Const.BehavioralPatternAnalysis.VERSION_V1 + "/ubi/score/{vinpath}", this.vinPath)
                        .headers(this.httpHeaders)
                        .content(JsonUtil.obj2str(this.drivingScoreReqDTO))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print()).andReturn();

        assertEquals(HttpStatus.OK.value(), response.getResponse().getStatus());
    }

    @Test
    public void getUbiSafetyDrivingScoreTest_500_bodynull() throws Exception {
        when(safetyScoreManagementService.ubiSafetyDrivingScoreRequest(any()))
                .thenReturn(null);

        MvcResult response = this.mockMvc.perform(
                post(Const.BehavioralPatternAnalysis.VERSION_V1 + "/ubi/score/{vinpath}", this.vinPath)
                        .headers(this.httpHeaders)
                        .content(JsonUtil.obj2str(this.drivingScoreReqDTO))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print()).andReturn();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getResponse().getStatus());
    }
}
