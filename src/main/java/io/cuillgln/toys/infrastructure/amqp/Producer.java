
package io.cuillgln.toys.infrastructure.amqp;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Producer implements Closeable {

	private Logger log = LoggerFactory.getLogger(Producer.class);

	private CachedConnectionFactory factory;
	private Channel channel;

	public Producer(CachedConnectionFactory factory) {
		this.factory = factory;
		try {
			doConnect();
		} catch (IOException | TimeoutException e) {
			throw new RuntimeException(e);
		}
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

	private void doConnect() throws IOException, TimeoutException {
		Connection conn = factory.newConnection();
		Channel ch = conn.createChannel();
		this.channel = ch;
		log.info("rabbitmq producer connection channel established");
	}
}
