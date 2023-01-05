package com.nph.druid;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.nph.druid.result.BaseResultElement;
import com.nph.druid.result.EventElement;
import com.nph.druid.result.Page;
import com.nph.druid.result.SelectResult;
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

    private ObjectMapper jsonMapper;

    private WebClient webClient;

    private DruidConnection() {
    }

    public static DruidConnection getConnection(String druidUrl) {
        DruidConnection druidConnection = new DruidConnection();
        druidConnection.jsonMapper = new ObjectMapper();

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
                .responseTimeout(Duration.ofMillis(30000))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(30000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(30000, TimeUnit.MILLISECONDS)));
        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(50 * 1024 * 1024))
                .build();
        druidConnection.webClient = WebClient.builder()
                .exchangeStrategies(strategies)
                .baseUrl(druidUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
        return druidConnection;
    }

    @SneakyThrows
    @Deprecated
    public List<BaseResultElement> executeQuery(Query query) {
        String req = null;
        req = jsonMapper.writeValueAsString(query);
        String result = executeQueryRRaw(req);
        TypeReference<List<BaseResultElement>> typeReference = new TypeReference<List<BaseResultElement>>() {
        };
        return jsonMapper.readValue(result, typeReference);
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
                .timeout(Duration.ofSeconds(30))
                .block();
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
