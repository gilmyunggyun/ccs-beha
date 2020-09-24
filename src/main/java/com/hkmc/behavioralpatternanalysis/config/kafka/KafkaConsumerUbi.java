package com.hkmc.behavioralpatternanalysis.config.kafka;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.hkmc.behavioralpatternanalysis.common.exception.GlobalCCSException;
import com.hkmc.behavioralpatternanalysis.common.util.JsonUtil;
import com.hkmc.behavioralpatternanalysis.safetyscoremanagement.service.SafetyScoreManagementService;

@Component
@EnableKafka
public class KafkaConsumerUbi {
	
	private SafetyScoreManagementService safetyScoreManagementService;
	
    public KafkaConsumerUbi(SafetyScoreManagementService safetyScoreManagementService) {

        this.safetyScoreManagementService = safetyScoreManagementService;

    }

	// 소비자 행통패턴 분석 - UBI 안전운행 점수
	@KafkaListener(topics = "${template.topic-behavioralpatternanalysis-safetyscoremanagement}", groupId="${templateservice.consumer.group-behavioralpatternanalysis}")
	public void safetyscoremanagementSubscribe(String consumerRecord) throws GlobalCCSException {

		Map<String, Object> consumerRecordMap = JsonUtil.str2map(consumerRecord);
	
		// 전송일자와 현재일자가 동일할 경우와 데이터가 존재할 경우에만 실행
		if(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")).equals(consumerRecordMap.get("sendDate")) 
						&& (consumerRecordMap.get("listData") != null && !("".equals(consumerRecordMap.get("listData"))))) {

			safetyScoreManagementService.saveSafetyScoreManagement(consumerRecordMap);

		}
			
	}
}
