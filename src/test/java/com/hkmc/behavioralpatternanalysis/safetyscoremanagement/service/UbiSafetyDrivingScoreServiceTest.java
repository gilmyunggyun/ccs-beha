package com.hkmc.behavioralpatternanalysis.safetyscoremanagement.service;

import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
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
import com.hkmc.behavioralpatternanalysis.safetyscoremanagement.model.BehaUbiSdhbInfo;
import com.hkmc.behavioralpatternanalysis.safetyscoremanagement.service.impl.SafetyScoreManagementServiceImpl;

import ccs.core.db.repository.redis.GenericRedisRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class UbiSafetyDrivingScoreServiceTest {

	BehaUbiSdhbInfo resData;
	
	Map<String, Object> body;
	
	SafetyScoreManagementService  safetyScoreManagementService;
	
	List<RedisVin> redisVinList;
	
	RedisVin redisVin;
	
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
	
	@BeforeEach
	public void setup() throws JsonMappingException, JsonProcessingException {
		
		body = new HashMap<>();
		body.put("vin", "KMHF241DBLA285994");		
		
		//UBI 안전운전점수 조회 테스트 정보.
		resData = new BehaUbiSdhbInfo();
		resData.setNnidVin("KMHF241DBLA_TEST3b20d1b5994");
		resData.setScoreDate("20191004");
		resData.setSafetyDrvScore(57);
		resData.setInsDiscountYn("N");
		resData.setBrstAccGrade("EXCELLENCE");
		resData.setBrstDecGrade("NORMAL");
		resData.setNightDrvGrade("EXCELLENCE");
		resData.setRangeDrvDist(788);
		resData.setIfDate(LocalDateTime.now());
		resData.setCarOid(992006581);
		
		redisVinList = new ArrayList<RedisVin>();
		redisVin = new RedisVin();
		redisVin.setCarOid("992006581");
		redisVinList.add(0, redisVin);
		
		body = new HashMap<>();
		body.put("vin", "KMHF241DBLA285994");
		
		safetyScoreManagementService = new SafetyScoreManagementServiceImpl(redisTemplate, postgresqlEntityOperations, postgresqlRepositoryFactory);
	}
	
	
	@SuppressWarnings("unused")
	@Test
	public void testUbiSafetyDrivingScoreSearch() {
		log.info("testUbiSafetyDrivingScoreSearch start");
		
		String srchPatt = "*_" + body.get("vin");
		
		given(redisVinRepo.findByAllHash(srchPatt)).willReturn(redisVinList); //	Redis Vin 데이터 조회
		Map<String, Object> retResp = safetyScoreManagementService.ubiSafetyDrivingScoreSearch(body);

		log.info("testUbiSafetyDrivingScoreSearch end");
	}
	
	
	@Test
	public void testUbiSafetyDrivingScoreSearchFaile()  throws GlobalCCSException {
		log.info("testUbiSafetyDrivingScoreSearchFaile start");
		
		Assertions.assertThrows(GlobalCCSException.class, () -> {
			safetyScoreManagementService.ubiSafetyDrivingScoreSearch(body);			
		});
		
		log.info("testUbiSafetyDrivingScoreSearchFaile end");
	}	
	
	
	@Test
	public void testUbiSafetyDrivingScoreDelete() {
		log.info("testUbiSafetyDrivingScoreDelete start");

		Map<String, Object> retResp = safetyScoreManagementService.ubiSafetyDrivingScoreDelete(body);
		log.debug("[Result] = {}", retResp.toString());

		log.info("testUbiSafetyDrivingScoreDelete end");
	}
	

	@Test
	public void testUbiSafetyDrivingScoreDeleteFaile()  throws GlobalCCSException {
		log.info("testUbiSafetyDrivingScoreDeleteFaile start");
		
		Assertions.assertThrows(GlobalCCSException.class, () -> {
			safetyScoreManagementService.ubiSafetyDrivingScoreDelete(body);
		});
		
		log.info("testUbiSafetyDrivingScoreDeleteFaile end");
	}	
	
}
