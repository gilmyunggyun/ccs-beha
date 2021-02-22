package com.hkmc.behavioralpatternanalysis.safetyscoremanagement.service.impl;

import com.google.gson.Gson;
import com.hkmc.behavioralpatternanalysis.common.Const;
import com.hkmc.behavioralpatternanalysis.common.client.InterfaceBluelinkClient;
import com.hkmc.behavioralpatternanalysis.common.client.InterfaceGenesisConnectedClient;
import com.hkmc.behavioralpatternanalysis.common.client.InterfaceUVOClient;
import com.hkmc.behavioralpatternanalysis.common.code.BehavioralPatternAnalysisServiceEnum;
import com.hkmc.behavioralpatternanalysis.common.code.SpaResponseCodeEnum;
import com.hkmc.behavioralpatternanalysis.common.exception.GlobalExternalException;
import com.hkmc.behavioralpatternanalysis.common.model.SpaResponseDTO;
import com.hkmc.behavioralpatternanalysis.common.util.JsonUtil;
import com.hkmc.behavioralpatternanalysis.safetyscoremanagement.model.DrivingScoreReqVO;
import com.hkmc.behavioralpatternanalysis.safetyscoremanagement.model.DrivingScoreResDTO;
import com.hkmc.behavioralpatternanalysis.safetyscoremanagement.model.DrivingScoreVO;
import com.hkmc.behavioralpatternanalysis.safetyscoremanagement.model.DspReqDTO;
import com.hkmc.behavioralpatternanalysis.safetyscoremanagement.service.SafetyScoreManagementService;
import feign.FeignException;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class SafetyScoreManagementServiceImpl implements SafetyScoreManagementService {
    @Autowired
    private InterfaceUVOClient interfaceUVOClient;
    @Autowired
    private InterfaceGenesisConnectedClient interfaceGenesisConnectedClient;
    @Autowired
    private InterfaceBluelinkClient interfaceBluelinkClient;
    @Autowired
    private Environment env;

    @Override
    public DrivingScoreVO ubiSafetyDrivingScoreRequest(DrivingScoreReqVO drivingScoreReqVO) throws GlobalExternalException {
        String auth = env.getProperty("dsp.auth");
        drivingScoreReqVO.getHeader().put("Authorization", auth);
        String serviceNo = drivingScoreReqVO.getDrivingScoreReqDTO().getServiceNo();
        try {
            ResponseEntity<Map<String, Object>> response;
            switch (drivingScoreReqVO.getDrivingScoreReqDTO().getAppType()) {
                case Const.AppType.BLUE_LINK:
                    response = interfaceBluelinkClient.requestBluCallGet(
                            drivingScoreReqVO.getHeader(),
                            BehavioralPatternAnalysisServiceEnum.UBI_SCORE.getServiceUrl(),
                            drivingScoreReqVO.getVinPath()
                    );
                    break;
                case Const.AppType.UVO:
                    response = interfaceUVOClient.requestUvoCallGet(
                            drivingScoreReqVO.getHeader(),
                            BehavioralPatternAnalysisServiceEnum.UBI_SCORE.getServiceUrl(),
                            drivingScoreReqVO.getVinPath()
                    );
                    break;
                case Const.AppType.GENESIS_CONNECTED:
                    response = interfaceGenesisConnectedClient.requestGenCallGet(
                            drivingScoreReqVO.getHeader(),
                            BehavioralPatternAnalysisServiceEnum.UBI_SCORE.getServiceUrl(),
                            drivingScoreReqVO.getVinPath()
                    );
                    break;
                default:
                    throw new Exception();
            }

            Map<String, Object> body = Optional.ofNullable(response.getBody()).orElse(new HashMap<>());

            DrivingScoreResDTO drivingScoreResDTO = DrivingScoreResDTO.builder()
                    .ServiceNo(serviceNo)
                    .RetCode(SpaResponseCodeEnum.SUCCESS.getRetCode())
                    .resCode(SpaResponseCodeEnum.SUCCESS.getResCode())
                    .safetyDrivingScore(this.npThenZeroInteger(body.get(Const.ClientKey.SAFETY_DRV_SCORE)))
                    .insuranceDiscountYN(this.npThenEmptyString(body.get(Const.ClientKey.INS_DISCOUNT_YN)))
                    .updateAt(this.npThenEmptyString(body.get(Const.ClientKey.SCORE_DATE)))
                    .drvDistance(this.npThenZeroInteger(body.get(Const.ClientKey.RANGE_DRV_DIST)))
                    .accelGrade(this.npThenEmptyString(body.get(Const.ClientKey.BRST_ACC_GRADE)))
                    .decelGrade(this.npThenEmptyString(body.get(Const.ClientKey.BRST_DEC_GRADE)))
                    .nightDrivingGrade(this.npThenEmptyString(body.get(Const.ClientKey.NIGHT_DRV_GRADE)))
                    .build();

            return DrivingScoreVO.builder()
                    .status(HttpStatus.OK.value())
                    .drivingScoreResDTO(drivingScoreResDTO)
                    .build();
        } catch (FeignException e) {
            Map<String, Object> body = JsonUtil.str2map(e.contentUTF8());
            if (ObjectUtils.isNotEmpty(body)
                    && this.npThenEmptyString(body.get("errCode")).equals("5003")) {

                throw new GlobalExternalException(
                        HttpStatus.OK.value(),
                        new Gson().toJson(SpaResponseDTO.builder()
                                .ServiceNo(serviceNo)
                                .RetCode(SpaResponseCodeEnum.ERROR_E110.getRetCode())
                                .resCode(SpaResponseCodeEnum.ERROR_E110.getResCode())
                                .build())
                );
            }
            throw GlobalExternalException_EX01(serviceNo);
        } catch (Exception e) {
            throw GlobalExternalException_EX01(serviceNo);
        }
    }

    private GlobalExternalException GlobalExternalException_EX01(String serviceNo) {
        return new GlobalExternalException(
                HttpStatus.OK.value(),
                new Gson().toJson(SpaResponseDTO.builder()
                        .ServiceNo(serviceNo)
                        .RetCode(SpaResponseCodeEnum.ERROR_EX01.getRetCode())
                        .resCode(SpaResponseCodeEnum.ERROR_EX01.getResCode())
                        .build())
        );
    }

    public String npThenEmptyString(Object obj) {
        return String.valueOf(Optional.ofNullable(obj).orElse(StringUtil.EMPTY_STRING));
    }

    public Integer npThenZeroInteger(Object obj) {
        return Integer.parseInt(String.valueOf(Optional.ofNullable(obj).orElse(BigInteger.ZERO.intValue())));
    }
}
