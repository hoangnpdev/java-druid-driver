package com.nph.druid.result;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SelectResponseElement<T> {

    private String timestamp;

    private SelectResult<T> selectResult;

}
