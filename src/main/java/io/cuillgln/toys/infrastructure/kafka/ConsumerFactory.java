
package io.cuillgln.toys.infrastructure.kafka;

import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class ConsumerFactory<K, V> {

	private Map<String, Object> consumerConfigs;

	public Consumer<K, V> createConsumer() {
		return createKafkaConsumer(consumerConfigs);
	}

	private KafkaConsumer<K, V> createKafkaConsumer(Map<String, Object> configs) {
		return new KafkaConsumer<>(configs);
	}

}
