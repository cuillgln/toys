
package io.cuillgln.toys.infrastructure.oio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TcpClient implements Runnable {

	private Socket socket;

	public TcpClient(String host, int port) throws IOException {
		this.socket = new Socket(host, port);
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
