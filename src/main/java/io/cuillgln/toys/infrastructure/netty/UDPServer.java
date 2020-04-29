
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

	private EventLoopGroup workerGroup = new NioEventLoopGroup();
	private volatile boolean running;
	private int inetPort;
	private ChannelInitializer<Channel> channelInitializer;

	public UDPServer(int inetPort) {
		this.inetPort = inetPort;
	}

	public void start() {
		try {
			doBind();
			running = true;
		} catch (IOException e) {
			log.error("Exception when bind to UDP port [{}], stop the server", inetPort, e);
			stop();
		}
	}

	public void stop() {
		this.running = false;
		workerGroup.shutdownGracefully();
	}

	public boolean isRunning() {
		return running;
	}

	private void doBind() throws IOException {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(workerGroup).channel(NioDatagramChannel.class)
						.option(ChannelOption.SO_BROADCAST, true)
						.handler(channelInitializer);

		ChannelFuture future = bootstrap.bind(inetPort).awaitUninterruptibly();
		if (!future.isSuccess()) {
			throw new IOException(future.cause());
		}
	}
}
