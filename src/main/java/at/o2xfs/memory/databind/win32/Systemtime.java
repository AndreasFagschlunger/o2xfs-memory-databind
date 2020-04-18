package at.o2xfs.memory.databind.win32;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import at.o2xfs.memory.databind.annotation.MemoryPropertyOrder;
import at.o2xfs.memory.databind.annotation.win32.UShort;

@MemoryPropertyOrder({ "year", "month", "dayOfWeek", "day", "hour", "minute", "second", "milliseconds" })
public final class Systemtime {

	public static class Builder {

		private int year;
		private int month;
		private int dayOfWeek;
		private int day;
		private int hour;
		private int minute;
		private int second;
		private int milliseconds;

		public Builder year(int year) {
			this.year = year;
			return this;
		}

		public Builder month(int month) {
			this.month = month;
			return this;
		}

		public Builder dayOfWeek(int dayOfWeek) {
			this.dayOfWeek = dayOfWeek;
			return this;
		}

		public Builder day(int day) {
			this.day = day;
			return this;
		}

		public Builder hour(int hour) {
			this.hour = hour;
			return this;
		}

		public Builder minute(int minute) {
			this.minute = minute;
			return this;
		}

		public Builder second(int second) {
			this.second = second;
			return this;
		}

		public Builder milliseconds(int milliseconds) {
			this.milliseconds = milliseconds;
			return this;
		}

		public Systemtime build() {
			return new Systemtime(this);
		}
	}

	private static final Systemtime EMPTY = new Systemtime.Builder().build();

	@UShort
	private final int year;

	@UShort
	private final int month;

	@UShort
	private final int dayOfWeek;

	@UShort
	private final int day;

	@UShort
	private final int hour;

	@UShort
	private final int minute;

	@UShort
	private final int second;

	@UShort
	private final int milliseconds;

	private Systemtime(Builder builder) {
		year = builder.year;
		month = builder.month;
		dayOfWeek = builder.dayOfWeek;
		day = builder.day;
		hour = builder.hour;
		minute = builder.minute;
		second = builder.second;
		milliseconds = builder.milliseconds;
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getDayOfWeek() {
		return dayOfWeek;
	}

	public int getDay() {
		return day;
	}

	public int getHour() {
		return hour;
	}

	public int getMinute() {
		return minute;
	}

	public int getSecond() {
		return second;
	}

	public int getMilliseconds() {
		return milliseconds;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(year)
				.append(month)
				.append(dayOfWeek)
				.append(day)
				.append(hour)
				.append(minute)
				.append(second)
				.append(milliseconds)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Systemtime) {
			Systemtime systemtime = (Systemtime) obj;
			return new EqualsBuilder()
					.append(year, systemtime.year)
					.append(month, systemtime.month)
					.append(dayOfWeek, systemtime.dayOfWeek)
					.append(day, systemtime.day)
					.append(hour, systemtime.hour)
					.append(minute, systemtime.minute)
					.append(second, systemtime.second)
					.append(milliseconds, systemtime.milliseconds)
					.isEquals();
		}
		return false;
	}

	@Override
	public String toString() {
		return String.format("%04d-%02d-%02dT%02d:%02d:%02d.%d", year, month, day, hour, minute, second, milliseconds);
	}

	public static Systemtime empty() {
		return EMPTY;
	}

	public static Systemtime valueOf(LocalDateTime dateTime) {
		return new Systemtime.Builder()
				.year(dateTime.getYear())
				.month(dateTime.getMonth().getValue())
				.dayOfWeek(DayOfWeek.SUNDAY.equals(dateTime.getDayOfWeek()) ? 0 : dateTime.getDayOfWeek().getValue())
				.day(dateTime.getDayOfMonth())
				.hour(dateTime.getHour())
				.minute(dateTime.getMinute())
				.second(dateTime.getSecond())
				.milliseconds(dateTime.get(ChronoField.MILLI_OF_SECOND))
				.build();
	}
}
