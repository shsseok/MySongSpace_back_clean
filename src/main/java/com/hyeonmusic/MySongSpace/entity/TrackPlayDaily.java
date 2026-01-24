package com.hyeonmusic.MySongSpace.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
/*안전장치 역할 만약에 이 부분이 없으면 2개 동시에 들어갔을 때 유일성을 보장하지 못함*/
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"playDate", "track_id"})
        }
)
public class TrackPlayDaily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trackPlayDailyId;


    @Column(nullable = false)
    private LocalDate playDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id", nullable = false)
    private Track track;

    // 해당 날짜의 재생 횟수
    @Column(nullable = false)
    private Long playCount;

    public TrackPlayDaily(LocalDate playDate, Track track) {
        this.playDate = playDate;
        this.track = track;
        this.playCount = 1L;
    }

    /**
     * 하루 최초 재생 시 생성
     * playCount = 1로 시작
     */
    public static TrackPlayDaily createTrackPlayDaily(LocalDate playDate, Track track) {
        TrackPlayDaily trackPlayDaily = new TrackPlayDaily();
        trackPlayDaily.playDate = playDate;
        trackPlayDaily.track = track;
        trackPlayDaily.playCount = 1L;
        return trackPlayDaily;
    }
    public static TrackPlayDaily createTrackPlayDaily(LocalDate playDate, Track track,Long playCount) {
        TrackPlayDaily trackPlayDaily = new TrackPlayDaily();
        trackPlayDaily.playDate = playDate;
        trackPlayDaily.track = track;
        trackPlayDaily.playCount = playCount;
        return trackPlayDaily;
    }
    public void increasePlayCount() {
        this.playCount++;
    }
}
