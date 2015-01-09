package com.china.acetech.ToolPackage.data.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 時間區域設置模塊。暫時只用這麼點
 *
 * @author bxc2010011
 */
public class TempTimeZone {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);

    public static TimeZone getDefaultTimeZone() {
        return TimeZone.getDefault();
    }

    public static String getFormatDateString(Date date) {
        synchronized (dateFormat) {
            StringBuilder builder = new StringBuilder(dateFormat.format(date));
            return builder.insert(-2 + builder.length(), ":").toString();
        }
    }
}
