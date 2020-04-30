
package io.cuillgln.toys.infrastructure.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class ConsumerConfiguration {

	private KafkaConsumerProperties properties;

	public <K, V> Consumer<K, V> producer() {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.properties.getBootstrapServers());
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, this.properties.getGroupId());
		properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, this.properties.getEnableAutoCommit());
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, this.properties.getKeyDeserializer());
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, this.properties.getValueDeserializer());
		return new KafkaConsumer<K, V>(properties);
	}
}
