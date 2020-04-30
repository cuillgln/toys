
package io.cuillgln.toys.infrastructure.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.io.IOException;

/**
 * UDP 也可以connect，这样client只能是一client对一server。
 * 如果没有connect可以一client对多server，在DatagramPacket中指定server的 socketAddress。
 * 
 * @author cuillgln
 *
 */
public class UDPClient {

	private EventLoopGroup workerGroup;
	private String inetHost;
	private int inetPort;
	private ChannelInitializer<Channel> channelInitializer;

	public UDPClient(String inetHost, int inetPort) {
		try {
			this.workerGroup = new NioEventLoopGroup();
			this.inetHost = inetHost;
			this.inetPort = inetPort;
			doConnect();
		} catch (IOException e) {
			shutdown();
			throw new RuntimeException(e);
		}
	}

	public void shutdown() {
		workerGroup.shutdownGracefully();
	}

	private void doConnect() throws IOException {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(workerGroup).channel(NioDatagramChannel.class)
						.option(ChannelOption.SO_BROADCAST, true)
						.handler(channelInitializer);

		ChannelFuture future = bootstrap.connect(inetHost, inetPort).awaitUninterruptibly();
		if (!future.isSuccess()) {
			throw new IOException(future.cause());
		}
	}
}
