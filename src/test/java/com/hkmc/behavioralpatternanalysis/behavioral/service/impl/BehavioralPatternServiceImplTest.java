package com.hkmc.behavioralpatternanalysis.behavioral.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkmc.behavioralpatternanalysis.behavioral.model.*;
import com.hkmc.behavioralpatternanalysis.common.client.DspServerBLClient;
import com.hkmc.behavioralpatternanalysis.common.client.DspServerGCClient;
import com.hkmc.behavioralpatternanalysis.common.client.DspServerUVOClient;
import com.hkmc.behavioralpatternanalysis.common.code.SpaResponseCodeEnum;
import com.hkmc.behavioralpatternanalysis.common.exception.GlobalExternalException;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class BehavioralPatternServiceImplTest {

    @Spy
    @InjectMocks
    private BehavioralPatternServiceImpl behavioralPatternService;

    @Mock
    private Environment env;
    @Mock
    private DspServerUVOClient dspServerUVOClient;
    @Mock
    private DspServerGCClient dspServerGCClient;
    @Mock
    private DspServerBLClient dspServerBLClient;

    private Map<String, String> header;
    private String vin;
    private String nadid;
    private String ccid;
    private String carid;

    @BeforeEach
    public void setup() {
        vin = "KMOSTEST020052701";
        nadid = "01229168761";
        ccid = "43318BC2-6DE2-4F87-8396-4DFDBAB66B9A_BLU";
        carid = "411e9a8a-f48f-4c82-8b9b-ef3e96ea1b06";

        header = new HashMap<>() {
            {
                put("vin", vin);
                put("nadid", nadid);
                put("from", "PHONE");
                put("xtid", "87d058d2-3971-4fa2-8d63-625e12a945ee");
            }
        };
    }

    @ParameterizedTest
    @ValueSource(strings = {"BLU", "UVO", "GEN", ""})
    @DisplayName("안전운전 점수 조회")
    void testUbiSafetyDrivingScore(String arg) throws Exception {
        UbiSafetyReqDTO ubiSafetyReq = UbiSafetyReqDTO.builder()
                .ServiceNo("A26")
                .CCID("43318BC2-6DE2-4F87-8396-4DFDBAB66B9A_" + arg)
                .carID(carid)
                .mtsNo(nadid)
                .build();

        when(env.getProperty(anyString())).thenReturn("/dsppath").thenReturn("header-auth");

        if (StringUtils.equals(arg, "BLU")) {
            when(dspServerBLClient.requestCallGet(anyMap(), anyString(), any())).thenReturn(ResponseEntity.ok().body(getDspResData()));
        } else if (StringUtils.equals(arg, "UVO")) {
            when(dspServerUVOClient.requestCallGet(anyMap(), anyString(), any())).thenReturn(ResponseEntity.ok().body(getDspResData()));
        } else if (StringUtils.equals(arg, "GEN")) {
            when(dspServerGCClient.requestCallGet(anyMap(), anyString(), any())).thenReturn(ResponseEntity.ok().body(getDspResData()));
        } else {
            ubiSafetyReq.setCCID(ubiSafetyReq.getCCID() + "BLU");
            when(dspServerBLClient.requestCallGet(anyMap(), anyString(), any())).thenReturn(ResponseEntity.ok().body(null));
        }

        UbiSafetyResDTO ubiSafetyRes = behavioralPatternService.ubiSafetyDrivingScore(UbiSafetyVO.builder()
                .header(header)
                .vinPath(vin)
                .ubiSafetyReq(ubiSafetyReq)
                .build());

        assertEquals(ubiSafetyRes.getRetCode(), SpaResponseCodeEnum.SUCCESS.getRetCode());
        assertEquals(ubiSafetyRes.getResCode(), SpaResponseCodeEnum.SUCCESS.getResCode());
    }

    @Test
    @DisplayName("안전운전 점수 조회 오류")
    void testUbiSafetyDrivingScoreException() {
        UbiSafetyReqDTO ubiSafetyReq = UbiSafetyReqDTO.builder()
                .ServiceNo("A26")
                .CCID(ccid)
                .carID(carid)
                .mtsNo(nadid)
                .build();

        HashMap<String, Collection<String>> feignHeader = new HashMap<>();
        feignHeader.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList(MediaType.APPLICATION_JSON_VALUE));

        Request mockedFeignRequest = Request.create(
                Request.HttpMethod.GET, "foo", feignHeader, null, StandardCharsets.UTF_8, new RequestTemplate()
        );

        when(env.getProperty(anyString())).thenReturn("/dsppath").thenReturn("header-auth");
        when(dspServerBLClient.requestCallGet(anyMap(), anyString(), any()))
                .thenThrow(FeignException.errorStatus("requestCallGet",
                        Response.builder()
                                .status(500)
                                .body("{\"errCode\":\"5003\"}".getBytes(StandardCharsets.UTF_8))
                                .reason("Initial server error")
                                .request(mockedFeignRequest)
                                .headers(feignHeader)
                                .build()));

        GlobalExternalException exception = assertThrows(GlobalExternalException.class, () ->
                behavioralPatternService.ubiSafetyDrivingScore(UbiSafetyVO.builder()
                        .header(header)
                        .vinPath(vin)
                        .ubiSafetyReq(ubiSafetyReq)
                        .build())
        );

        assertTrue(StringUtils.contains(exception.getBody(), SpaResponseCodeEnum.ERROR_EX01.getResCode()));

        ubiSafetyReq.setCCID("");
        exception = assertThrows(GlobalExternalException.class, () ->
                behavioralPatternService.ubiSafetyDrivingScore(UbiSafetyVO.builder()
                        .header(header)
                        .vinPath(vin)
                        .ubiSafetyReq(ubiSafetyReq)
                        .build())
        );

        assertTrue(StringUtils.contains(exception.getBody(), SpaResponseCodeEnum.ERROR_EX01.getResCode()));

    }

    private Map<String, Object> getDspResData() throws JsonProcessingException {

        String data = "{\"safety_drv_score\":9,\"ins_discount_yn\":\"N\",\"score_date\":\"20210510\",\"range_drv_dist\":487," +
                "\"brst_acc_grade\":\"WARNING\",\"brst_dec_grade\":\"WARNING\",\"night_drv_grade\":\"EXCELLENCE\"}";

        return new ObjectMapper().readValue(data, new TypeReference<>() {});
    }

}