
package io.cuillgln.toys.infrastructure.netty;

import io.netty.channel.Channel;

public interface L1Peer {

	void recvmsg(Channel channel, byte[] msg);

	void recvevt(Channel channel, Object evt);

	void recvex(Channel channel, Throwable ex);

	void add(Channel channel);

	void remove(Channel channel);

}
