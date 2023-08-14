package com.nph.druid.query;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class DataSource {
    private String type;
    private List<String> dataSources;

    public static DataSource from(String... sources) {
        DataSource ds = new DataSource();
        ds.type = "union";
        ds.dataSources = Arrays.asList(sources);
        return ds;
    }

    public static DataSource from(List<String> sources) {
        DataSource ds = new DataSource();
        ds.type = "union";
        ds.dataSources = sources;
        return ds;
    }
}
