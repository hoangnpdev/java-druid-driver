package com.nph.druid.result;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EventElement<T> {

    @JsonProperty("segmentId")
    private String segmentId;

    private Long offset;

    private T event;
}
