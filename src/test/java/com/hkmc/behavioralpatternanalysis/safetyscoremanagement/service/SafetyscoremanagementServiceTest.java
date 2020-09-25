package com.hkmc.behavioralpatternanalysis.safetyscoremanagement.service;


import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory;
import org.springframework.data.relational.repository.query.RelationalEntityInformation;

import com.hkmc.behavioralpatternanalysis.common.exception.GlobalCCSException;
import com.hkmc.behavioralpatternanalysis.common.util.JsonUtil;
import com.hkmc.behavioralpatternanalysis.safetyscoremanagement.model.BehaUbiSdhbInfoTemp;
import com.hkmc.behavioralpatternanalysis.safetyscoremanagement.service.impl.SafetyScoreManagementServiceImpl;

import ccs.core.db.repository.postgre.GenericPostgreRepository;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@ExtendWith(MockitoExtension.class)
public class SafetyscoremanagementServiceTest {
	
	@InjectMocks
	private SafetyScoreManagementServiceImpl safetyScoreManagementService;
	
	@Mock
	private R2dbcEntityOperations postgresqlEntityOperations;
	
	@Mock
	private R2dbcRepositoryFactory postgresqlRepositoryFactory;
	
	@Mock
	private R2dbcConverter r2dbcConverter;
	
	@Mock
	private RelationalEntityInformation<BehaUbiSdhbInfoTemp, Integer> entity;

	private String toDay = "";
	
	Map<String, Object> kafkaConsumerMap;
	
	
	@BeforeEach
	public void setup() throws GlobalCCSException {
		
		toDay = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		
		String consumerRecord = "";

		consumerRecord = "" + 
				"{\r\n" +
				"	\"serviceCode\": \"SVC01\"\r\n" +
				"	, \"subCode\": \"02\"\r\n" +
				"	, \"sendDate\": \"" + toDay + "\"\r\n" +
				"	, \"sendTotalPage\": \"1\"\r\n" +
				"	, \"sendCurrentPage\": \"1\"\r\n" +
				"	, \"sendTotalCount\": \"2\"\r\n" +
				"	, \"sendPageInCount\": \"2\"\r\n" +
				"	, \"listData\": [\r\n" +
				"		{\r\n" +
				"			\"ifDate\": \"" + toDay + "\"\r\n" +
				"			, \"nnidVin\": \"KMHG141DBKU_AC0A329905886\"\r\n" +
				"			, \"prjVehlCd\": \"HI\"\r\n" +
				"			, \"severeNormal\": \"SEVERE\"\r\n" +
				"			, \"cntNormal\": \"22\"\r\n" +
				"			, \"cntCaution\": \"4\"\r\n" +
				"			, \"cntSevere\": \"1\"\r\n" +
				"			, \"cntSeverePlus\": \"0\"\r\n" +
				"		}\r\n" +
				"		,{\r\n" +
				"			\"ifDate\": \"" + toDay + "\"\r\n" +
				"			, \"nnidVin\": \"KMHJ5815GHU_A1113B17C57A111\"\r\n" +
				"			, \"prjVehlCd\": \"RJ\"\r\n" +
				"			, \"severeNormal\": \"NORMAL\"\r\n" +
				"			, \"cntNormal\": \"13\"\r\n" +
				"			, \"cntCaution\": \"26\"\r\n" +
				"			, \"cntSevere\": \"1\"\r\n" +
				"			, \"cntSeverePlus\": \"0\"\r\n" +
				"		}\r\n" +
				"	]\r\n" +
				"}";
		
		kafkaConsumerMap = JsonUtil.str2map(consumerRecord);

	}
	
	
	@Test
	public void testSaveSafetyScoreManagement() throws GlobalCCSException{
		
		log.info("[[ testSaveSafetyScoreManagement Start ]]");

		when(safetyScoreManagementService.selectBehaUbiSdhbInfoTempCount()).thenReturn(2L);
//		when(postgresqlEntityOperations.count(Mockito.any(),Mockito.any())).thenReturn(Mono.just(2L));

		
		
		safetyScoreManagementService.saveSafetyScoreManagement(kafkaConsumerMap);
		
		
	}
	
	@SuppressWarnings("unused")
	@Test
	public void testSelectBehaUbiSdhbInfoTempCount() throws GlobalCCSException{
		
		log.info("[[ testSelectBehaUbiSdhbInfoTempCount Start ]]");

		RelationalEntityInformation<BehaUbiSdhbInfoTemp, Integer> entityTemp = postgresqlRepositoryFactory.getEntityInformation(BehaUbiSdhbInfoTemp.class);
		GenericPostgreRepository<BehaUbiSdhbInfoTemp, Integer> jpaRepositoryTemp = new GenericPostgreRepository<>(BehaUbiSdhbInfoTemp.class, entityTemp, postgresqlEntityOperations);

//		given(jpaRepositoryTemp.countAll()).willReturn(2L);
		
	}


}
