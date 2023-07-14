package ru.practicum.ewm.stats.service;

import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.stats.EndpointHit;
import ru.practicum.ewm.stats.mapper.EndpoitHitMapper;
import ru.practicum.ewm.stats.repository.StatsRepository;

@Service
public class StatsServiceImpl implements StatsService {
    private StatsRepository statsRepository;

    @Override
    public void saveHit(EndpointHit endpointHit) {
        statsRepository.save(EndpoitHitMapper.endpoitHitToStats(endpointHit));
    }
}
