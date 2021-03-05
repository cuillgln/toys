
package io.cuillgln.toys.infrastructure.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class NioTcpClient implements Runnable {

	private SocketChannel channel;

	public NioTcpClient(String host, int port) throws IOException {
		channel = SocketChannel.open();
		channel.connect(new InetSocketAddress(host, port));
	}

	@Override
	public void run() {
		// TODO 解包
		// channel.read

	}
}
