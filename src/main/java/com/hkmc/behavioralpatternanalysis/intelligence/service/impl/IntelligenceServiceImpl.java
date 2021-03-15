package com.hkmc.behavioralpatternanalysis.intelligence.service.impl;

import java.util.*;

import com.hkmc.behavioralpatternanalysis.intelligence.model.*;
import io.netty.util.internal.ObjectUtil;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory;
import org.springframework.stereotype.Service;

import com.hkmc.behavioralpatternanalysis.common.exception.GlobalCCSException;
import com.hkmc.behavioralpatternanalysis.intelligence.service.IntelligenceService;

import ccs.core.db.repository.postgre.GenericPostgreRepository;
import ccs.core.db.repository.redis.GenericRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class IntelligenceServiceImpl implements IntelligenceService {
    private final GenericRedisRepository<CarTmuBasicInfo, String> carTmuBasicRepository;

    private final R2dbcEntityOperations postgresqlEntityOperations;
    private final R2dbcRepositoryFactory postgresqlRepositoryFactory;

    private GenericPostgreRepository<BehaSvdvHist, Integer> behaSvdvHistRepository;
    private GenericPostgreRepository<BehaSvdvHist, Integer> behaSvdvHistRepository() {
        return new GenericPostgreRepository<>(
                BehaSvdvHist.class,
                this.postgresqlRepositoryFactory,
                postgresqlEntityOperations
        );
    }

    // 차량 브레이크 패드 자료에 대한 조회 요청을 처리
    @Override
    public ItlBreakpadResDTO getItlCarBreakpadDrvScore(final String vinPath) throws GlobalCCSException {
        if(ObjectUtils.isEmpty(vinPath) || vinPath.length() > 17){
            return ItlBreakpadResDTO.builder()
                    .resultStatus("F")
                    .errCd("InvaildReqSchema")
                    .errNm("450")
                    .build();
        }

        try {
            this.behaSvdvHistRepository = this.behaSvdvHistRepository();
            final CarTmuBasicInfo carTmuBasicInfo = this.carTmuBasicRepository.findByIdHash(
                    CarTmuBasicInfo.builder().vin(vinPath).build()
            );
            final String carOid = carTmuBasicInfo.getCarOid();
            if(ObjectUtils.isEmpty(carOid)){
                return new ItlBreakpadResDTO(new BehaSvdvHist(), vinPath, null, "F");
            }

            final List<BehaSvdvHist> behaSvdvHistList = behaSvdvHistRepository.reactiveFindByQuery(
                    String.format("select * from BEHA_SVDV_HIST where CAR_OID='%s' ORDER BY CRTN_YMD DESC LIMIT 1", carOid)
            ).block();
            if(ObjectUtils.isEmpty(behaSvdvHistList) || behaSvdvHistList.size() == 0){
                return new ItlBreakpadResDTO(new BehaSvdvHist(), vinPath, null, "F");
            }

            return new ItlBreakpadResDTO(behaSvdvHistList.get(0), vinPath, 0, "S");// TODO: aucmTrvgDist(운행거리?) 관련 넘겨받을곳 어디인지 확인 필요
        } catch (Exception e) {
            log.error("\n++++++++++[Exception] [itlCarBreakpadDrvScore] | {}(vin) | {}",
                    vinPath, e.getMessage());
            return ItlBreakpadResDTO.builder()
                    .resultStatus("F")
                    .errCd("InternelBizException")
                    .errNm("595")
                    .build();
        }
    }
}
