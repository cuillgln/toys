
package io.cuillgln.toys.infrastructure.oio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class TcpServer implements Runnable {

	private ServerSocket server;

	private ExecutorService workerExecutor;

	public TcpServer(int port) throws IOException {
		server = new ServerSocket(port);
	}

	@Override
	public void run() {
		try {
			while (true) {
				Socket socket = server.accept();
				workerExecutor.execute(new Worker(socket));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stop() throws IOException {
		server.close();
	}

	static class Worker implements Runnable {

		private Socket socket;

		public Worker(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				InputStream input = socket.getInputStream();
				OutputStream output = socket.getOutputStream();
				while (true) {
					// TODO
					byte[] buffer = new byte[1024];
					input.read(buffer);
					// echo
					output.write(buffer);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}
}
