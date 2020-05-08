
package io.cuillgln.toys.infrastructure.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface MessageHandler<K, V> {

	void handle(ConsumerRecord<K, V> record);
}
