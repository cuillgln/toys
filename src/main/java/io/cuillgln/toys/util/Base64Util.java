
package io.cuillgln.toys.util;

import java.util.Base64;

public class Base64Util {

	public static String encode(byte[] data) {
		return Base64.getEncoder().encodeToString(data);
	}

	public static byte[] decode(String src) {
		return Base64.getMimeDecoder().decode(src);
	}
}
