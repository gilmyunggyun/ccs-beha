package com.hkmc.behavioralpatternanalysis.config.kafka;

import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hkmc.behavioralpatternanalysis.common.exception.GlobalCCSException;
import com.hkmc.behavioralpatternanalysis.intelligencevehicleinformation.service.IntelligenceVehicleInformationService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@ExtendWith(MockitoExtension.class)
public class KafkaConsumerItlCarTest {

	@InjectMocks
    KafkaConsumer kafkaConsumerItlCar;
	
	@Mock
	IntelligenceVehicleInformationService intelligenceVehicleInformationService;
	
	private String toDay = "";
	
	private String consumerRecord = "";
	
	private String consumerRecordErr = "";
	
	@BeforeEach
	public void setup() throws GlobalCCSException {

		toDay = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")); 
		
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
		
		consumerRecordErr = "" + 
				"{\r\n" +
				"	\"serviceCode\": \"SVC01\"\r\n" +
				"	, \"subCode\": \"02\"\r\n" +
				"	, \"sendDate\": \"" + toDay + "\"\r\n" +
				"	, \"sendTotalPage\": \"1\"\r\n" +
				"	, \"sendCurrentPage\": \"1\"\r\n" +
				"	, \"sendTotalCount\": \"1\"\r\n" +
				"	, \"sendPageInCount\": \"1\"\r\n" +
				"}";
		
	}
	
}
