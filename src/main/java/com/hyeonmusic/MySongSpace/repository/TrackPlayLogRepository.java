package com.hyeonmusic.MySongSpace.repository;

import com.hyeonmusic.MySongSpace.entity.Track;
import com.hyeonmusic.MySongSpace.entity.TrackPlayLog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
@Repository
public interface TrackPlayLogRepository extends JpaRepository<TrackPlayLog,Long> {
    @Query("""
        select (count(l) > 0)
        from TrackPlayLog l
        where l.track = :track
          and l.viewerKey = :viewerKey
          and l.playedAt >= :fromTime
    """)
    boolean isTrackPlayLogDuplicated(
            @Param("track") Track track,
            @Param("viewerKey") String viewerKey,
            @Param("fromTime") LocalDateTime fromTime
    );
}
