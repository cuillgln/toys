
package io.cuillgln.toys.infrastructure.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;

public class RedisConfiguration {

	private RedisConfigurationProperties redisProperties;

	public StatefulRedisConnection<String, String> redisConnection() {
		RedisClient redisClient = RedisClient.create(
						RedisURI.Builder.redis(redisProperties.getHost(), redisProperties.getPort()).build());
		return redisClient.connect();
	}

	public StringRedisTemplate redisTemplate() {
		StatefulRedisConnection<String, String> conn = redisConnection();
		if (redisProperties.getPassword() != null) {
			conn.sync().auth(redisProperties.getPassword());
		}
		return new StringRedisTemplate(conn);
	}
}
