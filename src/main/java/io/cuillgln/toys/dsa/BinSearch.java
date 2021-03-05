
package io.cuillgln.toys.dsa;

public class BinSearch {

	public static int binsearch(int[] array, int value, int start, int stop) {
		if (start > stop) {
			return -1;
		}
		if (value == array[start]) {
			return start;
		}
		if (value == array[stop]) {
			return stop;
		}
		if (value < array[start]) {
			return -1;
		}
		if (value > array[stop]) {
			return -1;
		}
		int mid = (start + stop) / 2;
		if (value == array[mid]) {
			return mid;
		} else if (value > array[mid]) {
			return binsearch(array, value, start, mid - 1);
		} else {
			return binsearch(array, value, mid + 1, stop);
		}
	}

	public static int binsearch2(int[] array, int value) {
		int start = 0;
		int stop = array.length - 1;

		while (start <= stop) {
			int mid = (start + stop) / 2;
			int midValue = array[mid];
			if (value == midValue) {
				return mid;
			} else if (value < midValue) {
				stop = mid - 1;
			} else {
				start = mid + 1;
			}
		}
		return -1;
	}

}
