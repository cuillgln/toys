
package io.cuillgln.lock;

public class Lock {

	public static void main(String[] args) {
		long count = (long) Math.pow(2, 32);
		int remain = (int) (count % 8);
		int bytes = (int) (count / 8) + remain == 0 ? 0 : 1;
		byte[] bits = new byte[bytes];
		
		for (int i = 0; i < 1000; i++) {
			int byteIndex = i / 8;
			int bitIndex = i % 8;
			bits[byteIndex] = (byte) (bits[byteIndex] | (1 << 7 - bitIndex));
			System.out.println();
		}
		

	}
}
