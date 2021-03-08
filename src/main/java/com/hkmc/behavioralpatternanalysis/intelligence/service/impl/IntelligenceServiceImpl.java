package com.hkmc.behavioralpatternanalysis.intelligence.service.impl;

import java.util.*;

import com.hkmc.behavioralpatternanalysis.intelligence.model.*;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory;
import org.springframework.stereotype.Service;

import com.hkmc.behavioralpatternanalysis.common.exception.GlobalCCSException;
import com.hkmc.behavioralpatternanalysis.intelligence.service.IntelligenceService;

import ccs.core.db.repository.postgre.GenericPostgreRepository;
import ccs.core.db.repository.redis.GenericRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class IntelligenceServiceImpl implements IntelligenceService {
    private final GenericRedisRepository<CarTmuBasicInfo, String> carTmuBasicRepository;
    private final GenericRedisRepository<NadidVinAuth, String> nadidVinAuthRepository;

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
        this.behaSvdvHistRepository = this.behaSvdvHistRepository();
        try {
            final String nadid = this.carTmuBasicRepository.findByIdHash(
                    CarTmuBasicInfo.builder().vin(vinPath).build()
            ).getNadid();

            String carOid = this.nadidVinAuthRepository.findByIdHash(
                    NadidVinAuth.builder().nadidVin(String.format("%s_%s", nadid, vinPath)).build()
            ).getCarOid();

            final BehaSvdvHist behaSvdvHist = Optional
                    .ofNullable(behaSvdvHistRepository.reactiveFindByQuery(
                            String.format("select * from BEHA_SVDV_HIST where CAR_OID='%s' ORDER BY CRTN_YMD DESC LIMIT 1", carOid)
                    ).block())
                    .orElse(new ArrayList<>())
                    .get(0);

            return new ItlBreakpadResDTO(behaSvdvHist, vinPath, 0);// TODO: aucmTrvgDist 관련 확인 필요
        } catch (Exception e) {
            log.error("\n++++++++++[Exception] [itlCarBreakpadDrvScore] | {}(vin) | {}",
                    vinPath, e.getMessage());
            throw new GlobalCCSException(590);
        }
    }
}
