package com.hkmc.behavioralpatternanalysis.safetyscoremanagement.service.impl;

import com.google.gson.Gson;
import com.hkmc.behavioralpatternanalysis.common.Const;
import com.hkmc.behavioralpatternanalysis.common.client.InterfaceBluelinkClient;
import com.hkmc.behavioralpatternanalysis.common.client.InterfaceGenesisConnectedClient;
import com.hkmc.behavioralpatternanalysis.common.client.InterfaceUVOClient;
import com.hkmc.behavioralpatternanalysis.common.code.SpaResponseCodeEnum;
import com.hkmc.behavioralpatternanalysis.common.exception.GlobalExternalException;
import com.hkmc.behavioralpatternanalysis.common.model.SpaResponseDTO;
import com.hkmc.behavioralpatternanalysis.common.util.JsonUtil;
import com.hkmc.behavioralpatternanalysis.safetyscoremanagement.model.DrivingScoreVO;
import com.hkmc.behavioralpatternanalysis.safetyscoremanagement.model.DrivingScoreResDTO;
import com.hkmc.behavioralpatternanalysis.safetyscoremanagement.service.SafetyScoreManagementService;
import feign.FeignException;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
    public DrivingScoreResDTO ubiSafetyDrivingScoreRequest(DrivingScoreVO drivingScoreVO) throws GlobalExternalException {
        final String serviceNo = drivingScoreVO.getDrivingScoreReqDTO().getServiceNo();
        final String uri = env.getProperty(Const.Key.DSP_COMMON_URI);
        final Map<String, String> header = drivingScoreVO.getHeader();
        header.put(Const.Header.Authorization, env.getProperty(Const.Key.DSP_HEADER_AUTH));
        try {
            final String vinPath = drivingScoreVO.getVinPath();

            ResponseEntity<Map<String, Object>> response;
            switch (drivingScoreVO.getDrivingScoreReqDTO().getAppType()) {
                case Const.AppType.BLUE_LINK:
                    response = interfaceBluelinkClient.requestBluCallGet(header, uri, vinPath);
                    break;
                case Const.AppType.UVO:
                    response = interfaceUVOClient.requestUvoCallGet(header, uri, vinPath);
                    break;
                case Const.AppType.GENESIS_CONNECTED:
                    response = interfaceGenesisConnectedClient.requestGenCallGet(header, uri, vinPath);
                    break;
                default:
                    throw new Exception();
            }

            final Map<String, Object> body = this.npThenEmptyMap(response.getBody());

            return DrivingScoreResDTO.builder()
                    .ServiceNo(serviceNo)
                    .RetCode(SpaResponseCodeEnum.SUCCESS.getRetCode())
                    .resCode(SpaResponseCodeEnum.SUCCESS.getResCode())
                    .safetyDrivingScore(this.npThenZeroInteger(body, Const.ClientKey.SAFETY_DRV_SCORE))
                    .insuranceDiscountYN(this.npThenEmptyString(body, Const.ClientKey.INS_DISCOUNT_YN))
                    .updateAt(this.npThenEmptyString(body, Const.ClientKey.SCORE_DATE))
                    .drvDistance(this.npThenZeroInteger(body, Const.ClientKey.RANGE_DRV_DIST))
                    .accelGrade(this.npThenEmptyString(body, Const.ClientKey.BRST_ACC_GRADE))
                    .decelGrade(this.npThenEmptyString(body, Const.ClientKey.BRST_DEC_GRADE))
                    .nightDrivingGrade(this.npThenEmptyString(body, Const.ClientKey.NIGHT_DRV_GRADE))
                    .build();
        } catch (FeignException e) {
            log.error("\n++++++++++[FeignException] [itlCarBreakpadDrvScoreSearch] | CALL : {} | STATUS : {} | VIN : {} | AUTH : {} | {}",
                    uri, e.status(), drivingScoreVO.getVinPath(), header.get(Const.Header.Authorization), e.getMessage());

            final Map<String, Object> body = this.npThenEmptyMap(JsonUtil.str2map(e.contentUTF8()));
            if (this.npThenEmptyString(body, Const.Key.ERR_CODE_MAP).equals(Const.ErrMsg.CANNOT_FOUND_VIN)){
                throw new GlobalExternalException(
                        HttpStatus.OK.value(),
                        new Gson().toJson(SpaResponseDTO.builder()
                                .ServiceNo(serviceNo)
                                .RetCode(SpaResponseCodeEnum.ERROR_E110.getRetCode())
                                .resCode(SpaResponseCodeEnum.ERROR_E110.getResCode())
                                .build()
                        )
                );
            }
            throw GlobalExternalException_EX01(serviceNo);
        } catch (Exception e) {
            log.error("\n++++++++++[Exception] [itlCarBreakpadDrvScoreSearch] | VIN : {} | {}",
                    drivingScoreVO.getVinPath(), e.getMessage());
            throw GlobalExternalException_EX01(serviceNo);
        }
    }

    private GlobalExternalException GlobalExternalException_EX01(final String serviceNo) {
        return new GlobalExternalException(
                HttpStatus.OK.value(),
                new Gson().toJson(SpaResponseDTO.builder()
                        .ServiceNo(serviceNo)
                        .RetCode(SpaResponseCodeEnum.ERROR_EX01.getRetCode())
                        .resCode(SpaResponseCodeEnum.ERROR_EX01.getResCode())
                        .build())
        );
    }

    public Map<String, Object> npThenEmptyMap(final Map<String, Object> body) {
        return Optional.ofNullable(body).orElse(new HashMap<>());
    }

    public String npThenEmptyString(final Map<String, Object> body, final String keyName) {
        return String.valueOf(
                Optional.ofNullable(body.get(keyName)).orElse(StringUtil.EMPTY_STRING)
        );
    }

    public Integer npThenZeroInteger(final Map<String, Object> body, final String keyName) {
        return Integer.parseInt(
                String.valueOf(Optional.ofNullable(body.get(keyName)).orElse(BigInteger.ZERO.intValue()))
        );
    }
}
