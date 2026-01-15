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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 비로그인 포함 중복 체크용 키
    @Column(nullable = false)
    private String viewerKey;

    // 재생이 인정된 시점
    @Column(nullable = false)
    private LocalDateTime playedAt;

}
