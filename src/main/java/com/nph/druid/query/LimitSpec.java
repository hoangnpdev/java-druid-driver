package com.nph.druid.query;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class LimitSpec {

    private String type;

    private Integer limit;

    private List<ColumnSpec> columns;

    public static LimitSpec of(String type, Integer limit, ColumnSpec columnSpec) {
        LimitSpec limitSpec = new LimitSpec();
        limitSpec.type = type;
        limitSpec.limit = limit;
        limitSpec.columns = Arrays.asList(columnSpec);
        return limitSpec;
    }

    @Data
    public static class ColumnSpec {

        private String dimension;

        private String direction;

        private String dimensionOrder;

        public static ColumnSpec of(String dimension, String direction, String dimensionOrder) {
            ColumnSpec columnSpec = new ColumnSpec();
            columnSpec.dimension = dimension;
            columnSpec.direction = direction;
            columnSpec.dimensionOrder = dimensionOrder;
            return columnSpec;
        }
    }

}
