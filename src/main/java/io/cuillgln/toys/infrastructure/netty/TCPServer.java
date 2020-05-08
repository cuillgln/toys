
package io.cuillgln.toys.infrastructure.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TCPServer {

	private Logger log = LoggerFactory.getLogger(TCPServer.class);

	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	private int inetPort;
	private ChannelInitializer<Channel> channelInitializer;

	public TCPServer(int inetPort, ChannelInitializer<Channel> channelInitializer) {
		try {
			this.inetPort = inetPort;
			this.channelInitializer = channelInitializer;
			this.bossGroup = new NioEventLoopGroup();
			this.workerGroup = new NioEventLoopGroup();
			doBind();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void shutdown() {
		log.info("TCP server shutdown");
		workerGroup.shutdownGracefully();
		bossGroup.shutdownGracefully();
	}

	private void doBind() throws IOException {
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
						.childHandler(channelInitializer)
						.option(ChannelOption.SO_BACKLOG, 128)
						.childOption(ChannelOption.SO_KEEPALIVE, true);
		ChannelFuture future = bootstrap.bind(inetPort).awaitUninterruptibly();
		if (!future.isSuccess()) {
			throw new IOException(future.cause());
		} else {
			log.info("TCP server is running on port {}", inetPort);
		}
	}
}
