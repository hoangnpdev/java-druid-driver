package com.nph.druid.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nph.druid.util.EncodeUtils;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
public class SelectResult<T> {

    private Map<String, Integer> pagingIdentifiers;

    private List<EventElement<T>> events;

    @SneakyThrows
    public String getBase64Page() {
        ObjectMapper jsonMapper = new ObjectMapper();
        String value = jsonMapper.writeValueAsString(pagingIdentifiers);
        log.info("pagingIdentifiers: " + value);
        return EncodeUtils.encodeUTF8Base64(value);
    }

    public Page<T> toPage() {
        Page<T> page = new Page<>();
        page.setKpi(events.stream().map(EventElement::getEvent).collect(Collectors.toList()));
        page.setBase64Page(getBase64Page());
        return page;
    }

}
