
package io.cuillgln.file;

public class Resort {

	public static void main(String[] args) {
		char[] chars = new char[] { 'c', 'u', 'i', 'l', 'l', 'g', 'l', 'n' };
		for (int i = chars.length - 1; i >= 0; i--) {
			for (int k = 0; k < chars.length; k++) {
				char[] nchars = new char[] {};
			}
			for (int j = 0; j < i; j++) {

			}
		}
	}

	private static long arrange(long n) {
		if (n == 1) {
			return 1;
		} else {
			return n * arrange(n - 1);
		}
	}
}
