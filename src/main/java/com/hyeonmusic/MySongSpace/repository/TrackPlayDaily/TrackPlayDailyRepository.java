package com.hyeonmusic.MySongSpace.repository.TrackPlayDaily;

import com.hyeonmusic.MySongSpace.entity.Track;
import com.hyeonmusic.MySongSpace.entity.TrackPlayDaily;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
@Repository
public interface TrackPlayDailyRepository extends JpaRepository<TrackPlayDaily,Long>,TrackPlayDailyRepositoryCustom {
    /**
     * 같은 날짜 + 같은 트랙 집계 row를
     * 업데이트 용도로 잠금 조회
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select d
        from TrackPlayDaily d
        where d.playDate = :playDate
          and d.track = :track
    """)
    Optional<TrackPlayDaily> findForUpdate(
            @Param("playDate") LocalDate playDate,
            @Param("track") Track track
    );
}
