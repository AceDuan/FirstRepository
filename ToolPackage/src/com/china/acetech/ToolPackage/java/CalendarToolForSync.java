package com.china.acetech.ToolPackage.java;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Sync Calendar Tool
 * Created by BXC2010011 on 2015/1/4.
 */
public class CalendarToolForSync {

    public static final String DEFAULT_TIMEZONE_USING = "GMT+0:00";

    public static Calendar getCurrentTimeOfStandardCalendar(){

        TimeZone oldTime = Calendar.getInstance().getTimeZone();

        TimeZone timezone = TimeZone.getTimeZone(DEFAULT_TIMEZONE_USING);
        long date = Calendar.getInstance().getTimeInMillis()/1000;
        date *= 1000;
        return getCurrentTimeInNewTimeZone(oldTime, timezone, date);
    }

    public static Calendar getCurrentTimeOfStandardCalendar(TimeZone oldTime){
        TimeZone timezone = TimeZone.getTimeZone(DEFAULT_TIMEZONE_USING);
        long date = Calendar.getInstance().getTimeInMillis()/1000;
        date *= 1000;
        return getCurrentTimeInNewTimeZone(oldTime, timezone, date);
    }

    public static Calendar getCurrentTimeInNewTimeZone(TimeZone oldTime, TimeZone newTime, long date){
        Calendar cal = Calendar.getInstance(newTime);
        cal.add(Calendar.MILLISECOND, oldTime.getOffset(date) - newTime.getOffset(date));
        return cal;
    }

    public static Date getZeroTime(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(DEFAULT_TIMEZONE_USING));
        cal.set(1900, 1, 1);
        return cal.getTime();
    }

    public static Calendar getZeroOfToday(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(DEFAULT_TIMEZONE_USING));
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);
        cal.clear();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, day);

        return cal;

    }

    public static Date getSyncTime(){
        return Calendar.getInstance().getTime();
    }
}
