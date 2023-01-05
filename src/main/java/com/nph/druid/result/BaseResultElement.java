package com.nph.druid.result;

import lombok.Data;

@Data
public class BaseResultElement<T> {

    private String timestamp;

    private T result;
}
