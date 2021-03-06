
package io.cuillgln.toys.infrastructure.kafka.producer;

import java.io.Closeable;
import java.io.IOException;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class KafkaTemplate<K, V> implements Closeable {

	private Producer<K, V> producer;

	public KafkaTemplate(Producer<K, V> producer) {
		this.producer = producer;
	}

	public void send(String topic, K key, V value) {
		ProducerRecord<K, V> record = new ProducerRecord<>(topic, key, value);
		producer.send(record);
	}

	@Override
	public void close() throws IOException {
		producer.close();
	}
}
