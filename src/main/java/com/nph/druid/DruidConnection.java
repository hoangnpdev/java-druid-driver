package com.nph.druid;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.nph.druid.result.*;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DruidConnection {

    private static Logger log = LoggerFactory.getLogger(DruidConnection.class);

    protected ObjectMapper jsonMapper;

    protected WebClient webClient;

    protected DruidConnection() {
        jsonMapper = new ObjectMapper();
    }

    protected DruidConnection(String druidUrl) {
        jsonMapper = new ObjectMapper();
        webClient = getWebClient(druidUrl);
    }

    protected WebClient getWebClient(String baseUrl) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 60000)
                .responseTimeout(Duration.ofMillis(60000))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(60000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(60000, TimeUnit.MILLISECONDS)));
        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(150 * 1024 * 1024))
                .build();
        return WebClient.builder()
                .exchangeStrategies(strategies)
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    public static DruidConnection getConnection(String druidUrl) {
        return new DruidConnection(druidUrl);
    }

    public String executeQueryRRaw(Query query) {
        String req = null;
        try {
            req = jsonMapper.writeValueAsString(query);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Cannot parse processing json from druid query object.");
        }
        return executeQueryRRaw(req);
    }

    public String executeQueryRRaw(String nativeQuery) {
        log.info("native query: " + nativeQuery);
        return webClient.post()
                .body(Mono.just(nativeQuery), String.class)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(60))
                .block();
    }

    @SneakyThrows
    public <T> List<T> groupForList(Query query, Class<T> rType) {
        String res = executeQueryRRaw(query);
        TypeFactory typeFactory = jsonMapper.getTypeFactory();
        JavaType javaType = typeFactory.constructCollectionLikeType(
                List.class,
                typeFactory.constructParametricType(
                        GroupByBaseElement.class,
                        rType
                )
        );
        List<GroupByBaseElement<T>> result = jsonMapper.readValue(res, javaType);
        return result
                .stream()
                .map(GroupByBaseElement::getEvent)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public <T> List<T> selectForList(Query query, Class<T> rType) {
        String res = executeQueryRRaw(query);
        TypeFactory typeFactory = jsonMapper.getTypeFactory();
        JavaType javaType = typeFactory.constructCollectionLikeType(
                List.class,
                typeFactory.constructParametricType(
                        BaseResultElement.class,
                        typeFactory.constructParametricType(
                                SelectResult.class,
                                rType
                        )
                )
        );

        List<BaseResultElement<SelectResult<T>>> r = jsonMapper.readValue(res, javaType);
        if (!r.isEmpty()) {
            return r.get(0)
                    .getResult()
                    .getEvents()
                    .stream()
                    .map(EventElement::getEvent)
                    .collect(Collectors.toList());
        }
        return new ArrayList<T>();
    }

    @SneakyThrows
    public <T> Page<T> selectForPage(Query query, Class<T> rType) {
        Page<T> page = new Page<>();
        String res = executeQueryRRaw(query);
        TypeFactory typeFactory = jsonMapper.getTypeFactory();
        JavaType javaType = typeFactory.constructCollectionLikeType(
                List.class,
                typeFactory.constructParametricType(
                        BaseResultElement.class,
                        typeFactory.constructParametricType(
                                SelectResult.class,
                                rType
                        )
                )
        );
        List<BaseResultElement<SelectResult<T>>> r = jsonMapper.readValue(res, javaType);
        List<T> d = new ArrayList<>();
        String pageId = "";
        if (!r.isEmpty()) {
            d = r.get(0)
                    .getResult()
                    .getEvents()
                    .stream()
                    .map(EventElement::getEvent)
                    .collect(Collectors.toList());

            pageId = r.get(0)
                    .getResult()
                    .getBase64Page();
        }
        page.setKpi(d);
        page.setBase64Page(pageId);
        return page;
    }

}
