package com.china.acetech.ToolPackage.java;


import android.content.Context;
import android.content.res.Resources;
import android.text.format.Time;
import com.china.acetech.ToolPackage.R;
import com.china.acetech.ToolPackage.data.util.CustomLocale;
import com.china.acetech.ToolPackage.data.util.TempTimeZone;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 所有有关aa_TimeZone的函数暂时都无法发挥作用，因为没有自定义的时区设定。
 *
 * @author BXC2010011
 */
public class CalendarTool {
    public static final long SECOND_OF_MILL = 1000L;
    public static final long MINUTE_OF_MILL = 60000L;
    public static final long HOUR_OF_MILL = 3600000L;
    public static final long DAY_OF_MILL = 86400000L;
    public static final long WEEK_OF_MILL = 604800000L;
    public static final int DAY_OF_MINUTE = 1440;

    public static int getMilliSecond(int hour, int minute) {
        return 1000 * (60 * (minute + hour * 60));
    }

    public static int getDefaultStartYear(Context context) {
        try {
            return context.getResources().getInteger(R.integer.default_start_year);
        } catch (Resources.NotFoundException localNotFoundException) {
        }
        return 1990;
    }

    public static int compareHourAndSec(Calendar firstDate, Calendar secondDate) {
        Calendar tempFirst = (Calendar) firstDate.clone();
        tempFirst.set(Calendar.DATE, 0);
        tempFirst.set(Calendar.DAY_OF_WEEK, 0);
        tempFirst.set(Calendar.DAY_OF_WEEK_IN_MONTH, 0);
        tempFirst.set(Calendar.DAY_OF_YEAR, 0);
        tempFirst.set(Calendar.WEEK_OF_MONTH, 0);
        tempFirst.set(Calendar.WEEK_OF_YEAR, 0);
        tempFirst.set(Calendar.MONTH, 0);
        tempFirst.set(Calendar.YEAR, 0);
        Calendar tempSecond = (Calendar) secondDate.clone();
        tempSecond.set(Calendar.DATE, 0);
        tempSecond.set(Calendar.DAY_OF_WEEK, 0);
        tempSecond.set(Calendar.DAY_OF_WEEK_IN_MONTH, 0);
        tempSecond.set(Calendar.DAY_OF_YEAR, 0);
        tempSecond.set(Calendar.WEEK_OF_MONTH, 0);
        tempSecond.set(Calendar.WEEK_OF_YEAR, 0);
        tempSecond.set(Calendar.MONTH, 0);
        tempSecond.set(Calendar.YEAR, 0);
        return tempFirst.compareTo(tempSecond);
    }

    public static long getIntervalWeek(Date firstDate, Date secondDate) {
        return (secondDate.getTime() - firstDate.getTime()) / WEEK_OF_MILL;
    }

    public static Calendar getCalendarWithTime(long time) {
        GregorianCalendar calendar = getCalendar();
        calendar.setTimeInMillis(time);
        filterToWeeklyTime(calendar);
        return calendar;
    }

    public static Date getDate() {
        return new Date();
    }

    public static Date createDateWithTime(int year, int month, int dayOfMonth, int hourOfDay, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        return calendar.getTime();
    }

    public static Date getDailyOfCurrentZone(Date date) {
        return getDailyOfNewZone(date, TimeZone.getDefault());
    }

    public static Date add(Date date, int value, int field) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, value);
        return calendar.getTime();
    }

    public static Date getDailyOfNewZone(Date date, TimeZone timeZone) {
        if (date == null)
            return null;
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTime(date);
        calendar.clear(Calendar.HOUR_OF_DAY);
        calendar.clear(Calendar.HOUR);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        return calendar.getTime();
    }

    public static GregorianCalendar getCalendar(Locale paramLocale) {
        return new GregorianCalendar(TempTimeZone.getDefaultTimeZone(), paramLocale);
    }

    public static void filterToDailyTime(Calendar paramCalendar) {
        paramCalendar.set(Calendar.HOUR_OF_DAY, 0);
        paramCalendar.set(Calendar.MINUTE, 0);
        paramCalendar.set(Calendar.SECOND, 0);
        paramCalendar.set(Calendar.MILLISECOND, 0);
    }

    public static void setHour_Sec_Day(Calendar calendar, int time) {
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, time % 60);
        int minute_sum = time / 60;
        calendar.set(Calendar.MINUTE, minute_sum % 60);
        calendar.set(Calendar.HOUR_OF_DAY, minute_sum / 60 % 24);
    }

    private static boolean isSameDay(Date date, Calendar calendar) {
        Time time1 = new Time();
        time1.set(calendar.getTimeInMillis());
        Time time2 = new Time();
        time2.set(date.getTime());
        return (time1.year == time2.year) && (time1.month == time2.month) && (time1.monthDay == time2.monthDay);
    }

    /**
     * 计算两个日期的天数差值
     *
     * @param firstCalendar  first input calendar
     * @param secondCalendar second input calendar
     * @return 返回两个日期间的天数差值
     */
    public static int getIntervalDaily(Calendar firstCalendar, Calendar secondCalendar) {
        return getDailyNumber(firstCalendar) - getDailyNumber(secondCalendar);
    }

    /**
     * 计算两个日期的天数差值
     *
     * @param firstDate  first input date
     * @param secondDate second input date
     * @return 返回两个日期间的天数差值
     */
    public static long getIntervalDaily(Date firstDate, Date secondDate) {
        return (secondDate.getTime() - firstDate.getTime()) / DAY_OF_MILL;
    }

    public static Calendar getWeeklyTime(long time) {
        Calendar cakebdar = Calendar.getInstance();
        cakebdar.setTimeInMillis(time);
        filterToWeeklyTime(cakebdar);
        return cakebdar;
    }

    public static Date getZeroDate(Context context) {
        int startYear = getDefaultStartYear(context);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(startYear, 0, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获得中午12点的date
     *
     * @param date input date
     * @return 返回中午12点的日期对象
     */
    public static Date getNoonTime(Date date) {
        return getNoonTime(date, TimeZone.getDefault());
    }

    public static Date getLastTime_Daily(Date date, TimeZone timeZone) {
        if (date == null)
            return null;
        Calendar localCalendar = Calendar.getInstance(timeZone);
        localCalendar.setTime(date);
        localCalendar.set(Calendar.HOUR_OF_DAY, 23);
        localCalendar.set(Calendar.MINUTE, 59);
        localCalendar.set(Calendar.SECOND, 59);
        localCalendar.set(Calendar.MILLISECOND, 999);
        return localCalendar.getTime();
    }

    public static TimeZone getGMTTime() {
        return TimeZone.getTimeZone("GMT");
    }

    public static void filterToWeeklyTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public static int getIntervalYearTime(Date paramDate1, Date paramDate2) {
        Calendar localCalendar1 = Calendar.getInstance();
        localCalendar1.setTime(paramDate1);
        Calendar localCalendar2 = Calendar.getInstance();
        localCalendar2.setTime(paramDate2);
        int year = localCalendar2.get(Calendar.YEAR) - localCalendar1.get(Calendar.YEAR);
        if (localCalendar1.get(Calendar.DAY_OF_YEAR) > localCalendar2.get(Calendar.DAY_OF_YEAR))
            --year;
        return year;
    }

    public static Calendar getLastTime_Weekly(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        getLastTime_Weekly(calendar);
        return calendar;
    }

    public static Date getDailyOfNewZone(Date date) {
        return getDailyOfNewZone(date, TempTimeZone.getDefaultTimeZone());
    }

    private static Date getNoonTime(Date date, TimeZone timeZone) {
        if (date == null)
            return null;
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static GregorianCalendar getCalendar() {
        return getCalendar(CustomLocale.getDefault());
    }

    public static void getLastTime_Weekly(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_WEEK, 7);
        calendar.add(Calendar.SECOND, -1);
    }

    public static Calendar getTodayByDefaultLocale() {
        Calendar calendar = Calendar.getInstance(CustomLocale.getDefault());
        calendar.clear(Calendar.MILLISECOND);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.HOUR_OF_DAY);
        return calendar;
    }

    public static Date getLastTime_Daily_ByDefaultLocale(Date date) {
        return getLastTime_Daily(date, TimeZone.getDefault());
    }

    public static Date getEarlyDate(Date date1, Date date2) {
        if (date1.before(date2))
            return date1;
        return date2;
    }

    public static void getLastTimeOfMonth(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
    }

    /**
     * 返回Calendar的天数
     *
     * @param calendar input calendar
     * @return number of day by calendar
     */
    public static int getDailyNumber(Calendar calendar) {
        return (int) ((calendar.getTimeInMillis() + calendar.get(Calendar.ZONE_OFFSET)) / DAY_OF_MILL);
    }

    public static Date dateLastTime_Daily(Date date) {
        return getLastTime_Daily(date, TempTimeZone.getDefaultTimeZone());
    }

    public static Date getLaterTime(Date date1, Date date2) {
        if (date1.after(date2))
            return date1;
        return date2;
    }

    public static Date getLast_Second_OfThisMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);
        calendar.clear(Calendar.HOUR_OF_DAY);
        calendar.clear(Calendar.HOUR);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        calendar.add(Calendar.SECOND, -1);
        return calendar.getTime();
    }

    public static boolean isSameDay(Date date1, Date date2) {
        return getDailyOfCurrentZone(date1).equals(getDailyOfCurrentZone(date2));
    }

    public static Date getLastTime_Daily(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 23);
        calendar.add(Calendar.MINUTE, 59);
        calendar.add(Calendar.SECOND, 59);
        calendar.add(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static boolean isToday(Date date) {
        return date != null && isSameDay(date, Calendar.getInstance());
    }

    public static boolean isToday_DefaultTimeZone(Date date) {
        return isSameDay(date, Calendar.getInstance(TempTimeZone.getDefaultTimeZone()));
    }

    public static boolean isYesterday_ByDefaultTimeZone(Date date) {
        Calendar calendar = Calendar.getInstance(TempTimeZone.getDefaultTimeZone());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return isSameDay(calendar.getTime(), date);
    }

    public static boolean isLastSecond_Daily(Date date) {
        Calendar calendar = Calendar.getInstance(TempTimeZone.getDefaultTimeZone());
        calendar.setTime(date);
        return (calendar.get(Calendar.HOUR_OF_DAY) == 23) && (calendar.get(Calendar.MINUTE) == 59) && (calendar.get(Calendar.SECOND) == 59);
    }

    public static Date setTimeToDefaultTimeZone(Date date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTime(date);
        calendar.add(Calendar.MILLISECOND, TempTimeZone.getDefaultTimeZone().getOffset(date.getTime()) - TimeZone.getDefault().getOffset(date.getTime()));
        return calendar.getTime();
    }

    public static Date setTimeToDefaultLocale(Date date) {
        if (date == null)
            return null;
        Calendar calendar = Calendar.getInstance(TempTimeZone.getDefaultTimeZone());
        calendar.setTime(date);
        calendar.add(Calendar.MILLISECOND, -(TimeZone.getDefault().getOffset(date.getTime()) - TempTimeZone.getDefaultTimeZone().getOffset(date.getTime())));
        return calendar.getTime();
    }

    public static Date setTimeWithoutDefaultTimeZone(Date date) {
        if (date == null)
            return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MILLISECOND, -TimeZone.getDefault().getOffset(date.getTime()));
        return calendar.getTime();
    }

    public static boolean isYestoday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return isSameDay(calendar.getTime(), date);
    }

    public static boolean isTomorrow(Date calendar) {
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.add(Calendar.DAY_OF_MONTH, 1);
        return isSameDay(localCalendar.getTime(), calendar);
    }
}

/* Location:           H:\backupforG\Projects\FA_Jack\apk\dex2jar-0.0.9.15\classes-dex2jar.jar
 * Qualified Name:     com.fitbit.util.o
 * JD-Core Version:    0.5.4
 */
