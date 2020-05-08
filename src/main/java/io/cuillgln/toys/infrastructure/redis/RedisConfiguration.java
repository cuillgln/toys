
package io.cuillgln.toys.infrastructure.redis;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;

@EnableConfigurationProperties(RedisProperties.class)
@Configuration
public class RedisConfiguration {

	private final RedisProperties redisProperties;

	public RedisConfiguration(RedisProperties redisProperties) {
		this.redisProperties = redisProperties;
	}

	@Bean
	public StatefulRedisConnection<String, String> redisConnection() {
		RedisClient redisClient = RedisClient.create(RedisURI.Builder.redis(redisProperties.getHost(),
						redisProperties.getPort()).build());
		return redisClient.connect();
	}

	@Bean
	public StringRedisTemplate redisTemplate() {
		StatefulRedisConnection<String, String> conn = redisConnection();
		if (redisProperties.getPassword() != null) {
			conn.sync().auth(redisProperties.getPassword());
		}
		return new StringRedisTemplate(conn);
	}
}
