
package io.cuillgln.toys.infrastructure.util;

import java.util.Calendar;
import java.util.Date;

public final class BcdCodeUtil {

	public static int bcdToInteger(byte oneByte) {
		int low = oneByte & 0x0F;
		int high = (oneByte >>> 4) & 0x0F;
		return high * 10 + low;
	}

	public static byte intToBcd(int digit) {
		int high = digit / 10;
		int low = digit % 10;
		return (byte) (high << 4 | low);
	}

	/**
	 * BCD[6] 转时间
	 * 
	 * @param bcdBytes
	 * @return
	 */
	public static Date bcd6ToDateTime(byte[] bcdBytes) {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int yearPrefix = year / 100 * 100;
		c.clear();
		c.set(Calendar.YEAR, yearPrefix + bcdToInteger(bcdBytes[0]));
		c.set(Calendar.MONTH, bcdToInteger(bcdBytes[1]) - 1);
		c.set(Calendar.DATE, bcdToInteger(bcdBytes[2]));
		c.set(Calendar.HOUR_OF_DAY, bcdToInteger(bcdBytes[3]));
		c.set(Calendar.MINUTE, bcdToInteger(bcdBytes[4]));
		c.set(Calendar.SECOND, bcdToInteger(bcdBytes[5]));
		return c.getTime();
	}

	public static byte[] dateTimeToBcd6(Date time) {
		Calendar c = Calendar.getInstance();
		c.clear();
		c.setTime(time);
		byte year = intToBcd(c.get(Calendar.YEAR) % 100);
		byte month = intToBcd(c.get(Calendar.MONTH) + 1);
		byte date = intToBcd(c.get(Calendar.DATE));
		byte hour = intToBcd(c.get(Calendar.HOUR_OF_DAY));
		byte min = intToBcd(c.get(Calendar.MINUTE));
		byte second = intToBcd(c.get(Calendar.SECOND));
		return new byte[] { year, month, date, hour, min, second };
	}

	public static Date bcd3ToDate(byte[] bcdBytes) {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int yearPrefix = year / 100 * 100;
		c.clear();
		c.set(Calendar.YEAR, yearPrefix + bcdToInteger(bcdBytes[0]));
		c.set(Calendar.MONTH, bcdToInteger(bcdBytes[1]) - 1);
		c.set(Calendar.DATE, bcdToInteger(bcdBytes[2]));
		return c.getTime();
	}

	public static byte[] dateToBcd3(Date time) {
		Calendar c = Calendar.getInstance();
		c.clear();
		c.setTime(time);
		byte yearHigh = intToBcd(c.get(Calendar.YEAR) / 100);
		byte year = intToBcd(c.get(Calendar.YEAR) % 100);
		byte month = intToBcd(c.get(Calendar.MONTH) + 1);
		byte date = intToBcd(c.get(Calendar.DATE));
		return new byte[] { yearHigh, year, month, date };
	}

	/**
	 * BCD[8] 转时间
	 * 
	 * @param bcdBytes
	 * @return
	 */
	public static Date bcd8ToDateTime(byte[] bcdBytes) {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int yearPrefix = year / 100 * 100;
		c.clear();
		c.set(Calendar.YEAR, yearPrefix + bcdToInteger(bcdBytes[0]));
		c.set(Calendar.MONTH, bcdToInteger(bcdBytes[1]) - 1);
		c.set(Calendar.DATE, bcdToInteger(bcdBytes[2]));
		c.set(Calendar.HOUR_OF_DAY, bcdToInteger(bcdBytes[3]));
		c.set(Calendar.MINUTE, bcdToInteger(bcdBytes[4]));
		c.set(Calendar.SECOND, bcdToInteger(bcdBytes[5]));
		c.set(Calendar.MILLISECOND, bcdToInteger(bcdBytes[6]) * 10 + (bcdToInteger(bcdBytes[7]) >>> 4));
		return c.getTime();
	}

	public static byte[] dateTimeToBcd8(Date time) {
		Calendar c = Calendar.getInstance();
		c.clear();
		c.setTime(time);
		byte year = intToBcd(c.get(Calendar.YEAR) % 100);
		byte month = intToBcd(c.get(Calendar.MONTH) + 1);
		byte date = intToBcd(c.get(Calendar.DATE));
		byte hour = intToBcd(c.get(Calendar.HOUR_OF_DAY));
		byte min = intToBcd(c.get(Calendar.MINUTE));
		byte second = intToBcd(c.get(Calendar.SECOND));
		byte milli = intToBcd(c.get(Calendar.MILLISECOND) / 10);
		byte milliLSB = intToBcd((c.get(Calendar.MILLISECOND) % 10) << 4);
		return new byte[] { year, month, date, hour, min, second, milli, milliLSB };
	}
}
