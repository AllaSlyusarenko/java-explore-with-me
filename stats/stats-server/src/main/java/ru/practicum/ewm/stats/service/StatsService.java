package ru.practicum.ewm.stats.service;

import ru.practicum.ewm.dto.stats.EndpointHit;
import ru.practicum.ewm.dto.stats.ViewStatsResponse;
import ru.practicum.ewm.stats.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    public void saveHit(EndpointHit endpointHit);

    public List<ViewStatsResponse> getViews(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
