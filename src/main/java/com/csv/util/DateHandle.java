package com.csv.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Collections.sort;

import java.util.Calendar;
import java.util.Date;


import java.util.Calendar;
import java.util.Date;

/**
 * Created by appledev131 on 8/1/16.
 */
public class DateHandle {

    public static void main(String[] args) throws Exception {

        System.out.println(getLastDayOfMonth(new Date()));
     //   System.out.println("with tz " + getLastDayOfMonth(new Date(), "America/Chicago"));
        System.out.println("get last str : " + getFormatedLastDayString("America/Chicago"));
        //  System.out.println(getFormatedDateString(""));
        //  System.out.println(getFormatedDateString("Asia/Shanghai"));
        //  System.out.println(getFormatedDateString("Japan"));
        //  System.out.println(getFormatedDateString("Europe/Madrid"));
        //  System.out.println(getFormatedDateString("GMT+8:00"));

//        System.out.println(getFormatedDateString(8));
        System.out.println("getstr : " + getFormatedDateString("America/Chicago"));

        System.out.println("getday : " + getFormatedDayOnlyString("America/Chicago"));
        Date date_chi = getFormatedDate("America/Chicago");
        System.out.println("chi " + date_chi);
        Date x1 = new Date(Calendar.getInstance(TimeZone.getTimeZone("American/Chicago")).DAY_OF_MONTH);
        System.out.println("x1 " + x1);
        System.out.println("aaa " + getLastDayOfMonth(x1));
        Date x2 = new Date();
        System.out.println(x1.after(x2));

        //   printSysProperties();
    }

    public static int getLastDayOfMonth(Date sDate1, String _timeZone) {
       // Calendar cDay1 = Calendar.getInstance();

        TimeZone timeZone = null;
        if (StringUtils.isEmpty(_timeZone)) {
            timeZone = TimeZone.getDefault();
        } else {
            timeZone = TimeZone.getTimeZone(_timeZone);
        }
        Calendar cDay1 = Calendar.getInstance(timeZone);
       // cDay1.setTimeZone(timeZone);
        cDay1.setTime(sDate1);
        final int lastDay = cDay1.getActualMaximum(Calendar.DAY_OF_MONTH);

        return lastDay;
    }

    public static Date getLastDayOfMonth(Date sDate1) {
        Calendar cDay1 = Calendar.getInstance();
        cDay1.setTime(sDate1);

        final int lastDay = cDay1.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println(lastDay);
        //  final int firstDay = cDay1.getActualMinimum(Calendar.DAY_OF_MONTH);
        //  System.out.println(firstDay);
        Date lastDate = cDay1.getTime();
        return lastDate;
    }

    protected static Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void printSysProperties() {
        Properties props = System.getProperties();
        Iterator iter = props.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            System.out.println(key + " = " + props.get(key));
        }
    }

    /**
     * timeZoneOffset表示时区，如中国一般使用东八区，因此timeZoneOffset就是8
     *
     * @param timeZoneOffset
     * @return
     */
    public static String getFormatedDateString(int timeZoneOffset) {
        if (timeZoneOffset > 13 || timeZoneOffset < -12) {
            timeZoneOffset = 0;
        }
        TimeZone timeZone;
        String[] ids = TimeZone.getAvailableIDs(timeZoneOffset * 60 * 60 * 1000);
        if (ids.length == 0) {
            // if no ids were returned, something is wrong. use default TimeZone
            timeZone = TimeZone.getDefault();
        } else {
            timeZone = new SimpleTimeZone(timeZoneOffset * 60 * 60 * 1000, ids[0]);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(timeZone);
        return sdf.format(new Date());
    }

    public static String getFormatedDateString(String _timeZone) {
        TimeZone timeZone = null;
        if (StringUtils.isEmpty(_timeZone)) {
            timeZone = TimeZone.getDefault();
        } else {
            timeZone = TimeZone.getTimeZone(_timeZone);
        }

        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日  HH时mm分ss秒");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(timeZone);
        return sdf.format(new Date());
    }

    public static String getFormatedDayOnlyString(String _timeZone) {
        TimeZone timeZone = null;
        if (StringUtils.isEmpty(_timeZone)) {
            timeZone = TimeZone.getDefault();
        } else {
            timeZone = TimeZone.getTimeZone(_timeZone);
        }

        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日  HH时mm分ss秒");
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        sdf.setTimeZone(timeZone);
        return sdf.format(new Date());
    }


    public static String getFormatedLastDayString(String _timeZone) {
        TimeZone timeZone = null;
        if (StringUtils.isEmpty(_timeZone)) {
            timeZone = TimeZone.getDefault();
        } else {
            timeZone = TimeZone.getTimeZone(_timeZone);
        }

        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日  HH时mm分ss秒");
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        sdf.setTimeZone(timeZone);
        return sdf.format(getLastDayOfMonth(new Date()));
    }


    public static Date getFormatedDate(String _timeZone) {
        TimeZone timeZone = null;
        if (StringUtils.isEmpty(_timeZone)) {
            timeZone = TimeZone.getDefault();
        } else {
            timeZone = TimeZone.getTimeZone(_timeZone);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(timeZone);
        return new Date();
    }


}
