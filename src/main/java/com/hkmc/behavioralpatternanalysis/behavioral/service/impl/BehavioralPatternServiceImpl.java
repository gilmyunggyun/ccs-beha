package com.hkmc.behavioralpatternanalysis.behavioral.service.impl;

import com.google.gson.Gson;
import com.hkmc.behavioralpatternanalysis.behavioral.model.*;
import com.hkmc.behavioralpatternanalysis.behavioral.service.BehavioralPatternService;
import com.hkmc.behavioralpatternanalysis.common.Const;
import com.hkmc.behavioralpatternanalysis.common.client.DspServerBLClient;
import com.hkmc.behavioralpatternanalysis.common.client.DspServerGCClient;
import com.hkmc.behavioralpatternanalysis.common.client.DspServerUVOClient;
import com.hkmc.behavioralpatternanalysis.common.code.SpaResponseCodeEnum;
import com.hkmc.behavioralpatternanalysis.common.exception.GlobalExternalException;
import com.hkmc.behavioralpatternanalysis.common.model.InsuranceEnum;
import com.hkmc.behavioralpatternanalysis.common.model.SpaResponseDTO;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@RefreshScope
public class BehavioralPatternServiceImpl implements BehavioralPatternService {

    private final DspServerUVOClient dspServerUVOClient;
    private final DspServerGCClient dspServerGCClient;
    private final DspServerBLClient dspServerBLClient;
    private final Environment env;

    @Override
    public UbiSafetyResDTO ubiSafetyDrivingScore(UbiSafetyVO ubiSafetyVO) {
        final String serviceNo = ubiSafetyVO.getUbiSafetyReq().getServiceNo();
        final String uri = env.getProperty(Const.Key.DSP_COMMON_URI);
        final Map<String, String> requestHeader = ubiSafetyVO.getHeader();

        try {
            requestHeader.put(Const.Header.Authorization, env.getProperty(Const.Key.DSP_HEADER_AUTH));

            ResponseEntity<Map<String, Object>> feignResponse = ResponseEntity.noContent().build();

            if (Const.APP_TYPE_BLUE_LINK.equals(ubiSafetyVO.getUbiSafetyReq().getAppType())) {
                feignResponse = dspServerBLClient.requestCallGet(requestHeader, uri, ubiSafetyVO.getVinPath());
            } else if (Const.APP_TYPE_UVO.equals(ubiSafetyVO.getUbiSafetyReq().getAppType())) {
                feignResponse = dspServerUVOClient.requestCallGet(requestHeader, uri, ubiSafetyVO.getVinPath());
            } else if (Const.APP_TYPE_GENESIS_CONNECTED.equals(ubiSafetyVO.getUbiSafetyReq().getAppType())) {
                feignResponse = dspServerGCClient.requestCallGet(requestHeader, uri, ubiSafetyVO.getVinPath());
            }

            UbiSafetyResDTO ubiSafetyRes = UbiSafetyResDTO.builder()
                    .ServiceNo(serviceNo)
                    .RetCode(SpaResponseCodeEnum.SUCCESS.getRetCode())
                    .resCode(SpaResponseCodeEnum.SUCCESS.getResCode())
                    .build();

            if (ObjectUtils.isNotEmpty(feignResponse.getBody())) {
                ubiSafetyRes.setSafetyDrivingScore(
                        Integer.parseInt(StringUtils.defaultString(String.valueOf(feignResponse.getBody().get(Const.Key.SAFETY_DRV_SCORE)), "0")));
                ubiSafetyRes.setInsuranceDiscountYN(
                        StringUtils.defaultString(String.valueOf(feignResponse.getBody().get(Const.Key.INS_DISCOUNT_YN))));
                ubiSafetyRes.setUpdatedAt(
                        StringUtils.defaultString(String.valueOf(feignResponse.getBody().get(Const.Key.SCORE_DATE))));
                ubiSafetyRes.setDrvDistance(
                        Integer.parseInt(StringUtils.defaultString(String.valueOf(feignResponse.getBody().get(Const.Key.RANGE_DRV_DIST)), "0")));
                ubiSafetyRes.setAccelGrade(
                        StringUtils.defaultString(String.valueOf(feignResponse.getBody().get(Const.Key.BRST_ACC_GRADE))));
                ubiSafetyRes.setDecelGrade(
                        StringUtils.defaultString(String.valueOf(feignResponse.getBody().get(Const.Key.BRST_DEC_GRADE))));
                ubiSafetyRes.setNightDrivingGrade(
                        StringUtils.defaultString(String.valueOf(feignResponse.getBody().get(Const.Key.NIGHT_DRV_GRADE))));

                //20220620 UBI 안전 운전습관 보험할인가능 여부기준변경 임시조치(류민우 책임 요청_09월 원복필요)
                // drvDistance >=500 km, safetyDrivingScore >= 70 or drvDistance >=1000km,safetyDrivingScore >= 60
                if(ubiSafetyRes.getSafetyDrivingScore() >= 70 && ubiSafetyRes.getDrvDistance() >= 500){
                    ubiSafetyRes.setInsuranceDiscountYN("Y");
                }else if(ubiSafetyRes.getSafetyDrivingScore() >= 60 && ubiSafetyRes.getDrvDistance() >= 1000){
                    ubiSafetyRes.setInsuranceDiscountYN("Y");
                }else{
                    ubiSafetyRes.setInsuranceDiscountYN("N");
                }


                // 보험사별 할인 여부 정보 추가
                List<Map<String, String>> insurancesYNList = new ArrayList<>();

                for (InsuranceEnum insuranceEnum : InsuranceEnum.values()) {
                    if (ubiSafetyRes.getSafetyDrivingScore() >= insuranceEnum.getDrivingScore()
                            && ubiSafetyRes.getDrvDistance() >= insuranceEnum.getDrvDistance()) {
                        insurancesYNList.add(Map.of(insuranceEnum.getCName(), "Y"));
                    } else {
                        insurancesYNList.add(Map.of(insuranceEnum.getCName(), "N"));
                    }
                }

                ubiSafetyRes.setInsuranceDiscountYNmap(insurancesYNList);
            } else {
                // 값 없을 시 정상 출력, 단 로그에 남기기
                log.info("\n++++++++++[itlCarBreakpadDrvScoreSearch] | CALL : {} | STATUS : {} | VIN : {} | AUTH : {} | {}",
                        uri, HttpStatus.OK, ubiSafetyVO.getVinPath(), requestHeader.get(Const.Header.Authorization), SpaResponseCodeEnum.ERROR_DS01.getMessage());
            }

            return ubiSafetyRes;

        } catch (FeignException e) {
            log.error("\n++++++++++[FeignException] [itlCarBreakpadDrvScoreSearch] | CALL : {} | STATUS : {} | VIN : {} | AUTH : {} | {}",
                    uri, e.status(), ubiSafetyVO.getVinPath(), requestHeader.get(Const.Header.Authorization), SpaResponseCodeEnum.ERROR_DS01.getMessage());

            SpaResponseCodeEnum errResponse = SpaResponseCodeEnum.ERROR_EX01;

            /*
            // 추후 추가 예정, Exception 상세화
            if (StringUtils.isNotEmpty(e.contentUTF8())) {
                Map<String, Object> exceptionBody = new Gson().fromJson(e.contentUTF8(), new TypeToken<Map<String, Object>>() {}.getType());

                if (exceptionBody.containsKey(Const.Key.ERR_CODE_MAP)) {
                    if (StringUtils.equals(Const.ErrMsg.TYPE_4002, String.valueOf(exceptionBody.get(Const.Key.ERR_CODE_MAP))) ||
                            StringUtils.equals(Const.ErrMsg.TYPE_4010, String.valueOf(exceptionBody.get(Const.Key.ERR_CODE_MAP))) ||
                            StringUtils.equals(Const.ErrMsg.TYPE_5001, String.valueOf(exceptionBody.get(Const.Key.ERR_CODE_MAP))) ||
                            StringUtils.equals(Const.ErrMsg.TYPE_4122, String.valueOf(exceptionBody.get(Const.Key.ERR_CODE_MAP)))) {
                        log.error("\n++++++++++[FeignException] [itlCarBreakpadDrvScoreSearch] | CALL : {} | STATUS : {} | VIN : {} | AUTH : {} | {}",
                                uri, e.status(), ubiSafetyVO.getVinPath(), requestHeader.get(Const.Header.Authorization), SpaResponseCodeEnum.ERROR_DS02.getMessage());
                    }
                }
            }*/

            throw new GlobalExternalException(HttpStatus.OK.value(),
                    new Gson().toJson(SpaResponseDTO.builder()
                            .ServiceNo(serviceNo)
                            .RetCode(errResponse.getRetCode())
                            .resCode(errResponse.getResCode())
                            .build())
            );
        } catch (Exception e) {
            log.error("\n++++++++++[Exception] [itlCarBreakpadDrvScoreSearch] | VIN : {} | {}", ubiSafetyVO.getVinPath(),"한글");
            throw new GlobalExternalException(HttpStatus.OK.value(),
                    new Gson().toJson(SpaResponseDTO.builder()
                            .ServiceNo(serviceNo)
                            .RetCode(SpaResponseCodeEnum.ERROR_EX01.getRetCode())
                            .resCode(SpaResponseCodeEnum.ERROR_EX01.getResCode())
                            .build())
            );
        }
    }

}
