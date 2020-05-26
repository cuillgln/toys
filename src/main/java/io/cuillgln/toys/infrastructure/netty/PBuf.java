
package io.cuillgln.toys.infrastructure.netty;

public class PBuf {

	private byte[] buffer;

	public PBuf(byte[] buffer) {
		this.buffer = buffer;
	}

	public byte[] toByteArray() {
		return buffer;
	}

}
