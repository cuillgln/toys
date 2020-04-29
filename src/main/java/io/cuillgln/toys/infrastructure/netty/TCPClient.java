
package io.cuillgln.toys.infrastructure.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TCPClient {

	private Logger log = LoggerFactory.getLogger(TCPClient.class);

	private EventLoopGroup group = new NioEventLoopGroup();
	private volatile boolean running;

	private String inetHost;
	private int inetPort;
	private ChannelInitializer<Channel> channelInitializer;

	private ExecutorService reconnectExecutor = Executors.newSingleThreadExecutor();
	private volatile int reconnectCounter = 0;

	public TCPClient(String inetHost, int inetPort, ChannelInitializer<Channel> channelInitializer) {
		this.inetHost = inetHost;
		this.inetPort = inetPort;
		this.channelInitializer = channelInitializer;
	}

	public void start() {
		if (running) {
			throw new IllegalStateException("The client is running");
		}
		if (group.isShutdown()) {
			throw new IllegalStateException("The client has been stopped, cann't be start again."
							+ " You should renew an instance and call start().");
		}

		try {
			doConnect();
			running = true;
			log.info("TCP client has connected to [{}:{}]", inetHost, inetPort);
		} catch (IOException e) {
			log.error("connect to [{}:{}] faild, try reconnect after 5 seconds {} time(s)",
							inetHost, inetPort, ++reconnectCounter);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
			}
			start();
		}
	}

	public void stop() {
		if (!running) {
			return;
		}
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
		}

		future.channel().closeFuture().addListener(new CloseFutureListener());
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
						log.error("the connection to [{}] closed unexpectly, try reconnect after 5 seconds {} time(s)",
										future.channel().remoteAddress().toString(), ++reconnectCounter);
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
						}
						reconnectCounter = 0;
						running = false;
						start();
					}
				});
			}
		}

	}

}
