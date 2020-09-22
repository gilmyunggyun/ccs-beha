package com.hkmc.behavioralpatternanalysis.itlCar.service.impl;

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

import com.hkmc.behavioralpatternanalysis.common.exception.GlobalCCSException;
import com.hkmc.behavioralpatternanalysis.common.model.RedisVin;
import com.hkmc.behavioralpatternanalysis.itlCar.model.ItlCarBreakpadDrivingScore;
import com.hkmc.behavioralpatternanalysis.itlCar.service.ItlCarBreakpadDrivingScoreService;

import ccs.core.db.repository.postgre.GenericPostgreRepository;
import ccs.core.db.repository.redis.GenericRedisRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItlCarBreakpadDrivingScoreServiceImpl implements ItlCarBreakpadDrivingScoreService {

	private final RedisTemplate<byte[], byte[]> redisTemplate;
	
	private final R2dbcEntityOperations postgresqlEntityOperations;
	
	private final R2dbcRepositoryFactory postgresqlRepositoryFactory;
	
	private final R2dbcConverter r2dbcConverter; 
	
	/* 차량 브레이크 패드 자료에 대한 조회 요청을 처리  */
	@Override
	public Map<String, Object> itlCarBreakpadDrvScoreSearch(Map<String, Object> reqBody) {
		int status = 200;
		String resMsg = "Success";
		Map<String, Object> resultData = new HashMap<String, Object>();
		
		try {
			ItlCarBreakpadDrivingScore reqData = new ItlCarBreakpadDrivingScore();
			
			GenericRedisRepository<RedisVin, String> redisVinRepo = new GenericRedisRepository<>(RedisVin.class, redisTemplate);
			
			String srchPatt = "*_" + reqBody.get("vin").toString();
			List<RedisVin> receiveRedisVinData = redisVinRepo.findByAllHash(srchPatt);
			
			RelationalEntityInformation<ItlCarBreakpadDrivingScore, Integer> entity = postgresqlRepositoryFactory.getEntityInformation(ItlCarBreakpadDrivingScore.class);
			GenericPostgreRepository<ItlCarBreakpadDrivingScore, Integer> repository = new GenericPostgreRepository<>(ItlCarBreakpadDrivingScore.class, entity, postgresqlEntityOperations, r2dbcConverter);			
			
			reqData.setCarOid(Integer.parseInt(receiveRedisVinData.get(0).getCarOid()));
			
			List<ItlCarBreakpadDrivingScore> resDto = repository.findByAllCriteria(Criteria.where("carOid").is(reqData.getCarOid()));
			resultData.put("body", resDto);
			resultData.put("resultStatus", "S");
			resultData.put("status", status);
			resultData.put("message", resMsg);
			
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
