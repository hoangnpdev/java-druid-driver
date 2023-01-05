package com.nph.druid.query;

import com.fasterxml.jackson.annotation.JsonValue;
import com.nph.druid.util.DateUtil;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.List;

@Data
public class Intervals {

    private static final String TIME_PATTERN = "%s-%s-%sT%s:%s:%s.%s";

    //"1900-01-09T00:00:00.000Z/2992-01-10T00:00:00.000Z"
    private String startDate;

    //"1900-01-09T00:00:00.000Z/2992-01-10T00:00:00.000Z"
    private String endDate;

    public static Intervals from(int yyyy, int MM, int dd, int HH, int mm, int ss, int SSS) {
        Intervals instance = new Intervals();
        instance.startDate = String.format(TIME_PATTERN, yyyy, MM, dd, HH, mm, ss, SSS);
        instance.startDate = instance.endDate;
        return instance;
    }

    public static Intervals from(String startDate, String endDate) {
        Intervals instance = new Intervals();
        instance.startDate = startDate + "T00:00:00.000";
        instance.endDate = endDate + "T23:59:59.999";
        return instance;
    }

    @SneakyThrows
    public static Intervals from_yyyyMMdd(String startDate, String endDate) {
        String s = DateUtil.reFormat(startDate, "yyyyMMdd", "yyyy-MM-dd");
        String e = DateUtil.reFormat(endDate, "yyyyMMdd", "yyyy-MM-dd");
        return from(s, e);
    }

    public void setEndDate(int yyyy, int MM, int dd, int HH, int mm, int ss, int SSS) {
        this.endDate = String.format(TIME_PATTERN, yyyy, MM, dd, HH, mm, ss, SSS);
    }

    @JsonValue
    public List<String> getIntervals() {
        return Arrays.asList(String.join("/", startDate, endDate));
    }
}
