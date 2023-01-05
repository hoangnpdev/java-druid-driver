package com.nph.druid.query;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Granularity {
    ALL("all"),
    NONE("none"),
    SECOND("second"),
    MINUTE("minute"),
    QUARTER("fifteen_minute"),
    HALF("thirty_minute"),
    HOUR("hour"),
    DAY("day");
    public final String value;
    private Granularity(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }
}
