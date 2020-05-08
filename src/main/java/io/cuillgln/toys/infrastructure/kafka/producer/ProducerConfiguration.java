
package io.cuillgln.toys.infrastructure.kafka.producer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(ProducerProperties.class)
@Configuration
public class ProducerConfiguration {

	private final ProducerProperties properties;

	public ProducerConfiguration(ProducerProperties properties) {
		this.properties = properties;
	}

	@Bean
	public <K, V> Producer<K, V> producer() {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.properties.getBootstrapServers());
		properties.put(ProducerConfig.CLIENT_ID_CONFIG, this.properties.getClientId());
		properties.put(ProducerConfig.ACKS_CONFIG, this.properties.getAcks());
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, this.properties.getKeySerializer());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, this.properties.getValueSerializer());
		return new KafkaProducer<K, V>(properties);
	}

	@Bean
	public KafkaTemplate<byte[], byte[]> template() {
		return new KafkaTemplate<>(producer());
	}
}
