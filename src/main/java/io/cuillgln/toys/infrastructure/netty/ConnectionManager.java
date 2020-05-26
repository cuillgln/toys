
package io.cuillgln.toys.infrastructure.netty;

import java.util.HashMap;
import java.util.Map;

public class ConnectionManager {

	private Map<ConnectionId, Connection> connectionMap = new HashMap<>();

	public Connection getConnection(ConnectionId id) {
		return connectionMap.get(id);
	}
}
