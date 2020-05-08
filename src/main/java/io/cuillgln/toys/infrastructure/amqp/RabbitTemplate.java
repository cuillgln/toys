
package io.cuillgln.toys.infrastructure.amqp;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;

public class RabbitTemplate implements Closeable {

	private Logger log = LoggerFactory.getLogger(RabbitTemplate.class);

	private Channel channel;

	public RabbitTemplate(Channel channel) {
		this.channel = channel;
	}

	@Override
	public void close() throws IOException {
		log.info("rabbitmq producer connection channel closed");
		try {
			channel.close();
		} catch (TimeoutException e) {
			throw new IOException(e);
		}
	}

	public void send(String exchange, String routingKey, BasicProperties props, byte[] msg) throws IOException {
		this.channel.basicPublish(exchange, routingKey, props, msg);
	}

}
