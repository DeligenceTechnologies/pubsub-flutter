package com.deligence.pubnub_flutter.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Convenience methods for getting timestamps.
 */
public class DateTimeUtil {
    public static String getTimeStampUtc() {
        return DateTime.now(DateTimeZone.UTC).toString(ISODateTimeFormat.dateTime());
    }

    public static String getTimeStampUtc(long instant) {
        return ISODateTimeFormat.dateTime().withZoneUTC().print(instant);
    }
}