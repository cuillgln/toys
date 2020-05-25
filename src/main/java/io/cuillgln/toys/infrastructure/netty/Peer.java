
package io.cuillgln.toys.infrastructure.netty;

import io.netty.channel.Channel;

public interface Peer {

	void recvmsg(Channel channel, PBuf msg);

	void recvevt(Channel channel, Object evt);

	void recvex(Channel channel, Throwable ex);

	void release(Channel channel);

}
