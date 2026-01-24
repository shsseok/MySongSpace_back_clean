package com.hyeonmusic.MySongSpace.repository;

import com.hyeonmusic.MySongSpace.entity.TrackGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TrackGenreRepository extends JpaRepository<TrackGenre, Long> {
    @Query("""
    select tg
    from TrackGenre tg
    where tg.track.id in :trackIds
""")
    List<TrackGenre> findGenreByTrackIds(@Param("trackIds") List<Long> trackIds);
}
