package com.hkmc.behavioralpatternanalysis.ubi.service;

import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import org.springframework.data.annotation.Id;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.relational.core.mapping.Column;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.hkmc.behavioralpatternanalysis.common.exception.GlobalCCSException;
import com.hkmc.behavioralpatternanalysis.common.model.RedisVin;
import com.hkmc.behavioralpatternanalysis.itlCar.model.ItlCarBreakpadDrivingScore;
import com.hkmc.behavioralpatternanalysis.ubi.model.UbiSafetyDrivingScore;
import com.hkmc.behavioralpatternanalysis.ubi.service.impl.UbiSafetyDrivingScoreServiceImpl;

import ccs.core.db.repository.redis.GenericRedisRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class UbiSafetyDrivingScoreServiceTest {

	UbiSafetyDrivingScore resData;
	
	Map<String, Object> body;
	
	UbiSafetyDrivingScoreService  ubiSafetyDrivingScoreService;
	
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
		resData = new UbiSafetyDrivingScore();
		resData.setNnidVin("KMHF241DBLA_TEST3b20d1b5994");
		resData.setScoreDate("20191004");
		resData.setSafetyDrvScore(57);
		resData.setInsDiscountYn("N");
		resData.setBrstAccGrade("EXCELLENCE");
		resData.setBrstDecGrade("NORMAL");
		resData.setNightDrvGrade("EXCELLENCE");
		resData.setRangeDrvDist(788);
		resData.setIfDate(LocalDate.parse("2020-09-18 14:00:52", DateTimeFormatter.ISO_DATE));
		resData.setCarOid(992006581);
		
		redisVinList = new ArrayList<RedisVin>();
		redisVin = new RedisVin();
		redisVin.setCarOid("992006581");
		redisVinList.add(0, redisVin);
		
		body = new HashMap<>();
		body.put("vin", "KMHF241DBLA285994");
		
		ubiSafetyDrivingScoreService = new UbiSafetyDrivingScoreServiceImpl(redisTemplate, postgresqlEntityOperations, postgresqlRepositoryFactory, r2dbcConverter);
	}
	
	
	@Test
	public void testUbiSafetyDrivingScoreSearch() {
		log.info("testUbiSafetyDrivingScoreSearch start");
		
		String srchPatt = "*_" + body.get("vin");
		
		given(redisVinRepo.findByAllHash(srchPatt)).willReturn(redisVinList); //	Redis Vin 데이터 조회
		Map<String, Object> retResp = ubiSafetyDrivingScoreService.ubiSafetyDrivingScoreSearch(body);

		log.info("testUbiSafetyDrivingScoreSearch end");
	}
	
	
	@Test
	public void testUbiSafetyDrivingScoreSearchFaile()  throws GlobalCCSException {
		log.info("testUbiSafetyDrivingScoreSearchFaile start");
		
		Assertions.assertThrows(GlobalCCSException.class, () -> {
			ubiSafetyDrivingScoreService.ubiSafetyDrivingScoreSearch(body);			
		});
		
		log.info("testUbiSafetyDrivingScoreSearchFaile end");
	}	
	
	
	@Test
	public void testUbiSafetyDrivingScoreDelete() {
		log.info("testUbiSafetyDrivingScoreDelete start");

		Map<String, Object> retResp = ubiSafetyDrivingScoreService.ubiSafetyDrivingScoreDelete(body);
		log.debug("[Result] = {}", retResp.toString());

		log.info("testUbiSafetyDrivingScoreDelete end");
	}
	

	@Test
	public void testUbiSafetyDrivingScoreDeleteFaile()  throws GlobalCCSException {
		log.info("testUbiSafetyDrivingScoreDeleteFaile start");
		
		Assertions.assertThrows(GlobalCCSException.class, () -> {
			ubiSafetyDrivingScoreService.ubiSafetyDrivingScoreDelete(body);
		});
		
		log.info("testUbiSafetyDrivingScoreDeleteFaile end");
	}	
	
}
