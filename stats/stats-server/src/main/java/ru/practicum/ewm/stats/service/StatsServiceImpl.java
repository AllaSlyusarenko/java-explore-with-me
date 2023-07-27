package ru.practicum.ewm.stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.stats.EndpointHit;
import ru.practicum.ewm.dto.stats.ViewStatsResponse;
import ru.practicum.ewm.stats.exception.ValidationException;
import ru.practicum.ewm.stats.mapper.Mapper;
import ru.practicum.ewm.stats.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    @Transactional
    public void saveHit(EndpointHit endpointHit) {
        statsRepository.save(Mapper.endpointHitToStats(endpointHit));
    }

    @Override
    public List<ViewStatsResponse> getViews(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if(start.isAfter(end)){
            throw new ValidationException("Start не может быть после end");
        }
        if (uris == null || uris.isEmpty()) {
            if (unique) {
                log.info("Cтатистика запросов по uris ={} для уникальных ip", uris);
                return statsRepository.getAllStatsUniqueIpWithoutUris(start, end);
            } else {
                log.info("Cтатистика запросов по uris ={} для неуникальных ip", uris);
                return statsRepository.getAllStatsWithoutUris(start, end);
            }
        } else {
            if (unique) {
                log.info("Статистика запросов по uris ={} для уникальных ip", uris);
                return statsRepository.getAllStatsUniqueIpWithUris(start, end, uris);
            } else {
                log.info("Статистика запросов по uris ={} для неуникальных ip", uris);
                return statsRepository.getAllStatsWithUris(start, end, uris);
            }
        }
    }
}