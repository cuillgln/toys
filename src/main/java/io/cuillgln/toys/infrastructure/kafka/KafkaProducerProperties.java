
package io.cuillgln.toys.infrastructure.kafka;

import java.util.List;

import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaProducerProperties {

	private String acks;

	private List<String> bootstrapServers;

	private String clientId;

	private Class<?> keySerializer = StringSerializer.class;

	private Class<?> valueSerializer = StringSerializer.class;

	private Integer retries;

	private String transactionIdPrefix;

	public String getAcks() {
		return acks;
	}

	public void setAcks(String acks) {
		this.acks = acks;
	}

	public List<String> getBootstrapServers() {
		return bootstrapServers;
	}

	public void setBootstrapServers(List<String> bootstrapServers) {
		this.bootstrapServers = bootstrapServers;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public Class<?> getKeySerializer() {
		return keySerializer;
	}

	public void setKeySerializer(Class<?> keySerializer) {
		this.keySerializer = keySerializer;
	}

	public Class<?> getValueSerializer() {
		return valueSerializer;
	}

	public void setValueSerializer(Class<?> valueSerializer) {
		this.valueSerializer = valueSerializer;
	}

	public Integer getRetries() {
		return retries;
	}

	public void setRetries(Integer retries) {
		this.retries = retries;
	}

	public String getTransactionIdPrefix() {
		return transactionIdPrefix;
	}

	public void setTransactionIdPrefix(String transactionIdPrefix) {
		this.transactionIdPrefix = transactionIdPrefix;
	}

}
