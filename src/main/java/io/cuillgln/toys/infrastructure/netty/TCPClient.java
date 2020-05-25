
package io.cuillgln.toys.infrastructure.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TCPClient implements Peer {

	private Logger log = LoggerFactory.getLogger(getClass());

	private EventLoopGroup group;
	private ChannelInitializer<Channel> channelInitializer;
	private List<MessageHandler> handlers;

	public TCPClient(List<MessageHandler> handlers) {
		this.handlers = handlers;
		this.group = new NioEventLoopGroup();
		this.channelInitializer = new TCPChannelInitializer(this);
	}

	public Connection connect(Connection conn) throws IOException {
		doConnect(conn.getHost(), conn.getPort(), conn);
		//  connection auth
		return conn;
	}

	private void doConnect(String host, int port, Connection conn) throws IOException {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group).channel(NioSocketChannel.class)
						.option(ChannelOption.SO_KEEPALIVE, true)
						.handler(channelInitializer)
						.attr(Connection.USER_DATA_KEY, conn);
		ChannelFuture future = bootstrap.connect(host, port).awaitUninterruptibly();
		if (!future.isSuccess()) {
			future.channel().pipeline().deregister();
			throw new IOException(future.cause());
		} else {
			conn.setChannel(future.channel());
			future.channel().closeFuture().addListener(new CloseFutureListener(conn));
			log.info("TCP client has connected to [{}]", future.channel().remoteAddress());
		}
	}

	public void shutdown() {
		this.group.shutdownGracefully();
	}

	@Override
	public void recvmsg(Channel channel, PBuf msg) {
		Connection conn = channel.attr(Connection.USER_DATA_KEY).get();
		Message resp = getHandler(msg).handle(msg);
		if (resp != null) {
			conn.send(resp);
		}
	}

	@Override
	public void recvevt(Channel channel, Object evt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void recvex(Channel channel, Throwable ex) {
		// TODO Auto-generated method stub

	}

	@Override
	public void release(Channel channel) {
		Connection session = channel.attr(Connection.USER_DATA_KEY).get();
		if (session != null) {
			session.setChannel(null);
		}

	}

	private MessageHandler getHandler(PBuf msg) {
		for (MessageHandler handler : handlers) {
			if (handler.canHandle(msg)) {
				return handler;
			}
		}
		throw new RuntimeException("handler not found");
	}

	private class CloseFutureListener implements ChannelFutureListener {

		private Connection conn;

		public CloseFutureListener(Connection conn) {
			this.conn = conn;
		}

		@Override
		public void operationComplete(ChannelFuture future) throws Exception {
			log.info("The connection to [{}] closed.", future.channel().remoteAddress());
			future.channel().pipeline().deregister();
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						log.error("the connection to [{}] closed, try reconnect after 0.1 seconds",
										future.channel().remoteAddress().toString());
						Thread.sleep(100);
						connect(conn);
					} catch (InterruptedException | IOException e) {
						// just exit the reconnect thread
					}
				}
			}).start();
		}
	}

}
