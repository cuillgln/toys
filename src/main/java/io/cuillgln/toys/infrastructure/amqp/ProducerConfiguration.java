
package io.cuillgln.toys.infrastructure.amqp;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

@EnableConfigurationProperties(RabbitProperties.class)
@Configuration
public class ProducerConfiguration {

	private final RabbitProperties properties;

	public ProducerConfiguration(RabbitProperties properties) {
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
	public RabbitTemplate rabbitTemplate() throws IOException, TimeoutException {
		return new RabbitTemplate(connection().createChannel());
	}

}
