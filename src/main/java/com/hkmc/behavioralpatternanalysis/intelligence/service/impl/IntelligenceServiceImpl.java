package com.hkmc.behavioralpatternanalysis.intelligence.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.hkmc.behavioralpatternanalysis.common.Const;
import com.hkmc.behavioralpatternanalysis.common.util.JsonUtil;
import com.hkmc.behavioralpatternanalysis.intelligence.model.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.hkmc.behavioralpatternanalysis.common.exception.GlobalCCSException;
import com.hkmc.behavioralpatternanalysis.common.util.CommonUtil;
import com.hkmc.behavioralpatternanalysis.intelligence.service.IntelligenceService;

import ccs.core.db.repository.postgre.GenericPostgreRepository;
import ccs.core.db.repository.redis.GenericRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class IntelligenceServiceImpl implements IntelligenceService {
	private final R2dbcEntityOperations postgresqlEntityOperations;
	private final R2dbcRepositoryFactory postgresqlRepositoryFactory;

	private final GenericRedisRepository<CarTmuBasicInfo, String> carTmuBasicRepository;
	private final GenericRedisRepository<NadidVinAuth, String> nadidVinAuthRepository;

	private GenericPostgreRepository<BehaSvdvHist, Integer> behaSvdvHistRepository;
	private GenericPostgreRepository<BehaSvdvHist, Integer> behaSvdvHistRepository() {
		return new GenericPostgreRepository<>(
				BehaSvdvHist.class,
				this.postgresqlRepositoryFactory,
				postgresqlEntityOperations
		);
	}

	@Override
	@Async
	public void saveIntelligence(final ConsumerRecord<String, String> consumerRecord) throws GlobalCCSException {

		final TemplateConsumeDTO kafkaConsumerMap = Optional
				.ofNullable(JsonUtil.str2obj(consumerRecord.value(), TemplateConsumeDTO.class))
				.orElse(new TemplateConsumeDTO());

		// 전송일자와 현재일자가 동일할 경우와 데이터가 존재할 경우에만 실행
		if (!(LocalDate.now().format(DateTimeFormatter.ofPattern(Const.YYYYMMDD)).equals(kafkaConsumerMap.getSendDate()))
				|| 0 == Optional.ofNullable(kafkaConsumerMap.getIntelligenceVehicleList()).orElse(new ArrayList<>()).size()) {
			return;
		}

		this.behaSvdvHistRepository = behaSvdvHistRepository();

		// 변수 선언
		final String sendDate = kafkaConsumerMap.getSendDate();
		final long sendTotalPage = kafkaConsumerMap.getSendTotalPage();
		final long sendCurrentPage = kafkaConsumerMap.getSendCurrentPage();
		final long sendTotalCount = kafkaConsumerMap.getSendTotalCount();
		final long sendPageInCount = kafkaConsumerMap.getSendPageInCount();

		try {
			// 페이지 add
			CommonUtil.addConsumerPage();
			final List<BehaSvdvHist> itlVehicleList = kafkaConsumerMap.getIntelligenceVehicleList().stream()
					.map((IntelligenceDTO data) -> {
						CommonUtil.addConsumerCount();
						return BehaSvdvHist.builder()
								.ifDate(data.getIfDate())
								.nnidVin(data.getNnidVin())
								.prjVehlCd(data.getPrjVehlCd())
								.severeNormal(data.getSevereNormal())
								.cntNormal(data.getCntNormal())
								.cntCaution(data.getCntCaution())
								.cntSevere(data.getCntSevere())
								.carOid(data.getCarOid())
								.build();
					}).collect(Collectors.toList());

			this.behaSvdvHistRepository.reactiveSaveAsList(itlVehicleList).block();

			if (Objects.equals(
					this.behaSvdvHistRepository.reactiveCountByCriteria(Criteria.where(Const.Key.CRTN_YMD).is(sendDate)).block(),
					sendTotalCount
			)) {
				// 페이지 및 건수 초기화
				CommonUtil.resetConsumerCount();
				CommonUtil.resetConsumerPage();

				log.info("[IntelligenceVehicleInformationServiceImpl.saveIntelligenceVehicleInformation END]");
			}

		} catch (Exception e) {
			log.error("\n{}-kafkaHandler-exception | Record Offset : {} | partition : {} | timestamp : {} | consume-data:{} | exception:{} ",
					consumerRecord.topic(), consumerRecord.offset(), consumerRecord.partition(),
					consumerRecord.timestamp(), consumerRecord.value(), e.getMessage());
			throw new GlobalCCSException();
		}

	}

	// 차량 브레이크 패드 자료에 대한 조회 요청을 처리
	@Override
	public ItlBreakpadResDTO getItlCarBreakpadDrvScore(final String vinPath) throws GlobalCCSException {
		this.behaSvdvHistRepository = this.behaSvdvHistRepository();
		try {
			final String nadid = this.carTmuBasicRepository.findByIdHash(
					CarTmuBasicInfo.builder().vin(vinPath).build()
			).getNadid();

			final String carOid = this.nadidVinAuthRepository.findByIdHash(
					NadidVinAuth.builder().nadidVin(String.format("%s_%s", nadid, vinPath)).build()
			).getCarOid();

			final List<BehaSvdvHist> resDto = this.behaSvdvHistRepository.reactiveFindByAllCriteria(
					Criteria.where(Const.Key.CAR_OID).is(carOid)
			).block();

			return ItlBreakpadResDTO.builder()
					.body(resDto)
					.resultStatus(Const.ResponseCode.SUCCESS_STATUS)
					.message(Const.ResponseMessage.SUCCESS)
					.build();
		} catch (Exception e){
			log.error("\n++++++++++[Exception] [itlCarBreakpadDrvScore] | {}(vin) | {}",
					vinPath, e.getMessage());
			throw new GlobalCCSException(590);
		}
	}
}
