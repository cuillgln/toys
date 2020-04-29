
package io.cuillgln.toys.infrastructure.amqp;

import java.io.IOException;

import com.rabbitmq.client.AMQP.BasicProperties;

public class AMQPTemplate {

	private Producer producer;

	public AMQPTemplate(Producer producer) {
		this.producer = producer;
	}

	public void send(String exchange, String routingKey, BasicProperties props, byte[] msg) throws IOException {
		this.producer.send(exchange, routingKey, props, msg);
	}
}
