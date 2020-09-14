package com.hkmc.behavioralpatternanalysis.config.kafka;

import java.util.Map;

import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hkmc.behavioralpatternanalysis.common.util.CommonUtil;
import com.hkmc.behavioralpatternanalysis.common.util.JsonUtil;
import com.hkmc.behavioralpatternanalysis.intelligencevehicleinformation.service.impl.IntelligenceVehicleInformationServiceImpl;

import lombok.extern.slf4j.Slf4j;

//@Component
//@EnableKafka
//@Slf4j
public class KafkaConsumer {
	
//	private IntelligenceVehicleInformationServiceImpl intelligenceVehicleInformationService;
//	
//    public KafkaConsumer(IntelligenceVehicleInformationServiceImpl intelligenceVehicleInformationService) {
//        this.intelligenceVehicleInformationService = intelligenceVehicleInformationService;
//    }
//
//	@KafkaListener(topics = "${template.topic_intelligencevehicleinformation}", groupId="${templateservice.consumer.group_intelligencevehicleinformation}")
//	public void subscribe(String consumerRecord) throws JsonProcessingException {
//		
//		log.info("Received message: " + consumerRecord);
//
//		try {
//			Map<String, Object> consumerRecordMap = JsonUtil.str2map(consumerRecord);
//		
//			// 전송일자와 현재일자가 동일할 경우와 데이터가 존재할 경우에만 실행
//			if(CommonUtil.getFormattedDate(0).equals(consumerRecordMap.get("sendDate")) 
//					&& (consumerRecordMap.get("listData") != null && !("".equals(consumerRecordMap.get("listData"))))) {
//
////				intelligenceVehicleInformationService.saveIntelligenceVehicleInformation(consumerRecordMap);
//				log.debug("------------------------subscribe : {}", consumerRecordMap.toString());
//
//			}
//			
//
//			
//		} catch (Exception e) {
//			log.debug("Exception subscribe : {}", consumerRecord);
//			log.error("[KafkaConsumer.subscribe] Ex : ", e);
//		}
//	}

}
