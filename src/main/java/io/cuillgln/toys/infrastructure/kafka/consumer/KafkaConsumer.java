
package io.cuillgln.toys.infrastructure.kafka.consumer;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaConsumer<K, V> implements Closeable {

	private Logger log = LoggerFactory.getLogger(getClass());

	private MessageHandler<K, V> messageHandler;
	private Consumer<K, V> consumer;

	public KafkaConsumer(String[] topics, MessageHandler<K, V> messageHandler, Consumer<K, V> consumer) {
		this.messageHandler = messageHandler;
		consumer.subscribe(Arrays.asList(topics));
		Thread consumerThread = new Thread(new ConsumerRunner());
		consumerThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread t, Throwable e) {
				log.error("The kafka consumer thread exit with exception", e);
			}
		});
		consumerThread.start();
		this.consumer = consumer;
	}

	public KafkaConsumer(Collection<TopicPartition> topicPartitions, MessageHandler<K, V> messageHandler,
					Consumer<K, V> consumer) {
		this.messageHandler = messageHandler;
		consumer.assign(topicPartitions);
		Thread consumerThread = new Thread(new ConsumerRunner());
		consumerThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread t, Throwable e) {
				log.error("The kafka consumer thread exit with exception", e);
			}
		});
		consumerThread.start();
		this.consumer = consumer;
	}

	@Override
	public void close() throws IOException {
		consumer.wakeup();
		consumer.close();
	}

	private class ConsumerRunner implements Runnable {

		@Override
		public void run() {
			try {
				while (true) {
					ConsumerRecords<K, V> consumerRecords = consumer.poll(Long.MAX_VALUE);
					if (consumerRecords != null && !consumerRecords.isEmpty()) {
						for (ConsumerRecord<K, V> record : consumerRecords) {
							messageHandler.handle(record);
						}
					}
				}
			} catch (WakeupException e) {
				log.info("The kafka consumer thread received wakeup SIGNAL, and exit");
			} finally {
				log.info("The kafka consumer thread exit");
			}
		}
	}

	public static interface MessageHandler<K, V> {

		void handle(ConsumerRecord<K, V> record);
	}
}
