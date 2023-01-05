package com.nph.druid.query;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PostAggregation {

    private String type;

    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String value;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fieldName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> fieldNames;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String function;
}
