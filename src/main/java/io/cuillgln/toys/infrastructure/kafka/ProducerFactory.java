
package io.cuillgln.toys.infrastructure.kafka;

import java.util.Map;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

public class ProducerFactory<K, V> {
	
	private Map<String, Object> configs;

	Producer<K, V> createProducer() {
		return new KafkaProducer<K, V>(configs);
	}

}
