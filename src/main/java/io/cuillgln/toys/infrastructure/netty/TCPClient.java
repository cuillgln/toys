
package io.cuillgln.toys.infrastructure.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TCPClient implements Closeable {

	private Logger log = LoggerFactory.getLogger(TCPClient.class);

	private EventLoopGroup group;
	private volatile boolean running;

	private String inetHost;
	private int inetPort;
	private ChannelInitializer<Channel> channelInitializer;

	private ExecutorService reconnectExecutor = Executors.newSingleThreadExecutor();
	private volatile int reconnectCounter = 0;

	public TCPClient(String inetHost, int inetPort, ChannelInitializer<Channel> channelInitializer) {
		try {
			this.inetHost = inetHost;
			this.inetPort = inetPort;
			this.channelInitializer = channelInitializer;
			this.group = new NioEventLoopGroup();
			doConnect();
			this.running = true;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void close() throws IOException {
		running = false;
		group.shutdownGracefully();
	}

	public boolean isRunning() {
		return running;
	}

	private void doConnect() throws IOException {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group).channel(NioSocketChannel.class)
						.option(ChannelOption.SO_KEEPALIVE, true)
						.handler(channelInitializer);
		ChannelFuture future = bootstrap.connect(inetHost, inetPort).awaitUninterruptibly();
		if (!future.isSuccess()) {
			future.channel().pipeline().deregister();
			throw new IOException(future.cause());
		} else {
			log.info("TCP client has connected to [{}:{}]", inetHost, inetPort);
		}

		future.channel().closeFuture().addListener(new CloseFutureListener());
	}

	private void reconnect() throws InterruptedException {
		try {
			doConnect();
		} catch (IOException e) {
			log.error("reconnect to [{}:{}] faild, try reconnect after 5 seconds {} time(s)",
							inetHost, inetPort, ++reconnectCounter);
			Thread.sleep(5000);
			reconnect();
		}
	}

	private class CloseFutureListener implements ChannelFutureListener {

		@Override
		public void operationComplete(ChannelFuture future) throws Exception {
			log.info("The connection to [{}] closed.", future.channel().remoteAddress().toString());
			future.channel().pipeline().deregister();
			if (running) {
				reconnectExecutor.execute(new Runnable() {

					@Override
					public void run() {
						try {
							log.error("the connection to [{}] closed unexpectly, try reconnect after 0.1 seconds",
											future.channel().remoteAddress().toString());
							reconnectCounter = 0;
							Thread.sleep(100);
							reconnect();
						} catch (InterruptedException e) {
							// just exit the reconnect thread
						}
					}
				});
			}
		}

	}

}
