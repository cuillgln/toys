
package io.cuillgln.toys.infrastructure.amqp;

import io.cuillgln.toys.infrastructure.amqp.RabbitConsumer.MessageHandler;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

@EnableConfigurationProperties(RabbitProperties.class)
@Configuration
public class ConsumerConfiguration {

	private final RabbitProperties properties;

	public ConsumerConfiguration(RabbitProperties properties) {
		this.properties = properties;
	}

	@Bean
	public Connection connection() throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(properties.getHost());
		factory.setPort(properties.getPort());
		factory.setVirtualHost(properties.getVirtualHost());
		factory.setUsername(properties.getUsername());
		factory.setPassword(properties.getPassword());
		return factory.newConnection();
	}

	@Bean
	public RabbitConsumer consumer() throws IOException, TimeoutException {
		return new RabbitConsumer(connection().createChannel(), "queue", msgHandler());
	}

	public MessageHandler msgHandler() {
		return new MessageHandler() {

			@Override
			public void handle(byte[] msg) {
				// TODO Auto-generated method stub

			}
		};
	}
}
