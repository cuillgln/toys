
package io.cuillgln.toys.infrastructure.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UDPServer {

	private Logger log = LoggerFactory.getLogger(UDPServer.class);

	private EventLoopGroup workerGroup;
	private int inetPort;
	private ChannelInitializer<Channel> channelInitializer;

	public UDPServer(int inetPort) {
		try {
			this.inetPort = inetPort;
			this.workerGroup = new NioEventLoopGroup();
			doBind();
		} catch (IOException e) {
			shutdown();
			throw new RuntimeException(e);
		}
	}

	public void shutdown() {
		log.info("UDP server shutdown");
		workerGroup.shutdownGracefully();
	}

	private void doBind() throws IOException {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(workerGroup).channel(NioDatagramChannel.class)
						.option(ChannelOption.SO_BROADCAST, true)
						.handler(channelInitializer);

		ChannelFuture future = bootstrap.bind(inetPort).awaitUninterruptibly();
		if (!future.isSuccess()) {
			throw new IOException(future.cause());
		} else {
			log.info("UDP server is running on port {}", inetPort);
		}
	}
}
