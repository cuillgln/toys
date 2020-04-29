
package io.cuillgln.toys.infrastructure.amqp;

public class RabbitConfiguration {

	private RabbitConfigurationProperties rabbitProperties;

	public CachedConnectionFactory factory() {
		return new CachedConnectionFactory(rabbitProperties.getHost(),
						rabbitProperties.getPort(),
						rabbitProperties.getVirtualHost(),
						rabbitProperties.getUsername(),
						rabbitProperties.getPassword());
	}

	public Producer producer() {
		return new Producer(factory());
	}

	public AMQPTemplate amqpTemplate() {
		return new AMQPTemplate(producer());
	}
}
