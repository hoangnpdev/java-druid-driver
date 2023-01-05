package com.nph.druid.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupByBaseElement<T> {

    private String version;

    private String timestamp;

    private T event;

}
