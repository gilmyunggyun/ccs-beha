//package com.hkmc.behavioralpatternanalysis.config.kafka;
//
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.hkmc.behavioralpatternanalysis.common.exception.GlobalCCSException;
//import com.hkmc.behavioralpatternanalysis.safetyscoremanagement.service.SafetyScoreManagementService;
//
//import lombok.extern.slf4j.Slf4j;
//
//
//@Slf4j
//@ExtendWith(MockitoExtension.class)
//public class KafkaConsumerUbiTest {
//
//	@InjectMocks
//	KafkaConsumerUbi kafkaConsumerUbi;
//
//	@Mock
//	SafetyScoreManagementService safetyScoreManagementService;
//
//	private String toDay = "";
//
//	private String consumerRecord = "";
//
//	private String consumerRecordErr = "";
//
//	@BeforeEach
//	public void setup() throws GlobalCCSException {
//
//		toDay = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
//
//		consumerRecord = "" +
//				"{\r\n" +
//				"	\"serviceCode\": \"SVC01\"\r\n" +
//				"	, \"subCode\": \"01\"\r\n" +
//				"	, \"sendDate\": \"" + toDay + "\"\r\n" +
//				"	, \"sendTotalPage\": \"1\"\r\n" +
//				"	, \"sendCurrentPage\": \"1\"\r\n" +
//				"	, \"sendTotalCount\": \"2\"\r\n" +
//				"	, \"sendPageInCount\": \"2\"\r\n" +
//				"	, \"listData\": [\r\n" +
//				"		{\r\n" +
//				"			\"nnidVin\": \"KMHG141DBKU_AC0A329905886\"\r\n" +
//				"			, \"scoreDate\": \"20200901\"\r\n" +
//				"			, \"safetyDrvScore\": \"23\"\r\n" +
//				"			, \"insDiscountYn\": \"N\"\r\n" +
//				"			, \"brstAccGrade\": \"WARNING\"\r\n" +
//				"			, \"brstDecGrade\": \"NORMAL\"\r\n" +
//				"			, \"nightDrvGrade\": \"EXCELLENCE\"\r\n" +
//				"			, \"rangeDrvDist\": \"333\"\r\n" +
//				"		}\r\n" +
//				"		,{\r\n" +
//				"			\"nnidVin\": \"KMHJ5815GHU_A1113B17C57A111\"\r\n" +
//				"			, \"scoreDate\": \"20200902\"\r\n" +
//				"			, \"safetyDrvScore\": \"33\"\r\n" +
//				"			, \"insDiscountYn\": \"N\"\r\n" +
//				"			, \"brstAccGrade\": \"NORMAL\"\r\n" +
//				"			, \"brstDecGrade\": \"NORMAL\"\r\n" +
//				"			, \"nightDrvGrade\": \"EXCELLENCE\"\r\n" +
//				"			, \"rangeDrvDist\": \"1334\"\r\n" +
//				"		}\r\n" +
//				"	]\r\n" +
//				"}";
//
//		consumerRecordErr = "" +
//				"{\r\n" +
//				"	\"serviceCode\": \"SVC01\"\r\n" +
//				"	, \"subCode\": \"02\"\r\n" +
//				"	, \"sendDate\": \"" + toDay + "\"\r\n" +
//				"	, \"sendTotalPage\": \"1\"\r\n" +
//				"	, \"sendCurrentPage\": \"1\"\r\n" +
//				"	, \"sendTotalCount\": \"1\"\r\n" +
//				"	, \"sendPageInCount\": \"1\"\r\n" +
//				"}";
//
//	}
//
//
//	@Test
//	public void testIntelligencevehicleinformationSubscribe() throws JsonProcessingException {
//
//		log.info("[[ testIntelligencevehicleinformationSubscribe Start ]]");
//
//		kafkaConsumerUbi.safetyscoremanagementSubscribe(consumerRecord);
//
//		verify(safetyScoreManagementService, times(1)).saveSafetyScoreManagement((Mockito.anyMap()));
//	}
//
//	@Test
//	public void testIntelligencevehicleinformationSubscribeErr() throws JsonProcessingException {
//
//		log.info("[[ testIntelligencevehicleinformationSubscribeErr Start ]]");
//
//		kafkaConsumerUbi.safetyscoremanagementSubscribe(consumerRecordErr);
//
//		verify(safetyScoreManagementService, times(0)).saveSafetyScoreManagement((Mockito.anyMap()));
//	}
//
//}
