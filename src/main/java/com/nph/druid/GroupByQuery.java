package com.nph.druid;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nph.druid.query.*;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class GroupByQuery extends Query {

    private List<String> dimensions;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LimitSpec limitSpec;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Having having;

    private Granularity granularity;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Filter filter;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Aggregation> aggregations;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PostAggregation postAggregations;

    private Intervals intervals;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Context context;
}
