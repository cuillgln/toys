
package io.cuillgln.toys.infrastructure.redis;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

public class StringRedisTemplate implements Closeable {

	private StatefulRedisConnection<String, String> connection;
	private RedisCommands<String, String> syncCommands;
	private RedisAsyncCommands<String, String> asyncCommands;

	public StringRedisTemplate(StatefulRedisConnection<String, String> conn) {
		this.syncCommands = conn.sync();
		this.asyncCommands = conn.async();
		this.connection = conn;
	}

	public void expire(String key, long seconds) {
		this.asyncCommands.expire(key, seconds);
	}

	public void del(String key) {
		this.asyncCommands.del(key);
	}

	public void incr(String key) {
		this.asyncCommands.incr(key);
	}

	public void set(String key, String value) {
		this.asyncCommands.set(key, value);
	}

	public String get(String key) {
		return this.syncCommands.get(key);
	}

	public boolean exists(String key) {
		return this.syncCommands.exists(key) > 0;
	}

	public void hset(String key, String field, String value) {
		this.asyncCommands.hset(key, field, value);
	}

	public void hmset(String key, Map<String, String> values) {
		this.asyncCommands.hmset(key, values);
	}

	public String hget(String key, String field) {
		return this.syncCommands.hget(key, field);
	}

	public Map<String, String> hgetall(String key) {
		return this.syncCommands.hgetall(key);
	}

	public void hincr(String key, String field) {
		this.asyncCommands.hincrby(key, field, 1);
	}

	@Override
	public void close() throws IOException {
		this.connection.close();
	}
}
