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
    private static final String URL_SERVER = "http://stats-server:9090"; //для docker
//  private static final String URL_SERVER = "http://localhost:9090"; //для debug
    private static final String HIT_PREFIX = "/hit";
    private static final String STATS_PREFIX = "/stats";

    @Autowired
    public StatsClient(@Value(URL_SERVER) String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> saveHit(String appName, String uri, String ip, String timestamp) {
        log.info("Сохранение информации о запросе: appName = {}, uri = {}, ip = {}, timestamp = {}",
                appName, uri, ip, timestamp);
        EndpointHit endpointHit = EndpointHit.builder()
                .app(appName)
                .uri(uri)
                .ip(ip)
                .timestamp(timestamp)
                .build();
        return post(HIT_PREFIX, endpointHit);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        log.info("Получение статистики по параметрам: start = {}, end = {}, uris = {}, unique = {}",
                start, end, uris, unique);
        if (start == null || end == null || start.isAfter(end)) {
            throw new IllegalArgumentException("Ошибка в указании времени начала и конца");
        }
        Map<String, Object> parameters = Map.of(
                "start", start.format(formatter),
                "end", end.format(formatter)
        );
        StringBuilder pathBuilder = new StringBuilder(STATS_PREFIX + "?start={start}&end={end}");
        if (uris != null && !uris.isEmpty()) {
            for (String uri : uris) {
                pathBuilder.append("&uris=").append(uri);
            }
        }
        pathBuilder.append("&unique=").append(unique);
        return get(pathBuilder.toString(), parameters);
    }
}