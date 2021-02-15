package com.hkmc.behavioralpatternanalysis.config.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.hkmc.behavioralpatternanalysis.intelligencevehicleinformation.service.IntelligenceVehicleInformationService;

@Component
@EnableKafka
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerItlCar {
    private final IntelligenceVehicleInformationService intelligenceVehicleInformationService;

    // 소비자 행통패턴 분석 - 지능형 차량관리
    @KafkaListener(topics = "${kafka.topic.intelligence}")
    public void intelligencevehicleinformationSubscribe(ConsumerRecord<String, String> consumeRecord) {
        try {
            intelligenceVehicleInformationService.saveIntelligenceVehicleInformation(consumeRecord);
        } catch (Exception e) {
            log.error("Exception subscribe : {}", consumeRecord.value());
        }
    }
}
