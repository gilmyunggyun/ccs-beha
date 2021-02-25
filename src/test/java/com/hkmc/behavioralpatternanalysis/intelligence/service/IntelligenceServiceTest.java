package com.hkmc.behavioralpatternanalysis.intelligence.service;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import com.hkmc.behavioralpatternanalysis.common.Const;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory;

import com.hkmc.behavioralpatternanalysis.common.exception.GlobalCCSException;
import com.hkmc.behavioralpatternanalysis.common.util.JsonUtil;
import com.hkmc.behavioralpatternanalysis.intelligence.model.BehaSvdvHist;
import com.hkmc.behavioralpatternanalysis.intelligence.service.impl.IntelligenceServiceImpl;

import ccs.core.db.repository.postgre.GenericPostgreRepository;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@ExtendWith(MockitoExtension.class)
public class IntelligenceServiceTest {
	
	@InjectMocks
	private IntelligenceServiceImpl intelligenceVehicleInformationService;
	
	@Mock
	private R2dbcEntityOperations postgresqlEntityOperations;
	
	@Mock
	private R2dbcRepositoryFactory postgresqlRepositoryFactory;
	
	@Mock
	private R2dbcConverter r2dbcConverter;
	
	@Mock
	private GenericPostgreRepository<BehaSvdvHist, Integer> jpaRepository;
	
	private String toDay = "";
	
	List<Map<String, Object>> kafkaListData = null;
	
	private Map<String, Object> kafkaConsumerMap;
	
	BehaSvdvHist behaSvdvHist;
	
	@BeforeEach
	public void setup() throws GlobalCCSException {
		
		toDay = LocalDate.now().format(DateTimeFormatter.ofPattern(Const.YYYYMMDD));
	
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
	
	
//	@Test
//	public void testSaveIntelligenceVehicleInformation() throws GlobalCCSException{
//
//		log.info("[[ testGetCustomerBehaviorUbiInfo Start ]]");
//
//
//		intelligenceVehicleInformationService.saveIntelligenceVehicleInformation(kafkaConsumerMap);
//
//
//
//
//
////
////		intelligenceVehicleInformationService.saveIntelligenceVehicleInformation(kafkaConsumerMap);
////
////		List<BehaSvdvHist> behaSvdvHistList = new ArrayList<>();
////
////		// listData 담기
////		List<Map<String, Object>> kafkaListData = (List<Map<String, Object>>) kafkaConsumerMap.get("listData");
////
////		long sendTotalCount = (long) kafkaListData.get(0).get("sendTotalCount");
////
//////		when(intelligenceVehicleInformationService.selectBehaSvdvHistCount(toDay)).thenReturn((long) kafkaConsumerMap.get("sendTotalCount"));
////		given(jpaRepository.reactiveCountByCriteria(Criteria.where("ifDate").is(toDay)).block()).willReturn(sendTotalCount);
//
//	}

}
