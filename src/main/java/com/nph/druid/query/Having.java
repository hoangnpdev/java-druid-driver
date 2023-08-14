package com.nph.druid.query;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Having {
    private String type;
    private String aggregation;
    private Object value;
}
