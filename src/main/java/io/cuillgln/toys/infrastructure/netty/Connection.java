
package io.cuillgln.toys.infrastructure.netty;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

public interface Connection {

	public static final AttributeKey<Connection> USER_DATA_KEY = AttributeKey.newInstance("user_data");

	public String getHost();

	public int getPort();

	public void setChannel(Channel channel);
	
	public void setAutenticated(boolean flag);
	
	public boolean isAuthenticated();

	public void send(Message msg);
}
