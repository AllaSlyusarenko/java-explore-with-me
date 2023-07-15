package ru.practicum.ewm.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.dto.stats.ViewStatsRequest;
import ru.practicum.ewm.dto.stats.ViewStatsResponse;
import ru.practicum.ewm.stats.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Stats, Long> {

    //    public default List<ViewStatsResponse> getViews(ViewStatsRequest viewStatsRequest){
//        return List.of();
//    }
    public List<Stats> findAllByUriInAndCreatedBetween(List<String> uris, LocalDateTime start, LocalDateTime end);

    public long countByUri(String uri);
    public long countDistinctByUriAndIp(String uri, String ip);

    public List<Stats> findAllByCreatedBetween(LocalDateTime start, LocalDateTime end);

    public List<Stats> findDistinctStatsByUriInAndCreatedBetween(List<String> uris, LocalDateTime start, LocalDateTime end);
    public List<Stats> findStatsDistinctByUriAndIp(String uri, String ip);

    @Query("SELECT COUNT (ip) FROM Stats " +
            "WHERE uri = ?1")
    Integer findHitCountByUri(String uri);

    @Query("SELECT COUNT (DISTINCT ip) FROM Stats " +
            "WHERE uri = ?1")
    Integer findHitCountByUriWithUniqueIp(String uri);
}
