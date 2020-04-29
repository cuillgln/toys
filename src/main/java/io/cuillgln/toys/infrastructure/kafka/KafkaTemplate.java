
package io.cuillgln.toys.infrastructure.kafka;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class KafkaTemplate<K, V> {

	private Producer<K, V> producer;

	public KafkaTemplate(ProducerFactory<K, V> producerFactory) {
		this.producer = producerFactory.createProducer();
	}

	public void send(String topic, K key, V value) {
		ProducerRecord<K, V> record = new ProducerRecord<>(topic, key, value);
		producer.send(record);
	}
}
