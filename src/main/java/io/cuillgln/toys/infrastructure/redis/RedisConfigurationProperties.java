
package io.cuillgln.toys.infrastructure.redis;

public class RedisConfigurationProperties {

	private int database = 0;

	private String host = "localhost";

	private String password;

	private int port = 6379;

	public int getDatabase() {
		return database;
	}

	public void setDatabase(int database) {
		this.database = database;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
