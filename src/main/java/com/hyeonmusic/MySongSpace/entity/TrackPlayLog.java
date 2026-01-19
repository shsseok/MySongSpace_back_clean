package com.hyeonmusic.MySongSpace.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class TrackPlayLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trackPlayLogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id", nullable = false)
    private Track track;

    /*member를 남겨둔건 로그인한 사용자가 내가 들었던 것을 가져올 수 있도록 하기 위함*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = true)
    private Member member;

    // 비로그인 포함 중복 체크용 키
    @Column(nullable = false)
    private String viewerKey;

    // 재생이 인정된 시점
    @Column(nullable = false)
    private LocalDateTime playedAt;

    public static TrackPlayLog createTrackPlayLog( Track track,Member member,String viewerKey){
        TrackPlayLog trackPlayLog = new TrackPlayLog();
        trackPlayLog.member = member;
        trackPlayLog.viewerKey =viewerKey;
        trackPlayLog.track= track;
        trackPlayLog.playedAt = LocalDateTime.now();
        return trackPlayLog;
    }



}
