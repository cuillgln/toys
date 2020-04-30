
package io.cuillgln.toys.infrastructure.amqp;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.*;

public class ConsumerContainer implements Closeable {

	private Logger log = LoggerFactory.getLogger(ConsumerContainer.class);

	private MessageHandler messageHandler;
	private CachedConnectionFactory factory;
	private Channel channel;
	private String queueName;
	private String exchange;
	private String routingKey;

	public ConsumerContainer(String queueName, MessageHandler msgHandler, CachedConnectionFactory factory) {
		try {
			this.factory = factory;
			this.messageHandler = msgHandler;
			this.queueName = queueName;
			doConnect();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ConsumerContainer(String exchange, String routingKey, MessageHandler msgHandler,
					CachedConnectionFactory factory) {
		try {
			this.factory = factory;
			this.messageHandler = msgHandler;
			this.exchange = exchange;
			this.routingKey = routingKey;
			doConnect();
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

	private void doConnect() throws IOException, TimeoutException {
		Connection conn = factory.newConnection();
		Channel ch = conn.createChannel();
		if (this.queueName == null) {
			ch.exchangeDeclarePassive(exchange);
			this.queueName = ch.queueDeclare().getQueue();
			ch.queueBind(queueName, exchange, routingKey);
		}
		// autoAck
		ch.basicConsume(queueName, true, new SubscribeConsumer(ch));
		this.channel = ch;
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

}
