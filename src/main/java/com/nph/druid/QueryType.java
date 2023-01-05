package com.nph.druid;

public enum QueryType {
    SELECT("select"),
    TIME_SERIES("timeseries"),
    TOP_N("topN"),
    GROUP_BY("groupBy")
    ;

    public final String value;

    private QueryType(String value) {
        this.value = value;
    }
}
