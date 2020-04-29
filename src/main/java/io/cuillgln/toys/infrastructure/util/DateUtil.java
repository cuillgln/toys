
package io.cuillgln.toys.infrastructure.util;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.FastDateFormat;

public class DateUtil {

	public static final FastDateFormat MILLIS = FastDateFormat.getInstance("yyyyMMddHHmmssSSS");

	public static String nowMillis() {
		return MILLIS.format(System.currentTimeMillis());
	}

	public static Date parseMillis(String millis) {
		try {
			return MILLIS.parse(millis);
		} catch (ParseException e) {
		}
		return new Date();
	}
}
