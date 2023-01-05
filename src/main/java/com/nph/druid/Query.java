package com.nph.druid;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class Query {
    private String queryType;
    private String dataSource;

    public static DruidQueryBuilder getBuilder() {
        return new DruidQueryBuilder();
    }
}
