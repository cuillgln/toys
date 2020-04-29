
package io.cuillgln.toys.infrastructure.util;

import io.netty.buffer.ByteBufUtil;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ByteArrayUtil {

	public static final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5',
					'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String toHexString(final byte[] data) {
		final int l = data.length;
		final char[] out = new char[l << 1];
		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS_LOWER[0x0F & data[i]];
		}
		return new String(out);
	}

	public static byte[] concat(byte[] data, byte[] data2) {
		byte[] dst = new byte[data.length + data2.length];
		System.arraycopy(data, 0, dst, 0, data.length);
		System.arraycopy(data2, 0, dst, data.length, data2.length);
		return dst;
	}

	public static byte[] copyOfRange(byte[] data, int index, int size) {
		return Arrays.copyOfRange(data, index, index + size);
	}

	public static void copy(byte[] src, int srcIndex, byte[] dest, int destIndex, int size) {
		System.arraycopy(src, srcIndex, dest, destIndex, size);
	}

	public static byte getByte(byte[] data, int index) {
		assert index <= data.length - 1;
		return data[index];
	}

	public static void setByte(byte[] data, int index, int value) {
		assert index <= data.length - 1;
		data[index] = (byte) value;
	}

	public static int getWord(byte[] data, int index) {
		assert index <= data.length - 2;
		return ((data[index] & 0xff) << 8) | (data[index + 1] & 0xff);
	}

	public static void setWord(byte[] data, int index, int value) {
		assert index <= data.length - 2;
		data[index] = (byte) (value >>> 8);
		data[index + 1] = (byte) value;
	}

	public static long getDWord(byte[] data, int index) {
		assert index <= data.length - 2;
		return ((data[index] & 0xff) << 24) | ((data[index + 1] & 0xff) << 16)
						| ((data[index + 2] & 0xff) << 8) | (data[index + 3] & 0xff);
	}

	public static void setDWord(byte[] data, int index, long value) {
		assert index <= data.length - 2;
		data[index] = (byte) (value >>> 24);
		data[index + 1] = (byte) (value >>> 16);
		data[index + 2] = (byte) (value >>> 8);
		data[index + 3] = (byte) value;
	}

	/**
	 * 小端表示法
	 */
	public static int getWordLE(byte[] data, int index) {
		assert index <= data.length - 2;
		return (data[index] & 0xff) | (data[index + 1] & 0xff) << 8;
	}

	/**
	 * 字节流字符串
	 * 
	 * @param bytes
	 * @return
	 */
	public static String hexDump(byte[] bytes) {
		return ByteBufUtil.hexDump(bytes);
	}

	public static String toUTF8String(byte[] bytes) {
		return new String(bytes, StandardCharsets.UTF_8).trim();
	}

	public static byte[] toFixedBytes(String gbkString, int fixedLength) {
		ByteBuffer buffer = ByteBuffer.allocate(fixedLength);
		buffer.put(gbkString.getBytes(StandardCharsets.UTF_8));
		return buffer.array();
	}

	public static String toGBKString(byte[] bytes) {
		return new String(bytes, Charset.forName("GBK")).trim();
	}

	public static String toGBKString(byte[] bytes, int index, int size) {
		bytes = copyOfRange(bytes, index, size);
		return new String(bytes, Charset.forName("GBK")).trim();
	}

	public static byte[] gbkStringToBytes(String gbkString) {
		return gbkString.getBytes(Charset.forName("GBK"));
	}

	public static byte[] gbkStringToFixedBytes(String gbkString, int fixedLength) {
		ByteBuffer buffer = ByteBuffer.allocate(fixedLength);
		buffer.put(gbkString.getBytes(Charset.forName("GBK")));
		return buffer.array();
	}
}
