
package io.cuillgln.toys.dsa;

import static org.junit.Assert.*;

import org.junit.Test;

public class BinSearchTest {

	@Test
	public void test1() {
		int[] arry = new int[] { 1 };
		int pos = BinSearch.binsearch(arry, 1, 0, arry.length - 1);
		assertEquals(0, pos);
		int pos2 = BinSearch.binsearch2(arry, 1);
		assertEquals(0, pos2);
	}

	@Test
	public void test2() {
		int[] arry = new int[] { 1, 2 };
		int pos = BinSearch.binsearch(arry, 2, 0, arry.length - 1);
		assertEquals(1, pos);
		int pos2 = BinSearch.binsearch2(arry, 2);
		assertEquals(1, pos2);
	}
	
	@Test
	public void test3() {
		int[] arry = new int[] { 1, 2, 3 };
		int pos = BinSearch.binsearch(arry, 2, 0, arry.length - 1);
		assertEquals(1, pos);
		int pos2 = BinSearch.binsearch2(arry, 2);
		assertEquals(1, pos2);
	}
	
	@Test
	public void test4() {
		int[] arry = new int[] { 1, 2, 3, 4 };
		int pos = BinSearch.binsearch(arry, 4, 0, arry.length - 1);
		assertEquals(3, pos);
		int pos2 = BinSearch.binsearch2(arry, 4);
		assertEquals(3, pos2);
	}
}
