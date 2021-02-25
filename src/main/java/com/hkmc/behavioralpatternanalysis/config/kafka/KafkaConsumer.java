package com.hkmc.behavioralpatternanalysis.config.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.hkmc.behavioralpatternanalysis.intelligence.service.IntelligenceService;

@Component
@EnableKafka
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {
    private final IntelligenceService intelligenceService;

    // 소비자 행통패턴 분석 - 지능형 차량관리
    @KafkaListener(topics = "${kafka.topic.intelligence}")
    public void intelligenceSubscribe(ConsumerRecord<String, String> consumeRecord) {
        try {
            intelligenceService.saveIntelligence(consumeRecord);
        } catch (Exception e) {
            log.error("Exception subscribe : {}", consumeRecord.value());
        }
    }
}
