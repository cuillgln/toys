
package io.cuillgln.toys.infrastructure.netty;

public class PBuf {

	private boolean recvif;
	private byte[] buffer;

	public PBuf(byte[] buffer) {
		this(true, buffer);
	}

	public PBuf(boolean recvif, byte[] buffer) {
		this.recvif = recvif;
		this.buffer = buffer;
	}

	public byte[] toByteArray() {
		return buffer;
	}
	
}
