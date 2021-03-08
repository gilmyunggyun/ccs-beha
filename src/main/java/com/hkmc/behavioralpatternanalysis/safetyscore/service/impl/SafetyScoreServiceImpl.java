package com.hkmc.behavioralpatternanalysis.safetyscore.service.impl;

import com.google.gson.Gson;
import com.hkmc.behavioralpatternanalysis.common.Const;
import com.hkmc.behavioralpatternanalysis.common.client.InterfaceBluelinkDspClient;
import com.hkmc.behavioralpatternanalysis.common.client.InterfaceGenesisConnectedDspClient;
import com.hkmc.behavioralpatternanalysis.common.client.InterfaceUvoDspClient;
import com.hkmc.behavioralpatternanalysis.common.code.SpaResponseCodeEnum;
import com.hkmc.behavioralpatternanalysis.common.exception.GlobalExternalException;
import com.hkmc.behavioralpatternanalysis.common.model.SpaResponseDTO;
import com.hkmc.behavioralpatternanalysis.common.util.JsonUtil;
import com.hkmc.behavioralpatternanalysis.safetyscore.model.DrivingScoreVO;
import com.hkmc.behavioralpatternanalysis.safetyscore.model.DrivingScoreResDTO;
import com.hkmc.behavioralpatternanalysis.safetyscore.service.SafetyScoreService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SafetyScoreServiceImpl implements SafetyScoreService {
    private final InterfaceUvoDspClient interfaceUvoDspClient;
    private final InterfaceGenesisConnectedDspClient interfaceGenesisConnectedDspClient;
    private final InterfaceBluelinkDspClient interfaceBluelinkDspClient;
    private final Environment env;

    @Override
    public DrivingScoreResDTO ubiSafetyDrivingScoreRequest(DrivingScoreVO drivingScoreVO) throws GlobalExternalException {
        final String serviceNo = drivingScoreVO.getDrivingScoreReqDTO().getServiceNo();
        final String uri = env.getProperty(Const.Key.DSP_COMMON_URI);
        final Map<String, String> requestHeader = drivingScoreVO.getHeader();
        try {
            requestHeader.put(Const.Header.Authorization, env.getProperty(Const.Key.DSP_HEADER_AUTH));
            final String vinPath = drivingScoreVO.getVinPath();
            String appType = drivingScoreVO.getDrivingScoreReqDTO().getAppType();

            ResponseEntity<Map<String, Object>> feignResponse = ResponseEntity.noContent().build();
            if (Const.APP_TYPE_BLUE_LINK.equals(appType)) {
                feignResponse = interfaceBluelinkDspClient.requestCallGet(requestHeader, uri, vinPath);
            } else if (Const.APP_TYPE_UVO.equals(appType)) {
                feignResponse = interfaceUvoDspClient.requestCallGet(requestHeader, uri, vinPath);
            } else if (Const.APP_TYPE_GENESIS_CONNECTED.equals(appType)) {
                feignResponse = interfaceGenesisConnectedDspClient.requestCallGet(requestHeader, uri, vinPath);
            }

            final Map<String, Object> feignResponseSuccessBody = feignResponse.getBody();
            return ObjectUtils.isEmpty(feignResponseSuccessBody)
                    ? DrivingScoreResDTO.builder()
                            .ServiceNo(serviceNo)
                            .RetCode(SpaResponseCodeEnum.SUCCESS.getRetCode())
                            .resCode(SpaResponseCodeEnum.SUCCESS.getResCode())
                            .build()
                    : new DrivingScoreResDTO(feignResponseSuccessBody, serviceNo);

        } catch (FeignException e) {
            log.error("\n++++++++++[FeignException] [itlCarBreakpadDrvScoreSearch] | CALL : {} | STATUS : {} | VIN : {} | AUTH : {} | {}",
                    uri, e.status(), drivingScoreVO.getVinPath(), requestHeader.get(Const.Header.Authorization), e.getMessage());

            final Map<String, Object> feignResponseExceptionBody = Optional.ofNullable(JsonUtil.str2map(e.contentUTF8())).orElse(new HashMap<>());
            final SpaResponseCodeEnum errResponse = Optional.ofNullable(feignResponseExceptionBody.get(Const.Key.ERR_CODE_MAP)).orElse(StringUtils.EMPTY)
                    .equals(Const.ErrMsg.CANNOT_FOUND_VIN)
                    ? SpaResponseCodeEnum.ERROR_E110
                    : SpaResponseCodeEnum.ERROR_EX01;
            throw new GlobalExternalException(
                    HttpStatus.OK.value(),
                    new Gson().toJson(SpaResponseDTO.builder()
                            .ServiceNo(serviceNo)
                            .RetCode(errResponse.getRetCode())
                            .resCode(errResponse.getResCode())
                            .build()
                    )
            );
        } catch (Exception e) {
            log.error("\n++++++++++[Exception] [itlCarBreakpadDrvScoreSearch] | VIN : {} | {}",
                    drivingScoreVO.getVinPath(), e.getMessage());
            throw new GlobalExternalException(
                    HttpStatus.OK.value(),
                    new Gson().toJson(SpaResponseDTO.builder()
                            .ServiceNo(serviceNo)
                            .RetCode(SpaResponseCodeEnum.ERROR_EX01.getRetCode())
                            .resCode(SpaResponseCodeEnum.ERROR_EX01.getResCode())
                            .build())
            );
        }
    }
}
