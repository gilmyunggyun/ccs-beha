package com.hkmc.behavioralpatternanalysis.behavioral.service.impl;

import ccs.core.db.repository.postgre.GenericPostgreRepository;
import ccs.core.db.repository.redis.GenericRedisRepository;
import com.google.gson.Gson;
import com.hkmc.behavioralpatternanalysis.behavioral.model.*;
import com.hkmc.behavioralpatternanalysis.behavioral.service.BehavioralPatternService;
import com.hkmc.behavioralpatternanalysis.common.Const;
import com.hkmc.behavioralpatternanalysis.common.client.InterfaceBluelinkDspClient;
import com.hkmc.behavioralpatternanalysis.common.client.InterfaceGenesisConnectedDspClient;
import com.hkmc.behavioralpatternanalysis.common.client.InterfaceUvoDspClient;
import com.hkmc.behavioralpatternanalysis.common.code.SpaResponseCodeEnum;
import com.hkmc.behavioralpatternanalysis.common.exception.GlobalExternalException;
import com.hkmc.behavioralpatternanalysis.common.exception.RestException;
import com.hkmc.behavioralpatternanalysis.common.model.SpaResponseDTO;
import com.hkmc.behavioralpatternanalysis.common.util.JsonUtil;
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
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@RefreshScope
public class BehavioralPatternServiceImpl implements BehavioralPatternService {

    private final InterfaceUvoDspClient interfaceUvoDspClient;
    private final InterfaceGenesisConnectedDspClient interfaceGenesisConnectedDspClient;
    private final InterfaceBluelinkDspClient interfaceBluelinkDspClient;
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
            final String vinPath = ubiSafetyVO.getVinPath();
            String appType = ubiSafetyVO.getUbiSafetyReq().getAppType();

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
                    ? UbiSafetyResDTO.builder()
                            .ServiceNo(serviceNo)
                            .RetCode(SpaResponseCodeEnum.SUCCESS.getRetCode())
                            .resCode(SpaResponseCodeEnum.SUCCESS.getResCode())
                            .build()
                    : new UbiSafetyResDTO(feignResponseSuccessBody, serviceNo);

        } catch (FeignException e) {
            log.error("\n++++++++++[FeignException] [itlCarBreakpadDrvScoreSearch] | CALL : {} | STATUS : {} | VIN : {} | AUTH : {} | {}",
                    uri, e.status(), ubiSafetyVO.getVinPath(), requestHeader.get(Const.Header.Authorization), e.getMessage());

            final Map<String, Object> feignResponseExceptionBody = Optional.ofNullable(JsonUtil.str2map(e.contentUTF8())).orElse(new HashMap<>());
            final SpaResponseCodeEnum errResponse =
                    Optional.ofNullable(feignResponseExceptionBody.get(Const.Key.ERR_CODE_MAP))
                            .orElse(StringUtils.EMPTY).equals(Const.ErrMsg.CANNOT_FOUND_VIN)
                    ? SpaResponseCodeEnum.ERROR_E110 : SpaResponseCodeEnum.ERROR_EX01;

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
        if(org.springframework.util.ObjectUtils.isEmpty(vinPath) || vinPath.length() > 17){
            throw new RestException(Const.ErrMsg.TYPE_450);
        }

        behaSvdvHistRepo = behaSvdvHistRepository();
        carTmuBasicRepo = carTmuBasicRepository();

        try {
            final CarTmuBasicInfo carTmuBasicInfo =
                    carTmuBasicRepo.findByIdHash(CarTmuBasicInfo.builder().vin(vinPath).build());

            if(ObjectUtils.isEmpty(carTmuBasicInfo) || ObjectUtils.isEmpty(carTmuBasicInfo.getCarOid())){
                return ItlBreakpadResDTO.builder().errCd(Const.RESULT_FAIL).vin(vinPath).build();
            }

            BehaSvdvHist behaSvdvHist = behaSvdvHistRepo.reactiveFindByCriteria(
                    Criteria.where(Const.Key.CRTN_YMD).is(LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern(Const.YYYYMMDD)))
                            .and(Const.Key.CAR_OID).is(carTmuBasicInfo.getCarOid())).block();

            if(ObjectUtils.isEmpty(behaSvdvHist) || StringUtils.isEmpty(behaSvdvHist.getIfDate())){
                return ItlBreakpadResDTO.builder().errCd(Const.RESULT_FAIL).vin(vinPath).build();
            }

            return ItlBreakpadResDTO.builder()
                    .vin(vinPath)
                    .ifDate(behaSvdvHist.getIfDate())
                    .severeNormal(behaSvdvHist.getSevereNormal())
                    .cntNormal(behaSvdvHist.getCntNormal())
                    .cntCaution(behaSvdvHist.getCntCaution())
                    .cntSevere(behaSvdvHist.getCntSevere())
//                    .acumTrvgDist() // TODO 어디서 ?
                    .build();

        } catch (Exception e) {
            log.error("\n++++++++++[Exception] [itlCarBreakpadDrvScore] | {}(vin) | {}", vinPath, e.getMessage());
            throw new RestException(Const.ErrMsg.TYPE_595);
        }
    }

}
