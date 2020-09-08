package com.hkmc.behavioralpatternanalysis.config.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.hkmc.templateservice.hello.model.TemplateProduceDTO;

import lombok.extern.slf4j.Slf4j;

//@Component
//@EnableKafka
@Slf4j
public class KafkaConsumer {
	
////	private HelloServiceImpl helloServiceImpl;
//	
//	private static final ObjectMapper mapper =  new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).setSerializationInclusion(Include.NON_NULL);
//	
//	
////	public KafkaConsumer(final HelloServiceImpl helloServiceImpl) {
////        this.helloServiceImpl = helloServiceImpl;
////    }
//	
//	@KafkaListener(topics = "${template.topic}")
//	public void subscribe(ConsumerRecord<String, String> consumeRecord) {
//		
//		try {
//			TemplateProduceDTO templateProduceDTO = mapper.readValue(consumeRecord.value(), TemplateProduceDTO.class);
//			log.debug("------------------------subscribe : {}", templateProduceDTO.toString());
//			
//		} catch (Exception e) {
//			log.debug("Exception subscribe : {}",consumeRecord.value());
//		}
//	}

}
