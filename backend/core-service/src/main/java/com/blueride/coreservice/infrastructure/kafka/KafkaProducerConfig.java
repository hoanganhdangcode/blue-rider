package com.blueride.coreservice.infrastructure.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaProducerConfig {

	@Bean(destroyMethod = "close")
	Producer<String, String> kafkaProducer(@Value("${kafka.bootstrap-servers}") String bootstrapServers) {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 5_000);
		props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 3_000);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, "core-service");
		return new KafkaProducer<>(props);
	}
}
