
package io.cuillgln.toys.infrastructure.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class TCPChannelInitializer extends ChannelInitializer<Channel> {

	private final FinalChannelHandler finalChannelHandler;
	private final LoggingHandler loggingHandler;
	private final PBufEncoder encoder;

	public TCPChannelInitializer(Peer peer) {
		this.finalChannelHandler = new FinalChannelHandler(peer);
		this.loggingHandler = new LoggingHandler(LogLevel.DEBUG);
		this.encoder = new PBufEncoder();
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ch.pipeline().addLast("idle-event", new IdleStateHandler(180, 60, 0, TimeUnit.SECONDS))
						.addLast("logging", loggingHandler)
						.addLast("encoder", encoder)
						// .addLast("frame", new Frame())
						.addLast("decoder", new PBufDecoder())
						.addLast("final-handler", finalChannelHandler);

	}

	@Sharable
	static class FinalChannelHandler extends ChannelDuplexHandler {

		private Peer peer;

		public FinalChannelHandler(Peer peer) {
			this.peer = peer;
		}

		@Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			peer.release(ctx.channel());
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			if (msg instanceof PBuf) {
				peer.recvmsg(ctx.channel(), (PBuf) msg);
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
}
