
package io.cuillgln.toys.infrastructure.amqp;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.*;

public class RabbitConsumer implements Closeable {

	private Logger log = LoggerFactory.getLogger(RabbitConsumer.class);

	private MessageHandler messageHandler;
	private Channel channel;
	private String exchange;
	private String routingKey;
	private String queueName;

	public RabbitConsumer(Channel channel, String queueName, MessageHandler msgHandler) {
		try {
			this.messageHandler = msgHandler;
			this.queueName = queueName;
			doSubscribe();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public RabbitConsumer(Channel channel, String exchange, String routingKey, MessageHandler msgHandler) {
		try {
			this.messageHandler = msgHandler;
			this.exchange = exchange;
			this.routingKey = routingKey;
			doSubscribe();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void close() throws IOException {
		log.info("rabbitmq consumer connection channel closed");
		try {
			channel.close();
		} catch (TimeoutException e) {
			throw new IOException(e);
		}
	}

	private void doSubscribe() throws IOException {
		if (this.queueName == null) {
			channel.exchangeDeclarePassive(exchange);
			this.queueName = channel.queueDeclare().getQueue();
			channel.queueBind(queueName, exchange, routingKey);
		}
		// autoAck
		channel.basicConsume(queueName, true, new SubscribeConsumer(channel));
		log.info("rabbitmq consumer connection channel established");
	}

	private class SubscribeConsumer extends DefaultConsumer {

		public SubscribeConsumer(Channel channel) {
			super(channel);
		}

		@Override
		public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
						throws IOException {
			messageHandler.handle(body);
		}
	}

	public static interface MessageHandler {

		public void handle(byte[] msg);
	}

}
