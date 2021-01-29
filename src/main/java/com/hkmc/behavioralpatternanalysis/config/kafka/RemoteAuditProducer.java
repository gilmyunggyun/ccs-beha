package com.hkmc.behavioralpatternanalysis.config.kafka;

import com.hkmc.behavioralpatternanalysis.common.model.RemoteAuditProduceDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j

public class RemoteAuditProducer {

    @Value("${kafka.topic.remotecontrol-audit}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, RemoteAuditProduceDTO> kafkaTemplate;

    public void send(final RemoteAuditProduceDTO produceRecod) {
        log.debug("------------------------------------produce : {}", produceRecod);
        kafkaTemplate.send(topic, produceRecod);
    }

}
