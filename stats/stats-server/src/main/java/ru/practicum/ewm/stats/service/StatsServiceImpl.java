package ru.practicum.ewm.stats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.stats.EndpointHit;
import ru.practicum.ewm.dto.stats.ViewStatsResponse;
import ru.practicum.ewm.stats.mapper.Mapper;
import ru.practicum.ewm.stats.model.Stats;
import ru.practicum.ewm.stats.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    @Transactional
    public void saveHit(EndpointHit endpointHit) {
        statsRepository.save(Mapper.endpoitHitToStats(endpointHit));
    }

    @Override
    public List<ViewStatsResponse> getViews(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        List<Stats> results;
        if (uris == null) {
            results = statsRepository.findAllByCreatedBetween(start, end);
        } else {
            results = statsRepository.findAllByUriInAndCreatedBetween(uris, start, end);
            if (unique) {
                results = results.stream()
                        .filter(distinctByKey(Stats::getUri))
                        .filter(distinctByKey(Stats::getIp))
                        .collect(Collectors.toList());
            }
        }
        List<ViewStatsResponse> responses = new ArrayList<>();
        for (Stats stat : results) {
            Integer hits;
            if(unique){
                hits = statsRepository.findHitCountByUriWithUniqueIp(stat.getUri());
            } else {
                hits = statsRepository.findHitCountByUri(stat.getUri());
            }
            responses.add(Mapper.statToViewStatsResponse(stat, hits));
        }
        return responses;
    }

    @Override
    public List<Stats> getAll() {
        return statsRepository.findAll();
    }
    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = new HashSet<>();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
