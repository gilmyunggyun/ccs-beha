package com.hkmc.behavioralpatternanalysis.behavioral.service.impl;

import ccs.core.db.repository.postgre.GenericPostgreRepository;
import ccs.core.db.repository.redis.GenericRedisRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hkmc.behavioralpatternanalysis.behavioral.model.*;
import com.hkmc.behavioralpatternanalysis.behavioral.service.BehavioralPatternService;
import com.hkmc.behavioralpatternanalysis.common.Const;
import com.hkmc.behavioralpatternanalysis.common.client.DspServerBLClient;
import com.hkmc.behavioralpatternanalysis.common.client.DspServerGCClient;
import com.hkmc.behavioralpatternanalysis.common.client.DspServerUVOClient;
import com.hkmc.behavioralpatternanalysis.common.client.MSAServiceClient;
import com.hkmc.behavioralpatternanalysis.common.code.SpaResponseCodeEnum;
import com.hkmc.behavioralpatternanalysis.common.exception.GlobalExternalException;
import com.hkmc.behavioralpatternanalysis.common.exception.RestException;
import com.hkmc.behavioralpatternanalysis.common.model.SpaResponseDTO;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@RefreshScope
public class BehavioralPatternServiceImpl implements BehavioralPatternService {

    private final DspServerUVOClient dspServerUVOClient;
    private final DspServerGCClient dspServerGCClient;
    private final DspServerBLClient dspServerBLClient;
    private final MSAServiceClient msaServiceClient;
    private final Environment env;
    private final R2dbcEntityOperations postgresqlEntityOperations;
    private final R2dbcRepositoryFactory postgresqlRepositoryFactory;
    private final RedisTemplate<String, Object> redisTemplate;

    private GenericPostgreRepository<BehaSvdvHist, Integer> behaSvdvHistRepo;
    private GenericRedisRepository<CarTmuBasicInfo, String> carTmuBasicRepo;

    protected GenericPostgreRepository<BehaSvdvHist, Integer> behaSvdvHistRepository() {
        return new GenericPostgreRepository<>(BehaSvdvHist.class, postgresqlRepositoryFactory, postgresqlEntityOperations);
    }

    protected GenericRedisRepository<CarTmuBasicInfo, String> carTmuBasicRepository() {
        return new GenericRedisRepository<>(CarTmuBasicInfo.class, redisTemplate);
    }

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
                ubiSafetyRes.setUpdateAt(
                        StringUtils.defaultString(String.valueOf(feignResponse.getBody().get(Const.Key.SCORE_DATE))));
                ubiSafetyRes.setDrvDistance(
                        Integer.parseInt(StringUtils.defaultString(String.valueOf(feignResponse.getBody().get(Const.Key.RANGE_DRV_DIST)), "0")));
                ubiSafetyRes.setAccelGrade(
                        StringUtils.defaultString(String.valueOf(feignResponse.getBody().get(Const.Key.BRST_ACC_GRADE))));
                ubiSafetyRes.setDecelGrade(
                        StringUtils.defaultString(String.valueOf(feignResponse.getBody().get(Const.Key.BRST_DEC_GRADE))));
                ubiSafetyRes.setNightDrivingGrade(
                        StringUtils.defaultString(String.valueOf(feignResponse.getBody().get(Const.Key.NIGHT_DRV_GRADE))));
            }

            return ubiSafetyRes;

        } catch (FeignException e) {
            log.error("\n++++++++++[FeignException] [itlCarBreakpadDrvScoreSearch] | CALL : {} | STATUS : {} | VIN : {} | AUTH : {} | {}",
                    uri, e.status(), ubiSafetyVO.getVinPath(), requestHeader.get(Const.Header.Authorization), e.getMessage());

            SpaResponseCodeEnum errResponse = SpaResponseCodeEnum.ERROR_EX01;

            if (StringUtils.isNotEmpty(e.contentUTF8())) {
                Map<String, Object> exceptionBody = new Gson().fromJson(e.contentUTF8(), new TypeToken<Map<String, Object>>() {}.getType());

                if (StringUtils.equals(Const.ErrMsg.CANNOT_FOUND_VIN, String.valueOf(exceptionBody.get(Const.Key.ERR_CODE_MAP)))) {
                    errResponse = SpaResponseCodeEnum.ERROR_E110;
                }
            }

            throw new GlobalExternalException(HttpStatus.OK.value(),
                    new Gson().toJson(SpaResponseDTO.builder()
                            .ServiceNo(serviceNo)
                            .RetCode(errResponse.getRetCode())
                            .resCode(errResponse.getResCode())
                            .build())
            );
        } catch (Exception e) {
            log.error("\n++++++++++[Exception] [itlCarBreakpadDrvScoreSearch] | VIN : {} | {}", ubiSafetyVO.getVinPath(), e.getMessage());
            throw new GlobalExternalException(HttpStatus.OK.value(),
                    new Gson().toJson(SpaResponseDTO.builder()
                            .ServiceNo(serviceNo)
                            .RetCode(SpaResponseCodeEnum.ERROR_EX01.getRetCode())
                            .resCode(SpaResponseCodeEnum.ERROR_EX01.getResCode())
                            .build())
            );
        }
    }

    @Override
    public ItlBreakpadResDTO itlBreakpadDrvScore(String vinPath) {
        if(StringUtils.isEmpty(vinPath) || vinPath.length() > 17){
            throw new RestException(Const.ErrMsg.TYPE_450);
        }

        behaSvdvHistRepo = behaSvdvHistRepository();
        carTmuBasicRepo = carTmuBasicRepository();

        try {
            final CarTmuBasicInfo carTmuBasicInfo =
                    carTmuBasicRepo.findByIdHash(CarTmuBasicInfo.builder().vin(vinPath).build());

            if(ObjectUtils.isEmpty(carTmuBasicInfo) || StringUtils.isEmpty(carTmuBasicInfo.getCarOid())){
                return ItlBreakpadResDTO.builder().resultStatus(Const.RESULT_FAIL).vin(vinPath).build();
            }

            BehaSvdvHist behaSvdvHist = behaSvdvHistRepo.reactiveFindByCriteria(
                    Criteria.where(Const.Key.CRTN_YMD).is(LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern(Const.YYYYMMDD)))
                            .and(Const.Key.CAR_OID).is(carTmuBasicInfo.getCarOid())).block();

            if(ObjectUtils.isEmpty(behaSvdvHist) || StringUtils.isEmpty(behaSvdvHist.getIfDate())){
                return ItlBreakpadResDTO.builder().resultStatus(Const.RESULT_FAIL).vin(vinPath).build();
            }

            return ItlBreakpadResDTO.builder()
                    .vin(vinPath)
                    .ifDate(behaSvdvHist.getIfDate())
                    .severeNormal(behaSvdvHist.getSevereNormal())
                    .cntNormal(behaSvdvHist.getCntNormal())
                    .cntCaution(behaSvdvHist.getCntCaution())
                    .cntSevere(behaSvdvHist.getCntSevere())
                    .acumTrvgDist(getAcumTrvgDist(vinPath))
                    .resultStatus(Const.RESULT_SUCCESS)
                    .build();

        } catch (Exception e) {
            log.error("\n++++++++++[Exception] [itlBreakpadDrvScore] | {}(vin) | {}", vinPath, e.toString());
            throw new RestException(Const.ErrMsg.TYPE_595);
        }
    }

    private int getAcumTrvgDist(String vin) {
        Map<String, String> header = new HashMap<>() {
            {
                put(Const.Header.HOST, env.getProperty(Const.Key.SERVICE_DOMAIN_DRIVE_INFO));
            }
        };
        String path = StringUtils.defaultString(env.getProperty(Const.Key.SERVICE_PATH_ODOMETER));
        path = path.replace("{vin}", vin);

        try {
            ResponseEntity<Map<String, Object>> response = msaServiceClient.requestCallGet(header, path);

            if (ObjectUtils.isNotEmpty(response.getBody())) {
                if (ObjectUtils.isNotEmpty(response.getBody().get(Const.Key.ODOMETER_VALUE))) {
                    return Long.valueOf(String.valueOf(response.getBody().get(Const.Key.ODOMETER_VALUE))).intValue();
                }
            }
        } catch (FeignException e) {
            log.error("\n++++++++++[FeignException] [itlBreakpadDrvScore] ({}) - {} | {}(vin) | {}", e.status(), path, vin, e.toString());
        }

        return 0;
    }

}
