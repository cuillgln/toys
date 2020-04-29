
package io.cuillgln.toys.infrastructure.kafka;

import java.time.Duration;
import java.util.List;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaConfigurationProperties {

	public static class Consumer {

		private Duration autoCommitInterval;

		private String autoOffsetReset;

		private List<String> bootstrapServers;

		private String clientId;

		private Boolean enableAutoCommit;

		private Duration fetchMaxWait;

		private String groupId;

		private Duration heartbeatInterval;

		private Class<?> keyDeserializer = StringDeserializer.class;

		private Class<?> valueDeserializer = StringDeserializer.class;

		private Integer maxPollRecords;

		public Duration getAutoCommitInterval() {
			return autoCommitInterval;
		}

		public void setAutoCommitInterval(Duration autoCommitInterval) {
			this.autoCommitInterval = autoCommitInterval;
		}

		public String getAutoOffsetReset() {
			return autoOffsetReset;
		}

		public void setAutoOffsetReset(String autoOffsetReset) {
			this.autoOffsetReset = autoOffsetReset;
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

		public Boolean getEnableAutoCommit() {
			return enableAutoCommit;
		}

		public void setEnableAutoCommit(Boolean enableAutoCommit) {
			this.enableAutoCommit = enableAutoCommit;
		}

		public Duration getFetchMaxWait() {
			return fetchMaxWait;
		}

		public void setFetchMaxWait(Duration fetchMaxWait) {
			this.fetchMaxWait = fetchMaxWait;
		}

		public String getGroupId() {
			return groupId;
		}

		public void setGroupId(String groupId) {
			this.groupId = groupId;
		}

		public Duration getHeartbeatInterval() {
			return heartbeatInterval;
		}

		public void setHeartbeatInterval(Duration heartbeatInterval) {
			this.heartbeatInterval = heartbeatInterval;
		}

		public Class<?> getKeyDeserializer() {
			return keyDeserializer;
		}

		public void setKeyDeserializer(Class<?> keyDeserializer) {
			this.keyDeserializer = keyDeserializer;
		}

		public Class<?> getValueDeserializer() {
			return valueDeserializer;
		}

		public void setValueDeserializer(Class<?> valueDeserializer) {
			this.valueDeserializer = valueDeserializer;
		}

		public Integer getMaxPollRecords() {
			return maxPollRecords;
		}

		public void setMaxPollRecords(Integer maxPollRecords) {
			this.maxPollRecords = maxPollRecords;
		}

	}

	public static class Producer {

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
}
