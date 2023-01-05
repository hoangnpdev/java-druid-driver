package com.nph.druid;

import lombok.Data;

@Data
public class DruidQueryBuilder {

    public TopNQuery.TopNQueryBuilder topN() {
        return TopNQuery.builder();
    }

    public SelectQuery.SelectQueryBuilder select() {
        return SelectQuery.builder();
    }

    public TimeseriesQuery.TimeseriesQueryBuilder timeseries() {
        return TimeseriesQuery.builder();
    }

    public GroupByQuery.GroupByQueryBuilder groupBy() { return GroupByQuery.builder(); }

}
