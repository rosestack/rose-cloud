/*
 * Copyright © 2025 rose-group.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.rose.core.util.date;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * 时间工具类
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class TimeUtils {
    public static String format(final LocalDate localDate, String datePattern) {
        return localDate.format(DateTimeFormatter.ofPattern(datePattern));
    }

    public static String format(final LocalDateTime localDateTime, String datePattern) {
        return localDateTime.format(DateTimeFormatter.ofPattern(datePattern));
    }

    /**
     * Converts local date to Date.
     */
    public static Date toDate(final LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Converts local date to Date.
     */
    public static Date toDate(final LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Converts local date to Calendar.
     */
    public static Calendar toCalendar(final LocalDateTime localDateTime) {
        return GregorianCalendar.from(ZonedDateTime.of(localDateTime, ZoneId.systemDefault()));
    }

    /**
     * Converts local date to Calendar and setting date to midnight.
     */
    public static Calendar toCalendar(final LocalDate localDate) {
        return GregorianCalendar.from(ZonedDateTime.of(localDate, LocalTime.MIDNIGHT, ZoneId.systemDefault()));
    }

    /**
     * Converts local date to epoh milliseconds.
     */
    public static long toMilliseconds(final LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static long toSeconds(final LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    /**
     * Converts local date to epoh milliseconds assuming start of the day as date point.
     */
    public static long toMilliseconds(final LocalDate localDate) {
        return toMilliseconds(localDate.atStartOfDay());
    }

    public static LocalDateTime fromCalendar(final Calendar calendar) {
        final TimeZone tz = calendar.getTimeZone();
        final ZoneId zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
        return LocalDateTime.ofInstant(calendar.toInstant(), zid);
    }

    public static LocalDateTime fromDate(final Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static LocalDateTime fromLocalDate(LocalDate localDate) {
        return LocalDateTime.of(localDate, LocalTime.parse("00:00:00"));
    }

    public static LocalDateTime fromMilliseconds(final long milliseconds) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault());
    }

    public static LocalDateTime fromSeconds(final long milliseconds) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(milliseconds), ZoneId.systemDefault());
    }

    public static LocalDateTime getLocalDateTime() {
        return LocalDateTime.now(ZoneId.systemDefault());
    }

    public static LocalDateTime getLocalDateTimeUTC() {
        return LocalDateTime.now(Clock.systemUTC());
    }

    public static LocalDateTime getStartDay() {
        return getStartDay(getLocalDateTime());
    }

    public static LocalDateTime getStartDay(LocalDateTime time) {
        return time.toLocalDate().atStartOfDay();
    }

    public static LocalDateTime getEndDay() {
        return getEndDay(LocalDateTime.now());
    }

    public static LocalDateTime getEndDay(LocalDateTime time) {
        return time.with(LocalTime.MAX);
    }

    public static LocalDateTime getStartWorkDay() {
        return getStartWorkDay(LocalDateTime.now());
    }

    public static LocalDateTime getStartWorkDay(LocalDateTime time) {
        return getStartDay(time).plusHours(8);
    }

    public static LocalDateTime getEndWorkDay() {
        return getEndWorkDay(LocalDateTime.now());
    }

    public static LocalDateTime getEndWorkDay(LocalDateTime time) {
        return getStartWorkDay(time).plusHours(9);
    }

    public static LocalDate getFirstDayOfMonth(LocalDate localDate) {
        return localDate.with(TemporalAdjusters.firstDayOfMonth());
    }

    public static LocalDate getLastDayOfMonth(LocalDate localDate) {
        return localDate.with(TemporalAdjusters.lastDayOfMonth());
    }

    public static LocalDate getFirstDayOfYear(LocalDate localDate) {
        return localDate.with(TemporalAdjusters.firstDayOfYear());
    }

    public static LocalDate getLastDayOfYear(LocalDate localDate) {
        return localDate.with(TemporalAdjusters.lastDayOfYear());
    }
}
