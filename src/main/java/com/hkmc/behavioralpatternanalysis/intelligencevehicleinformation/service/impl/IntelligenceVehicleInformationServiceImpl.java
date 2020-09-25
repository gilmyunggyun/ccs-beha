package com.hkmc.behavioralpatternanalysis.intelligencevehicleinformation.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.repository.query.RelationalEntityInformation;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.hkmc.behavioralpatternanalysis.common.exception.GlobalCCSException;
import com.hkmc.behavioralpatternanalysis.common.model.RedisVin;
import com.hkmc.behavioralpatternanalysis.common.util.CommonUtil;
import com.hkmc.behavioralpatternanalysis.intelligencevehicleinformation.model.BehaSvdvHist;
import com.hkmc.behavioralpatternanalysis.intelligencevehicleinformation.service.IntelligenceVehicleInformationService;

import ccs.core.db.repository.postgre.GenericPostgreRepository;
import ccs.core.db.repository.redis.GenericRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class IntelligenceVehicleInformationServiceImpl implements IntelligenceVehicleInformationService {

	private final RedisTemplate<byte[], byte[]> redisTemplate;	
	private final R2dbcEntityOperations postgresqlEntityOperations;
	private final R2dbcRepositoryFactory postgresqlRepositoryFactory;

	@SuppressWarnings("unchecked")
	public void saveIntelligenceVehicleInformation(Map<String, Object> kafkaConsumerMap) throws GlobalCCSException {

		RelationalEntityInformation<BehaSvdvHist, Integer> entity = postgresqlRepositoryFactory.getEntityInformation(BehaSvdvHist.class);
        GenericPostgreRepository<BehaSvdvHist, Integer> repository = new GenericPostgreRepository<>(BehaSvdvHist.class, entity, postgresqlEntityOperations);
		
		// 변수 선언
		String sendDate = kafkaConsumerMap.get("sendDate").toString();
		long sendTotalPage = Long.parseLong(kafkaConsumerMap.get("sendTotalPage").toString());
		long sendCurrentPage = Long.parseLong(kafkaConsumerMap.get("sendCurrentPage").toString());
		long sendTotalCount = Long.parseLong(kafkaConsumerMap.get("sendTotalCount").toString());
		long sendPageInCount = Long.parseLong(kafkaConsumerMap.get("sendPageInCount").toString());

		String strNnidVin = "";
		int intCarOid = 0;
	
		try {

			// 페이지 add
			CommonUtil.addConsumerPage();

			List<BehaSvdvHist> behaSvdvHistList = new ArrayList<>();
			
			// listData 담기
			List<Map<String, Object>> kafkaListData = (List<Map<String, Object>>) kafkaConsumerMap.get("listData");
			
			Gson gson = new Gson();
			
	    	for (Map<String, Object> data : kafkaListData) {

	    		// 진행 건수 add
				CommonUtil.addConsumerCount();
	    		
	    		BehaSvdvHist behaSvdvHist = gson.fromJson(data.toString(), BehaSvdvHist.class);

	    		// carOid를 가져와 저장하기 위함
				strNnidVin = behaSvdvHist.getNnidVin();
	    		intCarOid = Integer.parseInt(strNnidVin.substring(16, (16 + (strNnidVin.length() - 19))), 16);
	    		
	    		behaSvdvHist.setCarOid(intCarOid);
	    		
	    		behaSvdvHistList.add(behaSvdvHist);

	    	}

	    	repository.reactiveSaveAsList(behaSvdvHistList);
	    	
	    	if(selectBehaSvdvHistCount(sendDate) == sendTotalCount) {
	    		
	    		// 페이지 및 건수 초기화
				CommonUtil.resetConsumerCount();
				CommonUtil.resetConsumerPage();
				
		    	log.info("[IntelligenceVehicleInformationServiceImpl.saveIntelligenceVehicleInformation END]");
		    	
	    	}
	    	
		}
		catch (Exception e) {

			log.error("[IntelligenceVehicleInformationServiceImpl.saveIntelligenceVehicleInformation Error] : sendDate({}), sendTotalPage({}) / currentPage({}) / sendCurrentPage({}), sendTotalCount({}) / currentCount({}) / sendPageInCount({}), vin({})"
					, sendDate, sendTotalPage, CommonUtil.consumerPage, sendCurrentPage, sendTotalCount, CommonUtil.consumerCount,  sendPageInCount, strNnidVin);

			// 페이지 및 건수 초기화
			CommonUtil.resetConsumerCount();
			CommonUtil.resetConsumerPage();
			
			log.error("[IntelligenceVehicleInformationServiceImpl.saveIntelligenceVehicleInformation] Ex : ", e);
		} 
		
	}
	
	
	// BEHA_SVDV_HIST의 데이터건수 가져오기
	public long selectBehaSvdvHistCount(String sendDate) throws GlobalCCSException {

		RelationalEntityInformation<BehaSvdvHist, Integer> entity = postgresqlRepositoryFactory.getEntityInformation(BehaSvdvHist.class);
        GenericPostgreRepository<BehaSvdvHist, Integer> repository = new GenericPostgreRepository<>(BehaSvdvHist.class, entity, postgresqlEntityOperations);

		long behaSvdvHistCount = 0;

		behaSvdvHistCount = repository.reactiveCountByCriteria(Criteria.where("ifDate").is(sendDate)).block();

		return behaSvdvHistCount;
		
	}
	
	// 차량 브레이크 패드 자료에 대한 조회 요청을 처리 
	@Override
	public Map<String, Object> itlCarBreakpadDrvScoreSearch(Map<String, Object> reqBody) {

		RelationalEntityInformation<BehaSvdvHist, Integer> entity = postgresqlRepositoryFactory.getEntityInformation(BehaSvdvHist.class);
        GenericPostgreRepository<BehaSvdvHist, Integer> repository = new GenericPostgreRepository<>(BehaSvdvHist.class, entity, postgresqlEntityOperations);

		GenericRedisRepository<RedisVin, String> redisVinRepo = new GenericRedisRepository<>(RedisVin.class, redisTemplate);

		Map<String, Object> resultData = new HashMap<String, Object>();
		
		BehaSvdvHist reqData = new BehaSvdvHist();
		
		int status = 200;
		String resMsg = "Success";
		
		try {

			String srchPatt = "*_" + reqBody.get("vin").toString();
			List<RedisVin> receiveRedisVinData = redisVinRepo.findByAllHash(srchPatt);
			
			reqData.setCarOid(Integer.parseInt(receiveRedisVinData.get(0).getCarOid()));
			
			List<BehaSvdvHist> resDto = repository.reactiveFindByAllCriteria(Criteria.where("carOid").is(reqData.getCarOid())).block();

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
