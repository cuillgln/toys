
package io.cuillgln.toys.infrastructure.netty;

public interface MessageHandler {

	boolean canHandle(PBuf msg);

	Message handle(PBuf msg);
}
