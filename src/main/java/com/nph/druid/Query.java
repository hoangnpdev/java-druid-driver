package com.nph.druid;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class Query {
    private String queryType;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object dataSource;

    public static DruidQueryBuilder getBuilder() {
        return new DruidQueryBuilder();
    }
}
