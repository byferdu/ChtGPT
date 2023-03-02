package com.ferdu.chtgpt.util;

import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Iso8601ZDateTypeConverter {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.CHINA);

    @TypeConverter
    public static Date fromIso8601ZString(String value) {
        try {
            return DATE_FORMAT.parse(value);
        } catch (ParseException e) {
            return null;
        }
    }

    @TypeConverter
    public static String toIso8601ZString(Date value) {
        return DATE_FORMAT.format(value);
    }
}

