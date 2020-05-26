
package io.cuillgln.toys.infrastructure.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TCPServer implements Peer {

	private Logger log = LoggerFactory.getLogger(TCPServer.class);

	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	private ChannelInitializer<Channel> channelInitializer;

	private ServerProperties config;
	private ConnectionManager connectionManager;
	private List<MessageHandler> handlers;

	public TCPServer(ServerProperties config, ConnectionManager connectionManager, List<MessageHandler> handlers) {
		try {
			this.config = config;
			this.connectionManager = connectionManager;
			this.handlers = handlers;

			this.bossGroup = new NioEventLoopGroup();
			this.workerGroup = new NioEventLoopGroup();
			this.channelInitializer = new TCPChannelInitializer(this);
			doBind(config.getPort());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void shutdown() {
		log.info("TCP server shutdown");
		workerGroup.shutdownGracefully();
		bossGroup.shutdownGracefully();
	}

	private void doBind(int inetPort) throws IOException {
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

	@Override
	public void recvmsg(Channel channel, PBuf msg) {
		Connection conn = channel.attr(Connection.USER_DATA_KEY).get();
		if (false) { // auth msg
			//			Connection conn = get(msg);
			//			conn.setChannel(channel);
			//			channel.attr(Connection.USER_DATA_KEY).set(conn);
		} else {
			//			Message resp = new Object();
			//			conn.send(resp);
		}
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
		Connection conn = channel.attr(Connection.USER_DATA_KEY).get();
		if (conn != null) {
			conn.setChannel(null);
			conn.setAutenticated(false);
			channel.attr(Connection.USER_DATA_KEY).set(null);
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
}
