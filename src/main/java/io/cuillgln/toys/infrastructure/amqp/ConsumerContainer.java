
package io.cuillgln.toys.infrastructure.amqp;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.*;
import com.rabbitmq.client.AMQP.BasicProperties;

public class ConsumerContainer {

	private Logger log = LoggerFactory.getLogger(ConsumerContainer.class);

	private MessageHandler messageHandler;
	private CachedConnectionFactory factory;
	private volatile boolean running;
	private Channel channel;
	private String queueName;
	private String exchange;
	private String routingKey;

	public ConsumerContainer(String queueName, MessageHandler msgHandler, CachedConnectionFactory factory) {
		this.factory = factory;
		this.messageHandler = msgHandler;
		this.queueName = queueName;
	}

	public ConsumerContainer(String exchange, String routingKey, MessageHandler msgHandler,
					CachedConnectionFactory factory) {
		this.factory = factory;
		this.messageHandler = msgHandler;
		this.exchange = exchange;
		this.routingKey = routingKey;
	}

	public void start() {
		try {
			doConnect();
			running = true;
		} catch (IOException | TimeoutException e) {
			log.error("connect to rabbitmq broker failed, try to connect again after 5 seconds...");
			start();
		}
	}

	public void stop() {
		try {
			running = false;
			channel.close();
		} catch (IOException | TimeoutException e) {
			log.error("Exception when close rabbitmq ConsumerClient", e);
		}
	}

	public boolean isRunning() {
		return running;
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
