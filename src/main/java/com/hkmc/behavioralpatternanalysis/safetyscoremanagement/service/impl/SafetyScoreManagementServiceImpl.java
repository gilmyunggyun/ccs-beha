package com.hkmc.behavioralpatternanalysis.safetyscoremanagement.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.repository.query.RelationalEntityInformation;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.google.gson.Gson;
import com.hkmc.behavioralpatternanalysis.common.exception.GlobalCCSException;
import com.hkmc.behavioralpatternanalysis.common.model.RedisVin;
import com.hkmc.behavioralpatternanalysis.common.util.CommonUtil;
import com.hkmc.behavioralpatternanalysis.safetyscoremanagement.model.BehaUbiSdhbInfo;
import com.hkmc.behavioralpatternanalysis.safetyscoremanagement.model.BehaUbiSdhbInfoTemp;
import com.hkmc.behavioralpatternanalysis.safetyscoremanagement.service.SafetyScoreManagementService;

import ccs.core.db.repository.postgre.GenericPostgreRepository;
import ccs.core.db.repository.redis.GenericRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SafetyScoreManagementServiceImpl implements SafetyScoreManagementService {

	private final RedisTemplate<byte[], byte[]> redisTemplate;
	private final R2dbcEntityOperations postgresqlEntityOperations;
	private final R2dbcRepositoryFactory postgresqlRepositoryFactory;
	private final R2dbcConverter r2dbcConverter;

	public void saveSafetyScoreManagement(Map<String, Object> kafkaConsumerMap) throws GlobalCCSException {

		try {
	
			// 페이지 add
			CommonUtil.addConsumerPage();
			
			// 처음 실행시 데이터가 존재할 수 있을 경우를 대비하여 삭제
			if (CommonUtil.getConsumerPage() == 1) {
				
				// BEHA_UBI_SDHB_INFO_TEMP 삭제
				deleteBehaUbiSdhbInfoTemp();

			}

			// BEHA_UBI_SDHB_INFO_TEMP에 데이터 저장
			insertBehaUbiSdhbInfoTemp(kafkaConsumerMap);
			
			// selectBehaUbiSdhbInfoTempCount()가 sendTotalCount와 동일할 경우만 실행
			if(selectBehaUbiSdhbInfoTempCount() == Long.parseLong(kafkaConsumerMap.get("sendTotalCount").toString())) {
				
				// BEHA_UBI_SDHB_INFO의 기존 데이터 삭제
				deleteBehaUbiSdhbInfo();
				
				// BEHA_UBI_SDHB_INFO_TEMP의 데이터를 BEHA_UBI_SDHB_INFO에 저장
				insertBehaUbiSdhbInfo();

				// BEHA_UBI_SDHB_INFO_TEMP 삭제
				deleteBehaUbiSdhbInfoTemp();				

				// 페이지 및 건수 초기화
				CommonUtil.resetConsumerPage();
				CommonUtil.resetConsumerCount();
				
				log.info("[SafetyScoreManagementServiceImpl.saveSafetyScoreManagement END]");
			}

		}
		catch(Exception e) {

			// 페이지 및 건수 초기화
			CommonUtil.resetConsumerPage();
			CommonUtil.resetConsumerCount();
			
			log.error("[SafetyScoreManagementServiceImpl.saveSafetyScoreManagement] Ex : ", e);
		}

	}
	
	
	// BEHA_UBI_SDHB_INFO_TEMP 삭제
	public void deleteBehaUbiSdhbInfoTemp() throws GlobalCCSException {

		RelationalEntityInformation<BehaUbiSdhbInfoTemp, Integer> entityTemp = postgresqlRepositoryFactory.getEntityInformation(BehaUbiSdhbInfoTemp.class);
		GenericPostgreRepository<BehaUbiSdhbInfoTemp, Integer> jpaRepositoryTemp = new GenericPostgreRepository<>(BehaUbiSdhbInfoTemp.class, entityTemp, postgresqlEntityOperations, r2dbcConverter);

		try {

			jpaRepositoryTemp.deleteAsAll();

		}
		catch(Exception e) {

			log.error("[SafetyScoreManagementServiceImpl.deleteBehaUbiSdhbInfoTemp] Ex : ", e);

		}

	}


	// BEHA_UBI_SDHB_INFO_TEMP에 데이터 저장
	@SuppressWarnings("unchecked")
	public void insertBehaUbiSdhbInfoTemp(Map<String, Object> kafkaConsumerMap) throws GlobalCCSException {

		RelationalEntityInformation<BehaUbiSdhbInfoTemp, Integer> entityTemp = postgresqlRepositoryFactory.getEntityInformation(BehaUbiSdhbInfoTemp.class);
		GenericPostgreRepository<BehaUbiSdhbInfoTemp, Integer> jpaRepositoryTemp = new GenericPostgreRepository<>(BehaUbiSdhbInfoTemp.class, entityTemp, postgresqlEntityOperations, r2dbcConverter);

		List<Map<String, Object>> kafkaListData = (List<Map<String, Object>>) kafkaConsumerMap.get("listData");

		List<BehaUbiSdhbInfoTemp> behaUbiSdhbInfoTempList = new ArrayList<>();
		
		Gson gson = new Gson();
		
		// 변수 선언
		String sendDate = kafkaConsumerMap.get("sendDate").toString();
		long sendTotalPage = Long.parseLong(kafkaConsumerMap.get("sendTotalPage").toString());
		long sendCurrentPage = Long.parseLong(kafkaConsumerMap.get("sendCurrentPage").toString());
		long sendTotalCount = Long.parseLong(kafkaConsumerMap.get("sendTotalCount").toString());
		long sendPageInCount = Long.parseLong(kafkaConsumerMap.get("sendPageInCount").toString());
		
		String strNnidVin = "";
		int intCarOid = 0;
		
		try {
			
	    	for (Map<String, Object> data : kafkaListData) {
	    		
				CommonUtil.addConsumerCount();
	    		
				BehaUbiSdhbInfoTemp behaUbiSdhbInfoTemp = gson.fromJson(data.toString(), BehaUbiSdhbInfoTemp.class);
				
	    		// carOid를 가져와 저장하기 위함
				strNnidVin = behaUbiSdhbInfoTemp.getNnidVin();
				intCarOid = Integer.parseInt(strNnidVin.substring(16, (16 + (strNnidVin.length() - 19))), 16);
	    		
	    		behaUbiSdhbInfoTemp.setCarOid(intCarOid);
	    		behaUbiSdhbInfoTemp.setIfDate(LocalDateTime.now());
    		
	    		behaUbiSdhbInfoTempList.add(behaUbiSdhbInfoTemp);

	    	}

	    	jpaRepositoryTemp.saveAsList(behaUbiSdhbInfoTempList);
	    	
		}
		catch (Exception e) {

			log.error("[SafetyScoreManagementServiceImpl.insertBehaUbiSdhbInfoTemp Error] : sendDate({}), sendTotalPage({}) / currentPage({}) / sendCurrentPage({}), sendTotalCount({}) / currentCount({}) / sendPageInCount({}), vin({})"
					, sendDate, sendTotalPage, CommonUtil.consumerPage, sendCurrentPage, sendTotalCount, CommonUtil.consumerCount,  sendPageInCount, strNnidVin);

			// 페이지 및 건수 초기화
			CommonUtil.resetConsumerCount();
			CommonUtil.resetConsumerPage();
			
			log.error("[SafetyScoreManagementServiceImpl.insertBehaUbiSdhbInfoTemp] Ex : ", e);
		} 

	}


	// BEHA_UBI_SDHB_INFO의 기존 데이터 삭제
	public void deleteBehaUbiSdhbInfo() throws GlobalCCSException {

		RelationalEntityInformation<BehaUbiSdhbInfo, Integer> entity = postgresqlRepositoryFactory.getEntityInformation(BehaUbiSdhbInfo.class);
		GenericPostgreRepository<BehaUbiSdhbInfo, Integer> jpaRepository = new GenericPostgreRepository<>(BehaUbiSdhbInfo.class, entity, postgresqlEntityOperations, r2dbcConverter);

		try {

			jpaRepository.deleteAsAll();
		}
		catch (Exception e) {

			log.error("[SafetyScoreManagementServiceImpl.deleteBehaUbiSdhbInfo] Ex : ", e);
			
		}

	}


	// BEHA_UBI_SDHB_INFO_TEMP의 데이터를 BEHA_UBI_SDHB_INFO에 저장
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void insertBehaUbiSdhbInfo() throws GlobalCCSException {

		RelationalEntityInformation<BehaUbiSdhbInfoTemp, Integer> entityTemp = postgresqlRepositoryFactory.getEntityInformation(BehaUbiSdhbInfoTemp.class);
		GenericPostgreRepository<BehaUbiSdhbInfoTemp, Integer> jpaRepositoryTemp = new GenericPostgreRepository<>(BehaUbiSdhbInfoTemp.class, entityTemp, postgresqlEntityOperations, r2dbcConverter);
		
		RelationalEntityInformation<BehaUbiSdhbInfo, Integer> entity = postgresqlRepositoryFactory.getEntityInformation(BehaUbiSdhbInfo.class);
		GenericPostgreRepository<BehaUbiSdhbInfo, Integer> jpaRepository = new GenericPostgreRepository<>(BehaUbiSdhbInfo.class, entity, postgresqlEntityOperations, r2dbcConverter);

		try {
			
			// BEHA_UBI_SDHB_INFO_TEMP 정보 가져오기
			List behaUbiSdhbInfoTempList = new ArrayList();
			behaUbiSdhbInfoTempList = jpaRepositoryTemp.findByAll();

			// behaUbiSdhbInfoList에 복사하기
			List behaUbiSdhbInfoList = new ArrayList(behaUbiSdhbInfoTempList);
			Collections.copy(behaUbiSdhbInfoList, behaUbiSdhbInfoTempList);
			
			// BEHA_UBI_SDHB_INFO에 저장
			jpaRepository.saveAsList(behaUbiSdhbInfoList);

		}
		catch (Exception e) {

			log.error("[SafetyScoreManagementServiceImpl.insertBehaUbiSdhbInfo] Ex : ", e);

		}

	}

	
	// BEHA_UBI_SDHB_INFO_TEMP의 데이터건수 가져오기
	public long selectBehaUbiSdhbInfoTempCount() throws GlobalCCSException {

		RelationalEntityInformation<BehaUbiSdhbInfoTemp, Integer> entityTemp = postgresqlRepositoryFactory.getEntityInformation(BehaUbiSdhbInfoTemp.class);
		GenericPostgreRepository<BehaUbiSdhbInfoTemp, Integer> jpaRepositoryTemp = new GenericPostgreRepository<>(BehaUbiSdhbInfoTemp.class, entityTemp, postgresqlEntityOperations, r2dbcConverter);

		long behaUbiSdhbInfoTempCount = 0;
		
		behaUbiSdhbInfoTempCount = jpaRepositoryTemp.countAll();

		return behaUbiSdhbInfoTempCount;

	}

	
	// UBI 안전 운전 점수 조회
	@Override
	public Map<String, Object> ubiSafetyDrivingScoreSearch (Map<String, Object> reqBody) {

		RelationalEntityInformation<BehaUbiSdhbInfo, Integer> entity = postgresqlRepositoryFactory.getEntityInformation(BehaUbiSdhbInfo.class);
		GenericPostgreRepository<BehaUbiSdhbInfo, Integer> repository = new GenericPostgreRepository<>(BehaUbiSdhbInfo.class, entity, postgresqlEntityOperations, r2dbcConverter);

    	GenericRedisRepository<RedisVin, String> redisVinRepo = new GenericRedisRepository<>(RedisVin.class, redisTemplate);
		
		Map<String, Object> resultData = new HashMap<String, Object>();

    	BehaUbiSdhbInfo reqData = new BehaUbiSdhbInfo();

		int status = 200;
		String resultMessage = "Success";
		
        try {
        	
			String srchPatt = "*_" + reqBody.get("vin").toString();
			List<RedisVin> receiveRedisVinData = redisVinRepo.findByAllHash(srchPatt);
        	
			reqData.setCarOid(Integer.parseInt(receiveRedisVinData.get(0).getCarOid()));
			
			List<BehaUbiSdhbInfo> resDto = repository.findByAllCriteria(Criteria.where("carOid").is(reqData.getCarOid()));
	        
	        resultData.put("body", resDto);
	        resultData.put("resultStatus", "S");
	        resultData.put("status", status);
	        resultData.put("message", resultMessage);

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


	// UBI 안전 운전 점수 삭제
	@Override
	public Map<String, Object> ubiSafetyDrivingScoreDelete (Map<String, Object> reqBody) {

		RelationalEntityInformation<BehaUbiSdhbInfo, Integer> entity = postgresqlRepositoryFactory.getEntityInformation(BehaUbiSdhbInfo.class);
		GenericPostgreRepository<BehaUbiSdhbInfo, Integer> repository = new GenericPostgreRepository<>(BehaUbiSdhbInfo.class, entity, postgresqlEntityOperations, r2dbcConverter);
		
		GenericRedisRepository<RedisVin, String> redisVinRepo = new GenericRedisRepository<>(RedisVin.class, redisTemplate);
		
		int status = 200;
		String resultMessage = "Success";
		
		Map<String, Object> resultData = new HashMap<String, Object>();

    	BehaUbiSdhbInfo reqData = new BehaUbiSdhbInfo();
		
		try {

			String srchPatt = "*_" + reqBody.get("vin").toString();
			List<RedisVin> receiveRedisVinData = redisVinRepo.findByAllHash(srchPatt);

			reqData.setCarOid(Integer.parseInt(receiveRedisVinData.get(0).getCarOid()));
			
			List<BehaUbiSdhbInfo> resDto = repository.findByAllCriteria(Criteria.where("carOid").is(reqData.getCarOid()));
			
			if(!(ObjectUtils.isEmpty(resDto))) {

				repository.deleteAsObject(resDto.get(0));
				
		        resultData.put("resultStatus", "S");
		        resultData.put("status", status);
		        resultData.put("message", resultMessage);

			}
			else {

				resultData.put("resultStatus", "F");
				resultData.put("status", 500);
				resultData.put("message", "데이터 없음");

			}
	        
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
