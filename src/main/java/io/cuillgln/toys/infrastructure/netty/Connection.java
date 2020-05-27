
package io.cuillgln.toys.infrastructure.netty;

import java.io.Closeable;
import java.io.IOException;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

public class Connection implements Closeable {

	public static final AttributeKey<Connection> USER_DATA_KEY = AttributeKey.valueOf("user_data");

	private String host;
	private int port;
	private ConnectionId id;

	private Channel channel;
	private boolean authenticated;

	public Connection(String host, int port, ConnectionId id) {
		super();
		this.host = host;
		this.port = port;
		this.id = id;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public ConnectionId getId() {
		return id;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

	public void send(Message msg) {

		this.channel.writeAndFlush(msg);
	}

	@Override
	public void close() throws IOException {
		if (channel != null) {
			channel.close();
		}
	}
}
