package com.hyeonmusic.MySongSpace.repository;

import com.hyeonmusic.MySongSpace.entity.Member;
import com.hyeonmusic.MySongSpace.entity.TrackMood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TrackMoodRepository extends JpaRepository<TrackMood, Long> {
    @Query("""
    select tm
    from TrackMood tm
    where tm.track.id in :trackIds
""")
    List<TrackMood> findMoodByTrackIds(@Param("trackIds") List<Long> trackIds);

}
