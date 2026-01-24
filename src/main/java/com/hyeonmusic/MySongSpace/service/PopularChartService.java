package com.hyeonmusic.MySongSpace.service;


import com.hyeonmusic.MySongSpace.common.utils.DateRange;
import com.hyeonmusic.MySongSpace.dto.track.TrackResponseDTO;
import com.hyeonmusic.MySongSpace.entity.*;
import com.hyeonmusic.MySongSpace.repository.Track.TrackRepository;
import com.hyeonmusic.MySongSpace.repository.TrackGenreRepository;
import com.hyeonmusic.MySongSpace.repository.TrackMoodRepository;
import com.hyeonmusic.MySongSpace.repository.TrackPlayDaily.TrackPlayDailyRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PopularChartService {
    private final TrackRepository trackRepository;
    private final TrackPlayDailyRepository trackPlayDailyRepository;
    private final TrackGenreRepository trackGenreRepository;
    private final TrackMoodRepository trackMoodRepository;

    public List<TrackResponseDTO> getTop10(ChartPeriod period) {
        DateRange dateRange = resolveDateRange(period, LocalDate.now());
        List<Tuple> top10TrackIdAndPlayCount = trackPlayDailyRepository.findTop10TrackIdAndPlayCount(dateRange);
        List<Long> rankedTrackIds = top10TrackIdAndPlayCount.stream()
                .map(t -> t.get(0, Long.class))
                .toList();

        Map<Long, Long> playCountTracksMap = top10TrackIdAndPlayCount.stream()
                .collect(Collectors.toMap(
                        t -> t.get(0, Long.class),
                        // 값이 없는 경우(null)에는 안전하게 0으로 매핑
                        t -> Optional.ofNullable(t.get(1, Long.class)).orElse(0L)
                ));

        List<Track> trackAndMemberFetchList = trackRepository.findTracksWithMemberByTracksId(rankedTrackIds);
        Map<Long, Track> trackAndMemberFetchMap = trackAndMemberFetchList.stream()
                .collect(Collectors.toMap(Track::getTrackId, t -> t));

        List<TrackGenre> trackGenres = trackGenreRepository.findGenreByTrackIds(rankedTrackIds);
        List<TrackMood> trackMoods = trackMoodRepository.findMoodByTrackIds(rankedTrackIds);

        Map<Long, List<Genre>> genreMap = trackGenres.stream()
                .collect(Collectors.groupingBy(
                        tg -> tg.getTrack().getTrackId(),
                        Collectors.mapping(TrackGenre::getGenre, Collectors.toList())
                ));

        Map<Long, List<Mood>> moodMap = trackMoods.stream()
                .collect(Collectors.groupingBy(
                        tm -> tm.getTrack().getTrackId(),
                        Collectors.mapping(TrackMood::getMood, Collectors.toList())
                ));

        List<TrackResponseDTO> result = new ArrayList<>();

        for (Long trackId : rankedTrackIds) {
            Track track = trackAndMemberFetchMap.get(trackId);
            if (track == null) continue;

            Long periodPlayCount = playCountTracksMap.getOrDefault(trackId, 0L);
            List<Genre> genres = genreMap.getOrDefault(trackId, List.of());
            List<Mood> moods = moodMap.getOrDefault(trackId, List.of());

            result.add(TrackResponseDTO.toResponse(track, genres, moods, periodPlayCount));
        }
        return result;
    }

    private DateRange resolveDateRange(ChartPeriod period, LocalDate today) {
        LocalDate from = switch (period) {
            case WEEK -> startOfWeek(today);
            case MONTH -> startOfMonth(today);
            case YEAR -> startOfYear(today);
        };
        return new DateRange(from, today);
    }

    private LocalDate startOfWeek(LocalDate today) {
        return today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    private LocalDate startOfMonth(LocalDate today) {
        return today.with(TemporalAdjusters.firstDayOfMonth());
    }

    private LocalDate startOfYear(LocalDate today) {
        return today.with(TemporalAdjusters.firstDayOfYear());
    }
}
