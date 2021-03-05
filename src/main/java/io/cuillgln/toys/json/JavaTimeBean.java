
package io.cuillgln.toys.json;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public class JavaTimeBean {

	private String name = "test";

	@JsonFormat(shape = Shape.STRING, timezone = "GMT+0100", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private Date dateNow = new Date();

	@JsonFormat(shape = Shape.STRING, timezone = "GMT+0100", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private Timestamp timestampNow = new Timestamp(new Date().getTime());

	@JsonFormat(shape = Shape.STRING, timezone = "GMT+0100", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private java.sql.Date sqlDateNow = new java.sql.Date(new Date().getTime());

	// java.time

	@JsonFormat(shape = Shape.STRING, timezone = "GMT+0800", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private Instant instantNow = Instant.now();

	// LocalDateTime没有timezone信息
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "GMT+0100")
	private LocalDateTime localNowTime = LocalDateTime.now();
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = JsonFormat.DEFAULT_TIMEZONE)
	private LocalDateTime zlocalNowTime = LocalDateTime.now(ZoneId.of("+0100"));

	@JsonFormat(shape = Shape.STRING, timezone = JsonFormat.DEFAULT_TIMEZONE, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private OffsetDateTime offsetNow = OffsetDateTime.now();
	@JsonFormat(shape = Shape.STRING, timezone = JsonFormat.DEFAULT_TIMEZONE, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private OffsetDateTime zoffsetNow = OffsetDateTime.now(ZoneId.of("+0100"));

	@JsonFormat(shape = Shape.STRING, timezone = JsonFormat.DEFAULT_TIMEZONE, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private ZonedDateTime zoneNow = ZonedDateTime.now();
	@JsonFormat(shape = Shape.STRING, timezone = "GMT+0200", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private ZonedDateTime zzoneNow = ZonedDateTime.now(ZoneId.of("+0100"));

	public String getName() {
		return name;
	}

	public Date getDateNow() {
		return dateNow;
	}

	public Timestamp getTimestampNow() {
		return timestampNow;
	}

	public java.sql.Date getSqlDateNow() {
		return sqlDateNow;
	}

	public Instant getInstantNow() {
		return instantNow;
	}

	public LocalDateTime getLocalNowTime() {
		return localNowTime;
	}

	public LocalDateTime getZlocalNowTime() {
		return zlocalNowTime;
	}

	public OffsetDateTime getOffsetNow() {
		return offsetNow;
	}

	public OffsetDateTime getZoffsetNow() {
		return zoffsetNow;
	}

	public ZonedDateTime getZoneNow() {
		return zoneNow;
	}

	public ZonedDateTime getZzoneNow() {
		return zzoneNow;
	}

	@Override
	public String toString() {
		return "JavaTimeBean [name=" + name + ", dateNow=" + dateNow + ", timestampNow=" + timestampNow
				+ ", sqlDateNow=" + sqlDateNow + ", instantNow=" + instantNow + ", localNowTime=" + localNowTime
				+ ", zlocalNowTime=" + zlocalNowTime + ", offsetNow=" + offsetNow + ", zoffsetNow=" + zoffsetNow
				+ ", zoneNow=" + zoneNow + ", zzoneNow=" + zzoneNow + "]";
	}

}
