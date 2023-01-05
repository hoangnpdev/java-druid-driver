package com.nph.druid.util;

import lombok.SneakyThrows;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {

    @SneakyThrows
    public static String reFormat(String date, String iFormat, String oFormat) {
        SimpleDateFormat i = new SimpleDateFormat(iFormat);
        SimpleDateFormat o = new SimpleDateFormat(oFormat);
        Date d = i.parse(date);
        return o.format(d);
    }

    public static Date str2Date(String value, String format) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.parse(value);
    }

    public static String date2Str(Date value, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(value);
    }

    public static String getToday() {
        return date2Str(new Date(), "yyyyMMdd");
    }

    public static String yesterday() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        return date2Str(calendar.getTime(), "yyyyMMdd");
    }

    public static String nextDay(String strDate, int nDay) throws ParseException {
        Date date = str2Date(strDate, "yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, nDay);
        return date2Str(calendar.getTime(), "yyyyMMdd");
    }
    
    public static String getStartOfLastHour() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, -60);
        date = calendar.getTime();
        return date2Str(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String getStartOfLastHourOfDay(String iDate) throws ParseException {
        Date date = str2Date(iDate, "yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MINUTE, 0);
        date = calendar.getTime();
        return date2Str(date, "yyyy-MM-dd HH:mm:ss");
    }

    @SneakyThrows
    public static List<String> listDateInMonth(String month) {
        Date date = str2Date(month, "yyyyMM");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        String startDate = month + "01";
        String endDate = date2Str(date, "yyyyMMdd");
        return listBetweenDates(startDate, endDate, "yyyyMMdd");
    }

    @SneakyThrows
    public static List<String> listBetweenDates(String startDate, String endDate, String outputFormat) {
        ArrayList<String> dates = new ArrayList<>();
        Date cd = str2Date(startDate, "yyyyMMdd");
        Date ed = str2Date(endDate, "yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        while (!cd.after(ed)) {

            dates.add(date2Str(cd, outputFormat));

            calendar.setTime(cd);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            cd = calendar.getTime();
        }
        return dates;
    }

}
