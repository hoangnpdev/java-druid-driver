package com.nph.druid.query;

import lombok.Data;

@Data
public class TopNMetricSpec {
    private String type;
    private String metric;

    public static TopNMetricSpec from(String metric) {
        TopNMetricSpec topNMetricSpec = new TopNMetricSpec();
        topNMetricSpec.type = "numeric";
        topNMetricSpec.metric = metric;
        return topNMetricSpec;
    }
}


