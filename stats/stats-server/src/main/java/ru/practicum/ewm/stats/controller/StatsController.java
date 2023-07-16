package ru.practicum.ewm.stats.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.stats.EndpointHit;
import ru.practicum.ewm.dto.stats.ViewStatsResponse;
import ru.practicum.ewm.stats.service.StatsService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class StatsController {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void saveHit(@Valid @RequestBody EndpointHit endpointHit) {
        statsService.saveHit(endpointHit);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ViewStatsResponse>> getStats(@RequestParam @NotNull String start,
                                                            @RequestParam @NotNull String end,
                                                            @RequestParam(required = false) List<String> uris,
                                                            @RequestParam(defaultValue = "false") Boolean unique) {
        LocalDateTime startDate;
        LocalDateTime endDate;
        try {
            startDate = LocalDateTime.parse(start, formatter);
            endDate = LocalDateTime.parse(end, formatter);
        } catch (DateTimeException exception) {
            return ResponseEntity.badRequest().build();
        }
        List<ViewStatsResponse> results = statsService.getViews(startDate, endDate, uris, unique);
        return ResponseEntity.ok(results);
    }
}
