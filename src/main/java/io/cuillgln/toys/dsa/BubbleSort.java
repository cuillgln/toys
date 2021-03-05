
package io.cuillgln.toys.dsa;

public class BubbleSort {

	public static int[] sort(int[] array) {
		for (int i = array.length - 1; i >= 0; i--) {
			for (int j = 0; j < i; j++) {
				if (array[j] > array[j + 1]) {
					int tmp = array[j];
					array[j] = array[j + 1];
					array[j + 1] = tmp;
				}
			}
		}
		return array;
	}
	
	public void sort2(int[] array) {
		
		for (int i = 0; i < array.length; i++) {
			
		}
	}
}
