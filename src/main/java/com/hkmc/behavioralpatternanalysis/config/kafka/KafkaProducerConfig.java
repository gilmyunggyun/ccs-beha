package com.hkmc.behavioralpatternanalysis.config.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

//@Configuration
public class KafkaProducerConfig<K, V> {
	
	@Value("${spring.kafka.bootstrap-servers}")
	private String kafkaBroker;

	@Bean
    public Map<String, Object> producerProps(){
        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBroker);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return properties;
    }
	
	@Bean
    public ProducerFactory<K, V> producerFactory(){
        return new DefaultKafkaProducerFactory<>(producerProps());
    }
	
	@Bean
    public KafkaTemplate<K, V> kafkaTemplate(){
		return new KafkaTemplate<>(producerFactory());
	}
	
	@Bean
	public KafkaProducer kafkaProducer() {
		return new KafkaProducer();
	}
	
}
