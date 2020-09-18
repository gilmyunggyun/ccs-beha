package com.hkmc.behavioralpatternanalysis.ubi.service.impl;

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

import com.hkmc.behavioralpatternanalysis.common.exception.GlobalCCSException;
import com.hkmc.behavioralpatternanalysis.common.model.RedisVin;
import com.hkmc.behavioralpatternanalysis.ubi.model.UbiSafetyDrivingScore;
import com.hkmc.behavioralpatternanalysis.ubi.service.UbiSafetyDrivingScoreService;

import ccs.core.db.repository.postgre.GenericPostgreRepository;
import ccs.core.db.repository.redis.GenericRedisRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UbiSafetyDrivingScoreServiceImpl implements UbiSafetyDrivingScoreService {

	private final RedisTemplate<byte[], byte[]> redisTemplate;
	
	private final R2dbcEntityOperations postgresqlEntityOperations;
	
	private final R2dbcRepositoryFactory postgresqlRepositoryFactory;
	
	private final R2dbcConverter r2dbcConverter; 
	
	/* UBI 안전 운전 점수 조회  */
	@Override
	public Map<String, Object> ubiSafetyDrivingScoreSearch (Map<String, Object> reqBody) {
		int status = 200;
		String resultMessage = "Success";	
		Map<String, Object> resultData = new HashMap<String, Object>();
		
        try {
        	UbiSafetyDrivingScore reqData = new UbiSafetyDrivingScore();
        	
        	GenericRedisRepository<RedisVin, String> redisVinRepo = new GenericRedisRepository<>(RedisVin.class, redisTemplate);
        	
			String srchPatt = "*_" + reqBody.get("vin").toString();
			List<RedisVin> receiveRedisVinData = redisVinRepo.findByAllHash(srchPatt);
        	
			RelationalEntityInformation<UbiSafetyDrivingScore, Integer> entity = postgresqlRepositoryFactory.getEntityInformation(UbiSafetyDrivingScore.class);
			GenericPostgreRepository<UbiSafetyDrivingScore, Integer> repository = new GenericPostgreRepository<>(UbiSafetyDrivingScore.class, entity, postgresqlEntityOperations, r2dbcConverter);			
			
			reqData.setCarOid(Integer.parseInt(receiveRedisVinData.get(0).getCarOid()));
			
			List<UbiSafetyDrivingScore> resDto = repository.findByAllCriteria(Criteria.where("carOid").is(reqData.getCarOid()));
	        
	        resultData.put("body", resDto);
	        resultData.put("resultStatus", "S");
	        resultData.put("status", status);
	        resultData.put("message", resultMessage);	        
		} catch (GlobalCCSException e) {
			resultData.put("resultStatus", "F");
			resultData.put("status", e.getStatus());
			resultData.put("message", e.getErrorMessage());
		} catch (Exception e) {
			resultData.put("resultStatus", "F");
			resultData.put("status", e.getCause());
			resultData.put("message", e.getMessage());			
		} 
        
   		return resultData;
	}	
	
	/* UBI 안전 운전 점수 삭제 */
	@Override
	public Map<String, Object> ubiSafetyDrivingScoreDelete (Map<String, Object> reqBody) {
		
		int status = 200;
		String resultMessage = "Success";	
		Map<String, Object> resultData = new HashMap<String, Object>();

		try {
        	UbiSafetyDrivingScore reqData = new UbiSafetyDrivingScore();
        	
        	GenericRedisRepository<RedisVin, String> redisVinRepo = new GenericRedisRepository<>(RedisVin.class, redisTemplate);
        	
			String srchPatt = "*_" + reqBody.get("vin").toString();
			List<RedisVin> receiveRedisVinData = redisVinRepo.findByAllHash(srchPatt);
        	
			RelationalEntityInformation<UbiSafetyDrivingScore, Integer> entity = postgresqlRepositoryFactory.getEntityInformation(UbiSafetyDrivingScore.class);
			GenericPostgreRepository<UbiSafetyDrivingScore, Integer> repository = new GenericPostgreRepository<>(UbiSafetyDrivingScore.class, entity, postgresqlEntityOperations, r2dbcConverter);			
			
			reqData.setCarOid(Integer.parseInt(receiveRedisVinData.get(0).getCarOid()));
			
			List<UbiSafetyDrivingScore> resDto = repository.findByAllCriteria(Criteria.where("carOid").is(reqData.getCarOid()));
			
			if(!(ObjectUtils.isEmpty(resDto))) {
				repository.deleteAsObject(resDto.get(0));
				
		        resultData.put("resultStatus", "S");
		        resultData.put("status", status);
		        resultData.put("message", resultMessage);
			}else {
				resultData.put("resultStatus", "F");
				resultData.put("status", 500);
				resultData.put("message", "데이터 없음");				
			}
	        
		} catch (GlobalCCSException e) {
			resultData.put("resultStatus", "F");
			resultData.put("status", e.getStatus());
			resultData.put("message", e.getErrorMessage());
		} catch (Exception e) {
			resultData.put("resultStatus", "F");
			resultData.put("status", e.getCause());
			resultData.put("message", e.getMessage());			
		} 
   		return resultData;		
	}
}
