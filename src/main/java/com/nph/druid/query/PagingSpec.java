package com.nph.druid.query;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nph.druid.util.EncodeUtils;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Data
public class PagingSpec {

    private Map<String, Integer> pagingIdentifiers;

    private String threshold;

    private boolean fromNext = true;

    public static PagingSpec of(String threshold, boolean fromNext) {
        PagingSpec pagingSpec = new PagingSpec();
        pagingSpec.pagingIdentifiers = new HashMap<>();
        pagingSpec.setThreshold(threshold);
        pagingSpec.setFromNext(fromNext);
        return pagingSpec;
    }

    public static PagingSpec of(Map<String, Integer> pagingIdentifiers, Long threshold) {
        PagingSpec pagingSpec = new PagingSpec();
        pagingSpec.pagingIdentifiers = pagingIdentifiers;
        pagingSpec.setThreshold(threshold.toString());
        pagingSpec.setFromNext(true);
        return pagingSpec;
    }

    @Deprecated
    public static PagingSpec of(String pagingIdentity, Long threshold, boolean fromNext) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        PagingSpec pagingSpec = new PagingSpec();
        TypeReference<Map<String, Integer>> typeReference = new TypeReference<Map<String, Integer>>() {};
        pagingSpec.pagingIdentifiers = objectMapper.readValue(pagingIdentity, typeReference);
        pagingSpec.setThreshold(threshold.toString());
        pagingSpec.setFromNext(fromNext);
        return pagingSpec;
    }

    @SneakyThrows
    public static PagingSpec ofBase64(String base64Code, Long threshold) {
        // ignore: remove default old page id format from frontend
        base64Code = base64Code.replace("}", "");
        base64Code = base64Code.replace("{", "");
        ObjectMapper jsonMapper = new ObjectMapper();
        Map<String, Integer> pageIdentifiers = !StringUtils.isEmpty(base64Code) ?
                jsonMapper.readValue(EncodeUtils.decodeUTF8Base64(base64Code), Map.class) : new HashMap<>();
        return of(pageIdentifiers, threshold);
    }
}
