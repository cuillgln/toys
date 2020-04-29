
package io.cuillgln.toys.infrastructure.amqp;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.*;

/**
 * AMQP model
 * exchange (direct/fanout/topic/headers)
 * queue
 * queue-bind(routing-key)
 * 
 * @author cuillgln
 *
 */
public class CachedConnectionFactory {

	private ConnectionFactory factory;

	private CachedConnection cachedConnection;

	public CachedConnectionFactory(String host, int port, String virtualHost, String username, String password) {
		this.factory = new ConnectionFactory();
		factory.setHost(host);
		factory.setPort(port);
		factory.setVirtualHost(virtualHost);
		factory.setUsername(username);
		factory.setPassword(password);
	}

	public Connection newConnection() throws IOException, TimeoutException {
		if (cachedConnection == null || !cachedConnection.isOpen()) {
			cachedConnection = new CachedConnection(factory.newConnection());
		}
		return cachedConnection;
	}

	public void close() {
		try {
			if (cachedConnection != null && cachedConnection.isOpen()) {
				cachedConnection.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			cachedConnection = null;
		}
	}

	static class CachedConnection implements Connection {

		private Connection conn;

		public CachedConnection(Connection conn) {
			this.conn = conn;
		}

		public void addShutdownListener(ShutdownListener listener) {
			conn.addShutdownListener(listener);
		}

		public void removeShutdownListener(ShutdownListener listener) {
			conn.removeShutdownListener(listener);
		}

		public ShutdownSignalException getCloseReason() {
			return conn.getCloseReason();
		}

		public void notifyListeners() {
			conn.notifyListeners();
		}

		public boolean isOpen() {
			return conn.isOpen();
		}

		public InetAddress getAddress() {
			return conn.getAddress();
		}

		public int getPort() {
			return conn.getPort();
		}

		public int getChannelMax() {
			return conn.getChannelMax();
		}

		public int getFrameMax() {
			return conn.getFrameMax();
		}

		public int getHeartbeat() {
			return conn.getHeartbeat();
		}

		public Map<String, Object> getClientProperties() {
			return conn.getClientProperties();
		}

		public String getClientProvidedName() {
			return conn.getClientProvidedName();
		}

		public Map<String, Object> getServerProperties() {
			return conn.getServerProperties();
		}

		public Channel createChannel() throws IOException {
			return conn.createChannel();
		}

		public Channel createChannel(int channelNumber) throws IOException {
			return conn.createChannel(channelNumber);
		}

		public void close() throws IOException {
			conn.close();
		}

		public void close(int closeCode, String closeMessage) throws IOException {
			conn.close(closeCode, closeMessage);
		}

		public void close(int timeout) throws IOException {
			conn.close(timeout);
		}

		public void close(int closeCode, String closeMessage, int timeout) throws IOException {
			conn.close(closeCode, closeMessage, timeout);
		}

		public void abort() {
			conn.abort();
		}

		public void abort(int closeCode, String closeMessage) {
			conn.abort(closeCode, closeMessage);
		}

		public void abort(int timeout) {
			conn.abort(timeout);
		}

		public void abort(int closeCode, String closeMessage, int timeout) {
			conn.abort(closeCode, closeMessage, timeout);
		}

		public void addBlockedListener(BlockedListener listener) {
			conn.addBlockedListener(listener);
		}

		public BlockedListener addBlockedListener(BlockedCallback blockedCallback, UnblockedCallback unblockedCallback) {
			return conn.addBlockedListener(blockedCallback, unblockedCallback);
		}

		public boolean removeBlockedListener(BlockedListener listener) {
			return conn.removeBlockedListener(listener);
		}

		public void clearBlockedListeners() {
			conn.clearBlockedListeners();
		}

		public ExceptionHandler getExceptionHandler() {
			return conn.getExceptionHandler();
		}

		public String getId() {
			return conn.getId();
		}

		public void setId(String id) {
			conn.setId(id);
		}

	}

}
