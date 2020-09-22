package com.hkmc.behavioralpatternanalysis.itlCar.service;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.hkmc.behavioralpatternanalysis.common.exception.GlobalCCSException;
import com.hkmc.behavioralpatternanalysis.common.model.RedisVin;
import com.hkmc.behavioralpatternanalysis.itlCar.model.ItlCarBreakpadDrivingScore;
import com.hkmc.behavioralpatternanalysis.itlCar.service.impl.ItlCarBreakpadDrivingScoreServiceImpl;

import ccs.core.db.repository.postgre.GenericPostgreRepository;
import ccs.core.db.repository.redis.GenericRedisRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ItlCarBreakpadDrivingScoreServiceTest {
	
	ItlCarBreakpadDrivingScore reqData;
	
	ItlCarBreakpadDrivingScore resData;
	
	List<ItlCarBreakpadDrivingScore> resDataList;
	
	List<RedisVin> redisVinList;
	
	RedisVin redisVin;
	
	ItlCarBreakpadDrivingScoreService itlCarBreakpadDrivingScoreService;
	
	Map<String, Object> body;
	
	@Mock
	private RedisTemplate<byte[], byte[]> redisTemplate;
	
	@Mock
	private R2dbcEntityOperations postgresqlEntityOperations;
	
	@Mock
	private R2dbcRepositoryFactory postgresqlRepositoryFactory;
	
	@Mock
	private R2dbcConverter r2dbcConverter; 
	
	@Mock
	private GenericRedisRepository<RedisVin, String> redisVinRepo;
	
	@Mock
	GenericPostgreRepository<ItlCarBreakpadDrivingScore, Integer> repository;

	@BeforeEach
	public void setup() throws JsonMappingException, JsonProcessingException {
										
		reqData = new ItlCarBreakpadDrivingScore();
		reqData.setCarOid(992006581);
		
		resData = new ItlCarBreakpadDrivingScore();
		resData.setIfDate("20200917");
		resData.setNnidVin("KMHF241DBLA_TEST3b20d1b5994");
		resData.setPrjVehlCd("HI");
		resData.setSevereNormal("NORMAL");
		resData.setCntNormal(50);
		resData.setCntCaution(20);
		resData.setCntSevere(0);
		resData.setCntSeverePlus(0);
		resData.setCarOid(992006581);
		
		resDataList = new ArrayList<ItlCarBreakpadDrivingScore>();
		resDataList.add(0, resData);
		
		redisVinList = new ArrayList<RedisVin>();
		redisVin = new RedisVin();
		redisVin.setCarOid("992006581");
		redisVinList.add(0, redisVin);
		
		body = new HashMap<>();
		body.put("vin", "KMHF241DBLA285994");
		
		redisVinRepo = new GenericRedisRepository<>(RedisVin.class, redisTemplate);
		itlCarBreakpadDrivingScoreService = new ItlCarBreakpadDrivingScoreServiceImpl(redisTemplate, postgresqlEntityOperations, postgresqlRepositoryFactory, r2dbcConverter);
	}
	
	@Test
	public void testItlCarBreakpadDrvScoreSearchSuccess() throws GlobalCCSException {
		log.info("testUBIItlCarBreakpadDrvScoreApiSuccess start");
		
		String srchPatt = "*_" + body.get("vin").toString();
		given(redisVinRepo.findByAllHash(srchPatt)).willReturn(redisVinList); //	Redis Vin 데이터 조회
		Map<String, Object> retResp = itlCarBreakpadDrivingScoreService.itlCarBreakpadDrvScoreSearch(body);
		
		log.info("testUBIItlCarBreakpadDrvScoreApiSuccess end");

	}
	
	@Test
	public void testItlCarBreakpadDrvScoreSearchException() throws GlobalCCSException {
		log.info("testUBIItlCarBreakpadDrvScoreApiException start");

		Assertions.assertThrows(GlobalCCSException.class, () -> { 
			Map<String, Object> retResp = itlCarBreakpadDrivingScoreService.itlCarBreakpadDrvScoreSearch(body);
		});
		
		
	}
}
