
package io.cuillgln.toys.infrastructure.kafka;

import java.util.Collection;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManualAssignConsumerContainer<K, V> {

	private Logger log = LoggerFactory.getLogger(ManualAssignConsumerContainer.class);

	private ConsumerFactory<K, V> factory;
	private Collection<TopicPartition> topicPartitions;
	private MessageHandler<K, V> messageHandler;

	private volatile boolean running;
	private Consumer<K, V> consumer;

	public ManualAssignConsumerContainer(Collection<TopicPartition> topicPartitions, MessageHandler<K, V> messageHandler,
					ConsumerFactory<K, V> factory) {
		this.topicPartitions = topicPartitions;
		this.messageHandler = messageHandler;
		this.factory = factory;
	}

	public void start() {
		this.consumer = factory.createConsumer();
		this.consumer.assign(topicPartitions);
		this.running = true;
		Thread consumerThread = new Thread(new ConsumerRunner());
		consumerThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread t, Throwable e) {
				log.error("The kafka consumer thread exit with exception", e);
			}
		});
		consumerThread.start();
	}

	public void stop() {
		running = false;
		consumer.wakeup();
		consumer.close();
	}

	public boolean isRunning() {
		return running;
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
}
