
package io.cuillgln.toys.infrastructure.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;

public class RedisConfiguration {

	private RedisConfigurationProperties redisProperties;

	public RedisClient redisClient() {
		return RedisClient.create(RedisURI.Builder.redis(redisProperties.getHost(), redisProperties.getPort()).build());
	}

	public StringRedisTemplate redisTemplate() {
		StatefulRedisConnection<String, String> conn = redisClient().connect();
		if (redisProperties.getPassword() != null) {
			conn.sync().auth(redisProperties.getPassword());
			conn.async().auth(redisProperties.getPassword());
		}
		return new StringRedisTemplate(conn.sync(), conn.async());
	}
}
