package ru.practicum.ewm.client.stats;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.dto.stats.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class StatsClient extends BaseClient {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> saveHit(String appName, String uri, String ip, String timestamp) {
        EndpointHit endpointHit = EndpointHit.builder()
                .app(appName)
                .uri(uri)
                .ip(ip)
                .timestamp(timestamp)
                .build();
        return post("/hit", endpointHit);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end) { //нет uris, неуникальные ip
        if (start == null || end == null || start.isAfter(end)) {
            throw new IllegalArgumentException("Ошибка в указании времени начала и конца");
        }
        Map<String, Object> parameters = Map.of(
                "start", start.format(formatter),
                "end", end.format(formatter)
        );
        return get("/stats?start={start}&end={end}", parameters);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, Boolean unique) { //нет uris, уникальные ip
        if (start == null || end == null || start.isAfter(end)) {
            throw new IllegalArgumentException("Ошибка в указании времени начала и конца");
        }
        Map<String, Object> parameters = Map.of(
                "start", start.format(formatter),
                "end", end.format(formatter),
                "unique", unique
        );
        return get("/stats?start={start}&end={end}&unique={unique}", parameters);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, List<String> uris) { //есть uris, неуникальные ip
        if (start == null || end == null || start.isAfter(end)) {
            throw new IllegalArgumentException("Ошибка в указании времени начала и конца");
        }
        Map<String, Object> parameters = Map.of(
                "start", start.format(formatter),
                "end", end.format(formatter)
        );
        StringBuilder pathBuilder = new StringBuilder("/stats?start={start}&end={end}");
        for (String uri : uris) {
            pathBuilder.append("&uris=").append(uri);
        }
        return get(pathBuilder.toString(), parameters);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) { //есть uris, уникальные ip
        if (start == null || end == null || start.isAfter(end)) {
            throw new IllegalArgumentException("Ошибка в указании времени начала и конца");
        }
        Map<String, Object> parameters = Map.of(
                "start", start.format(formatter),
                "end", end.format(formatter),
                "unique", unique
        );
        StringBuilder pathBuilder = new StringBuilder("/stats?start={start}&end={end}");
        for (String uri : uris) {
            pathBuilder.append("&uris=").append(uri);
        }
        return get(pathBuilder.toString() + "&unique={unique}", parameters);
    }
}