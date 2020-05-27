
package io.cuillgln.toys.infrastructure.netty;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConnectionManager implements Closeable {

	private Map<ConnectionId, Connection> connectionMap = new HashMap<>();

	public Connection getConnection(ConnectionId id) {
		return connectionMap.get(id);
	}

	@Override
	public void close() throws IOException {
		for (ConnectionId id : connectionMap.keySet()) {
			Connection conn = connectionMap.get(id);
			conn.close();
		}
	}
}
