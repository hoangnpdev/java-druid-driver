package com.nph.druid.query;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Aggregation {

    private String type;

    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fieldName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> fieldNames;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fnAggregate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fnCombine;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fnReset;


    public static Aggregation of(String type, String name, String fieldName) {
        Aggregation agg = new Aggregation();
        agg.type = type;
        agg.name = name;
        agg.fieldName = fieldName;
        return agg;
    }

    public static List<Aggregation> maxList(List<String> metrics) {
        return metrics.stream().map(metric -> Aggregation.of("doubleMax", metric, metric)).collect(Collectors.toList());
    }
}
