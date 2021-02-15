package com.hkmc.behavioralpatternanalysis.intelligencevehicleinformation.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.hkmc.behavioralpatternanalysis.common.util.JsonUtil;
import com.hkmc.behavioralpatternanalysis.config.RedisConfig;
import com.hkmc.behavioralpatternanalysis.intelligencevehicleinformation.model.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.repository.query.RelationalEntityInformation;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.hkmc.behavioralpatternanalysis.common.exception.GlobalCCSException;
import com.hkmc.behavioralpatternanalysis.common.model.RedisVin;
import com.hkmc.behavioralpatternanalysis.common.util.CommonUtil;
import com.hkmc.behavioralpatternanalysis.intelligencevehicleinformation.service.IntelligenceVehicleInformationService;

import ccs.core.db.repository.postgre.GenericPostgreRepository;
import ccs.core.db.repository.redis.GenericRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class IntelligenceVehicleInformationServiceImpl implements IntelligenceVehicleInformationService {

//	private final RedisTemplate<byte[], byte[]> redisTemplate;

	private final R2dbcEntityOperations postgresqlEntityOperations;
	private final R2dbcRepositoryFactory postgresqlRepositoryFactory;
	@Autowired
	private GenericRedisRepository<CarTmuBasicInfoDTO, String> carTmuBasicRepository;
	@Autowired
	private GenericRedisRepository<NadidVinAuthDTO, String> nadidVinAuthRepository;

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
	public void saveIntelligenceVehicleInformation(ConsumerRecord<String, String> consumerRecord) throws GlobalCCSException {

		TemplateProduceDTO kafkaConsumerMap = JsonUtil.str2obj(consumerRecord.value(), TemplateProduceDTO.class);

		// 전송일자와 현재일자가 동일할 경우와 데이터가 존재할 경우에만 실행
		final String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		final boolean sendDateIsNotToday = !(today.equals(kafkaConsumerMap.getSendDate()));
		final int dataLength = Optional.ofNullable(kafkaConsumerMap.getIntelligenceVehicleList()).orElse(new ArrayList<>()).size();
		if (sendDateIsNotToday || dataLength == 0) {
			return;
		}

		this.behaSvdvHistRepository = behaSvdvHistRepository();

		// 변수 선언
		String sendDate = kafkaConsumerMap.getSendDate();
		long sendTotalPage = kafkaConsumerMap.getSendTotalPage();
		long sendCurrentPage = kafkaConsumerMap.getSendCurrentPage();
		long sendTotalCount = kafkaConsumerMap.getSendTotalCount();
		long sendPageInCount = kafkaConsumerMap.getSendPageInCount();

		try {
			// 페이지 add
			CommonUtil.addConsumerPage();
			this.behaSvdvHistRepository
					.reactiveSaveAsList(kafkaConsumerMap.getIntelligenceVehicleList().stream ()
							.map((IntelligenceVehicleDTO data) -> {
								CommonUtil.addConsumerCount();
								return BehaSvdvHist.builder()
										.ifDate(data.getIfDate())
										.nnidVin(data.getNnidVin())
										.prjVehlCd(data.getPrjVehlCd())
										.severeNormal(data.getSevereNormal())
										.cntNormal(data.getCntNormal())
										.cntCaution(data.getCntCaution())
										.cntSevere(data.getCntSevere())
										.carOid(Integer.parseInt(data.getNnidVin().substring(16, (16 + (data.getNnidVin().length() - 19))), 16))
										.build();
							}).collect(Collectors.toList()))
					.block();

			if (Objects.equals(
					this.behaSvdvHistRepository.reactiveCountByCriteria(Criteria.where("ifDate").is(sendDate)).block(),
					sendTotalCount)) {
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

//			log.error("[IntelligenceVehicleInformationServiceImpl.saveIntelligenceVehicleInformation Error] : sendDate({}), sendTotalPage({}) / currentPage({}) / sendCurrentPage({}), sendTotalCount({}) / currentCount({}) / sendPageInCount({}), vin({})"
//					, sendDate, sendTotalPage, CommonUtil.consumerPage, sendCurrentPage, sendTotalCount, CommonUtil.consumerCount, sendPageInCount, strNnidVin);
//
//			// 페이지 및 건수 초기화
//			CommonUtil.resetConsumerCount();
//			CommonUtil.resetConsumerPage();
//
//			log.error("[IntelligenceVehicleInformationServiceImpl.saveIntelligenceVehicleInformation] Ex : ", e);
		}

	}

	// 차량 브레이크 패드 자료에 대한 조회 요청을 처리
	@Override
	public Map<String, Object> itlCarBreakpadDrvScoreSearch(Map<String, Object> reqBody) {

		Map<String, Object> resultData = new HashMap<String, Object>();
		
		BehaSvdvHist reqData = new BehaSvdvHist();
		
		int status = 200;
		String resMsg = "Success";
		
		try {

			final String vinPath = reqBody.get("vin").toString();
			final String nadid = this.carTmuBasicRepository.findByIdHash(
					CarTmuBasicInfoDTO.builder().vin(vinPath).build()
			).getNadid();

			final String carOid = this.nadidVinAuthRepository.findByIdHash(
				NadidVinAuthDTO.builder().nadidVin(String.format("%s_%s", nadid, vinPath)).build()
			).getCarOid();

			reqData.setCarOid(Integer.parseInt(carOid));
			
			List<BehaSvdvHist> resDto = this.behaSvdvHistRepository.reactiveFindByAllCriteria(
					Criteria.where("carOid").is(reqData.getCarOid())
			).block();

			resultData.put("body", resDto);
			resultData.put("resultStatus", "S");
			resultData.put("status", status);
			resultData.put("message", resMsg);
			
		}
		catch (GlobalCCSException e) {
			
			resultData.put("resultStatus", "F");
			resultData.put("status", e.getStatus());
			resultData.put("message", e.getErrorMessage());
			
		}
		catch (Exception e) {
			
			resultData.put("resultStatus", "F");
			resultData.put("status", e.getCause());
			resultData.put("message", e.getMessage());
			
		}
        
   		return resultData;

	}
	
}
