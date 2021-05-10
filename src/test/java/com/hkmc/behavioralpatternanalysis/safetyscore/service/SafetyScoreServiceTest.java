//package com.hkmc.behavioralpatternanalysis.safetyscore.service;
//
//import com.google.gson.Gson;
//import com.hkmc.behavioralpatternanalysis.common.Const;
//import com.hkmc.behavioralpatternanalysis.common.client.InterfaceBluelinkDspClient;
//import com.hkmc.behavioralpatternanalysis.common.client.InterfaceGenesisConnectedDspClient;
//import com.hkmc.behavioralpatternanalysis.common.client.InterfaceUvoDspClient;
//import com.hkmc.behavioralpatternanalysis.common.code.SpaResponseCodeEnum;
//import com.hkmc.behavioralpatternanalysis.common.exception.GlobalExternalException;
//import com.hkmc.behavioralpatternanalysis.common.util.JsonUtil;
//import com.hkmc.behavioralpatternanalysis.safetyscore.model.*;
//import com.hkmc.behavioralpatternanalysis.safetyscore.service.impl.SafetyScoreServiceImpl;
//import feign.*;
//import io.netty.util.internal.StringUtil;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.core.env.Environment;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.util.ObjectUtils;
//
//import java.math.BigInteger;
//import java.nio.charset.StandardCharsets;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//@ActiveProfiles("local")
//@TestPropertySource("classpath:application.yaml")
//class SafetyScoreServiceTest {
//    @Mock
//    private InterfaceUvoDspClient interfaceUvoDspClient;
//    @Mock
//    private InterfaceBluelinkDspClient interfaceBluelinkDspClient;
//    @Mock
//    private InterfaceGenesisConnectedDspClient interfaceGenesisConnectedDspClient;
//    @Mock
//    private Environment env;
//
//    @Spy
//    @InjectMocks
//    private SafetyScoreServiceImpl safetyScoreManagementService;
//
//    private String vin;
//
//    private DrivingScoreVO drivingScoreVO;
//    private DrivingScoreReqDTO drivingScoreReqDTO;
//
//    private String serviceNo;
//    private String carID;
//    private String mtsNo;
//
//    private String ccid;
//    private String uvoCCID;
//    private String bluCCID;
//    private String gneCCID;
//
//    final static private Gson gson = new Gson();
//
//    @BeforeEach
//    void afterAll() {
//        this.vin = "KMTG341ABJU014200";
//        this.ccid = "someCCID";
//        this.bluCCID = this.ccid + "_BLU";
//        this.uvoCCID = this.ccid + "_UVO";
//        this.gneCCID = this.ccid + "_GEN";
//
//        this.serviceNo = "A26";
//        this.carID = "someCarID";
//        this.mtsNo = "MTS";
//
//        this.drivingScoreReqDTO = DrivingScoreReqDTO.builder()
//                .ServiceNo(this.serviceNo)
//                .carID(this.carID)
//                .CCID(this.ccid)
//                .mtsNo(this.mtsNo)
//                .build();
//
//        this.drivingScoreVO = DrivingScoreVO.builder()
//                .vinPath(this.vin)
//                .header(new HashMap<>())
//                .drivingScoreReqDTO(this.drivingScoreReqDTO)
//                .build();
//        when(this.env.getProperty(eq(Const.Key.DSP_COMMON_URI))).thenReturn("/api/v1/dsp/ubi");
//    }
//
//    @Test
//    public void ubiSafetyDrivingScoreRequest_Success() {
//        when(this.env.getProperty(eq(Const.Key.DSP_HEADER_AUTH))).thenReturn("Basic ########");
//        Map<String, Object> dspResBody = new HashMap<>();
//        dspResBody.put(Const.Key.SAFETY_DRV_SCORE, 9);
//        dspResBody.put(Const.Key.INS_DISCOUNT_YN, "N");
//        dspResBody.put(Const.Key.SCORE_DATE, "20210122");
//        dspResBody.put(Const.Key.RANGE_DRV_DIST, 487);
//        dspResBody.put(Const.Key.BRST_ACC_GRADE, "WARNING");
//        dspResBody.put(Const.Key.BRST_DEC_GRADE, "WARNING");
//        dspResBody.put(Const.Key.NIGHT_DRV_GRADE, "EXCELLENCE");
//        ResponseEntity<Map<String, Object>> resSuccess = ResponseEntity.status(HttpStatus.OK).body(dspResBody);
//
//        // bluTest
//        assertDoesNotThrow(() -> {
//            when(this.interfaceBluelinkDspClient.requestCallGet(anyMap(), anyString(), anyString())).thenReturn(resSuccess);
//            this.drivingScoreReqDTO.setCCID(this.bluCCID);
//
//            DrivingScoreResDTO bluSuccess = this.safetyScoreManagementService.ubiSafetyDrivingScoreRequest(this.drivingScoreVO);
//
//            verify(this.interfaceBluelinkDspClient, times(1)).requestCallGet(anyMap(), anyString(), anyString());
//            assertResult(dspResBody, bluSuccess);
//        });
//
//        // uvoTest
//        assertDoesNotThrow(() -> {
//            when(this.interfaceUvoDspClient.requestCallGet(anyMap(), anyString(), anyString())).thenReturn(resSuccess);
//            this.drivingScoreReqDTO.setCCID(this.uvoCCID);
//
//            DrivingScoreResDTO uvoSuccess = this.safetyScoreManagementService.ubiSafetyDrivingScoreRequest(this.drivingScoreVO);
//
//            verify(this.interfaceUvoDspClient, times(1)).requestCallGet(anyMap(), anyString(), anyString());
//            assertResult(dspResBody, uvoSuccess);
//        });
//
//        // genTest
//        assertDoesNotThrow(() -> {
//            when(this.interfaceGenesisConnectedDspClient.requestCallGet(anyMap(), anyString(), anyString())).thenReturn(resSuccess);
//            this.drivingScoreReqDTO.setCCID(this.gneCCID);
//
//            DrivingScoreResDTO genSuccess = this.safetyScoreManagementService.ubiSafetyDrivingScoreRequest(this.drivingScoreVO);
//
//            verify(this.interfaceGenesisConnectedDspClient, times(1)).requestCallGet(anyMap(), anyString(), anyString());
//            assertResult(dspResBody, genSuccess);
//        });
//    }
//
//    private void assertResult(Map<String, Object> dspResBody, DrivingScoreResDTO resultDTO) {
//        if (resultDTO != null) {
//            if (!ObjectUtils.isEmpty(resultDTO)) {
//                assertEquals(this.drivingScoreReqDTO.getServiceNo(), resultDTO.getServiceNo());
//                assertEquals(dspResBody.get(Const.Key.SAFETY_DRV_SCORE), resultDTO.getSafetyDrivingScore());
//                assertEquals(dspResBody.get(Const.Key.INS_DISCOUNT_YN), resultDTO.getInsuranceDiscountYN());
//                assertEquals(dspResBody.get(Const.Key.SCORE_DATE), resultDTO.getUpdateAt());
//                assertEquals(dspResBody.get(Const.Key.RANGE_DRV_DIST), resultDTO.getDrvDistance());
//                assertEquals(dspResBody.get(Const.Key.BRST_ACC_GRADE), resultDTO.getAccelGrade());
//                assertEquals(dspResBody.get(Const.Key.BRST_DEC_GRADE), resultDTO.getDecelGrade());
//                assertEquals(dspResBody.get(Const.Key.NIGHT_DRV_GRADE), resultDTO.getNightDrivingGrade());
//            } else {
//                fail("drivingScoreResDTO is null");
//            }
//        } else {
//            fail("result is null");
//        }
//    }
//
//    @Test
//    public void ubiSafetyDrivingScoreRequest_npBody() {
//        when(this.env.getProperty(eq(Const.Key.DSP_HEADER_AUTH))).thenReturn("Basic ########");
//        ResponseEntity<Map<String, Object>> resSuccess = ResponseEntity.status(HttpStatus.OK).build();
//        when(this.interfaceBluelinkDspClient.requestCallGet(anyMap(), anyString(), anyString())).thenReturn(resSuccess);
//        this.drivingScoreReqDTO.setCCID(this.bluCCID);
//
//        assertDoesNotThrow(() -> {
//            DrivingScoreResDTO resultVO = this.safetyScoreManagementService.ubiSafetyDrivingScoreRequest(this.drivingScoreVO);
//
//            verify(this.interfaceBluelinkDspClient, times(1)).requestCallGet(anyMap(), anyString(), anyString());
//            if (!ObjectUtils.isEmpty(resultVO)) {
//                assertEquals(this.drivingScoreReqDTO.getServiceNo(), resultVO.getServiceNo());
//                assertEquals(BigInteger.ZERO.intValue(), resultVO.getSafetyDrivingScore());
//                assertEquals(StringUtil.EMPTY_STRING, resultVO.getInsuranceDiscountYN());
//                assertEquals(StringUtil.EMPTY_STRING, resultVO.getUpdateAt());
//                assertEquals(BigInteger.ZERO.intValue(), resultVO.getDrvDistance());
//                assertEquals(StringUtil.EMPTY_STRING, resultVO.getAccelGrade());
//                assertEquals(StringUtil.EMPTY_STRING, resultVO.getDecelGrade());
//                assertEquals(StringUtil.EMPTY_STRING, resultVO.getNightDrivingGrade());
//            } else {
//                fail("drivingScoreResDTO is null");
//            }
//        });
//    }
//
//    @Test
//    public void ubiSafetyDrivingScoreRequest_FeignException_5003() throws FeignException {
//        when(this.env.getProperty(eq(Const.Key.DSP_HEADER_AUTH))).thenReturn("Basic ########");
//
//        Map<String, Object> bodyErr5003 = new HashMap<>();
//        bodyErr5003.put("errId", "b8b7a9b5-1474-4646-b06e-6479d3f5c3a5");
//        bodyErr5003.put("errCode", "5003");
//        bodyErr5003.put("errMsg", "Cannot found vin");
//
//
//        HashMap<String, Collection<String>> headerMap = new HashMap<>();
//        headerMap.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList(MediaType.APPLICATION_JSON_VALUE));
//
//        Request mockedFeignRequest = Request.create(
//                Request.HttpMethod.GET, "foo", headerMap, null, StandardCharsets.UTF_8, new RequestTemplate()
//        );
//
//        Response mockedFeignResponse = Response.builder()
//                .status(500)
//                .body(gson.toJson(bodyErr5003).getBytes(StandardCharsets.UTF_8))
//                .reason("Initial server error")
//                .request(mockedFeignRequest)
//                .headers(headerMap)
//                .build();
//
//        when(this.interfaceBluelinkDspClient.requestCallGet(anyMap(), anyString(), anyString()))
//                .thenThrow(FeignException.errorStatus("requestBLCallGet", mockedFeignResponse));
//        this.drivingScoreReqDTO.setCCID(this.drivingScoreReqDTO.getCCID() + "_BLU");
//
//        GlobalExternalException globalExternalException = assertThrows(GlobalExternalException.class, () -> {
//            this.safetyScoreManagementService.ubiSafetyDrivingScoreRequest(this.drivingScoreVO);
//        });
//
//        verify(this.interfaceBluelinkDspClient, times(1)).requestCallGet(anyMap(), anyString(), anyString());
//
//        Map<String, Object> spaResponse = JsonUtil.str2map(globalExternalException.getBody());
//
//        assertNotEquals(new HashMap<>(), spaResponse);
//        assertEquals(HttpStatus.OK.value(), globalExternalException.getStatusCode());
//        assertEquals(SpaResponseCodeEnum.ERROR_E110.getResCode(), spaResponse.get(Const.Key.RES_CODE_MAP));
//        assertEquals(SpaResponseCodeEnum.ERROR_E110.getRetCode(), spaResponse.get(Const.Key.RET_CODE_MAP));
//    }
//
//    @Test
//    public void ubiSafetyDrivingScoreRequest_FeignException_EX01() throws FeignException {
//        when(this.env.getProperty(eq(Const.Key.DSP_HEADER_AUTH))).thenReturn("Basic ########");
//        Map<String, Object> bodyErr5003 = new HashMap<>();
//        bodyErr5003.put("errId", "a8a00000-eb68-4925-8242-fd6497e5c68f");
//        bodyErr5003.put("errCode", "5001");
//        bodyErr5003.put("errMsg", "Error Internal Server");
//
//        when(this.interfaceBluelinkDspClient.requestCallGet(anyMap(), anyString(), anyString()))
//                .thenThrow(FeignException.class);
//
//        this.drivingScoreReqDTO.setCCID(this.drivingScoreReqDTO.getCCID() + "_BLU");
//        GlobalExternalException globalExternalException = assertThrows(GlobalExternalException.class, () -> {
//            this.safetyScoreManagementService.ubiSafetyDrivingScoreRequest(this.drivingScoreVO);
//        });
//
//        verify(this.interfaceBluelinkDspClient, times(1)).requestCallGet(anyMap(), anyString(), anyString());
//
//        Map<String, Object> spaResponse = JsonUtil.str2map(globalExternalException.getBody());
//        if (spaResponse != null) {
//            assertEquals(HttpStatus.OK.value(), globalExternalException.getStatusCode());
//            assertEquals(SpaResponseCodeEnum.ERROR_EX01.getResCode(), spaResponse.get(Const.Key.RES_CODE_MAP));
//            assertEquals(SpaResponseCodeEnum.ERROR_EX01.getRetCode(), spaResponse.get(Const.Key.RET_CODE_MAP));
//        } else {
//            fail("result is null");
//        }
//    }
//
//    @Test
//    public void ubiSafetyDrivingScoreRequest_Exception_EX01() throws FeignException {
//        when(env.getProperty(eq(Const.Key.DSP_HEADER_AUTH))).thenThrow(IllegalArgumentException.class);
//        GlobalExternalException globalExternalException = assertThrows(GlobalExternalException.class, () -> {
//            this.safetyScoreManagementService.ubiSafetyDrivingScoreRequest(this.drivingScoreVO);
//        });
//
//        assertEquals(HttpStatus.OK.value(), globalExternalException.getStatusCode());
//        Map<String, Object> spaResponse = JsonUtil.str2map(globalExternalException.getBody());
//        if (spaResponse != null) {
//            assertEquals(SpaResponseCodeEnum.ERROR_EX01.getResCode(), spaResponse.get(Const.Key.RES_CODE_MAP));
//            assertEquals(SpaResponseCodeEnum.ERROR_EX01.getRetCode(), spaResponse.get(Const.Key.RET_CODE_MAP));
//        } else {
//            fail("body is null");
//        }
//    }
//}