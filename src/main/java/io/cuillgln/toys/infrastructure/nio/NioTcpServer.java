
package io.cuillgln.toys.infrastructure.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioTcpServer implements Runnable {

	private final int port = 9000;

	public void start() throws IOException {
		Selector selector = Selector.open();
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);
		serverChannel.bind(new InetSocketAddress(port));
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		while (selector.select() > 0) {
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			while (it.hasNext()) {
				SelectionKey key = it.next();
				it.remove();

				if (key.isAcceptable()) {
					dealWithAcceptable(key);
				}
				if (key.isReadable()) {
					// TODO
				}

				if (key.isWritable()) {
					// TODO
				}
			}
		}

	}

	void dealWithAcceptable(SelectionKey key) throws IOException {
		ServerSocketChannel schannel = (ServerSocketChannel) key.channel();
		SocketChannel channel = schannel.accept();
		channel.configureBlocking(false);
		channel.register(key.selector(), SelectionKey.OP_READ | SelectionKey.OP_WRITE);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
