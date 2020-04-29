
package io.cuillgln.toys.infrastructure.amqp;

public interface MessageHandler {

	public void handle(byte[] msg);
}
