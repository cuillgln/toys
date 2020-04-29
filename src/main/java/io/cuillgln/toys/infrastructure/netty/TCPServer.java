
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

	private EventLoopGroup bossGroup = new NioEventLoopGroup();
	private EventLoopGroup workerGroup = new NioEventLoopGroup();
	private volatile boolean running;
	private int inetPort;
	private ChannelInitializer<Channel> channelInitializer;

	public TCPServer(int inetPort, ChannelInitializer<Channel> channelInitializer) {
		this.inetPort = inetPort;
		this.channelInitializer = channelInitializer;
	}

	public void start() {
		if (running) {
			throw new IllegalStateException("The server is running");
		}
		if (workerGroup.isShutdown() || bossGroup.isShutdown()) {
			throw new IllegalStateException("The server has been stopped, cann't be start again."
							+ " You should renew an instance and call start().");
		}
		try {
			doBind();
			running = true;
			log.info("TCP server is running on port {}", inetPort);
		} catch (IOException e) {
			log.error("Exception when bind to TCP port [{}], stop the server", inetPort, e);
			stop();
		}
	}

	public void stop() {
		this.running = false;
		workerGroup.shutdownGracefully();
		bossGroup.shutdownGracefully();
	}

	public boolean isRunning() {
		return running;
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
		}
	}
}
