package ru.practicum.ewm.stats.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.dto.stats.EndpointHit;
import ru.practicum.ewm.dto.stats.ViewStatsResponse;
import ru.practicum.ewm.stats.model.Stats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class Mapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Stats endpoitHitToStats(EndpointHit endpointHit){
        Stats stats = new Stats();
        stats.setApp(endpointHit.getApp());
        stats.setUri(endpointHit.getUri());
        stats.setIp(endpointHit.getIp());
        stats.setCreated(LocalDateTime.parse(endpointHit.getTimestamp(), formatter));
        return stats;
    }

    public ViewStatsResponse statToViewStatsResponse(Stats stat, Integer hits){
        ViewStatsResponse viewStatsResponse = new ViewStatsResponse();
        viewStatsResponse.setApp(stat.getApp());
        viewStatsResponse.setUri(stat.getUri());
        viewStatsResponse.setHits(hits);
        return  viewStatsResponse;
    }

}
