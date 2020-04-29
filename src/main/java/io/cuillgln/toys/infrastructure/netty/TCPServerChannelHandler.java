
package io.cuillgln.toys.infrastructure.netty;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

@Sharable
public class TCPServerChannelHandler extends ChannelDuplexHandler {

	private L1Peer peer;

	public TCPServerChannelHandler(L1Peer peer) {
		this.peer = peer;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		peer.add(ctx.channel());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		peer.remove(ctx.channel());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			if (msg instanceof byte[]) {
				peer.recvmsg(ctx.channel(), (byte[]) msg);
			}
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		peer.recvevt(ctx.channel(), evt);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		peer.recvex(ctx.channel(), cause);
	}

}
