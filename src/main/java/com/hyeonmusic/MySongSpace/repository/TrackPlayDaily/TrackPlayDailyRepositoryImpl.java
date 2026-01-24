package com.hyeonmusic.MySongSpace.repository.TrackPlayDaily;

import com.hyeonmusic.MySongSpace.common.utils.DateRange;
import com.hyeonmusic.MySongSpace.entity.*;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class TrackPlayDailyRepositoryImpl implements TrackPlayDailyRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    private final QTrackPlayDaily qTrackPlayDaily = QTrackPlayDaily.trackPlayDaily;

    public TrackPlayDailyRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Tuple> findTop10TrackIdAndPlayCount(DateRange dateRange) {
        return queryFactory.select(
                        qTrackPlayDaily.track.trackId,
                        qTrackPlayDaily.playCount.sum()
                ).from(qTrackPlayDaily)
                .where(qTrackPlayDaily.playDate.between(dateRange.from(), dateRange.to()))
                .groupBy(qTrackPlayDaily.track.trackId)
                .orderBy(qTrackPlayDaily.playCount.sum().desc())
                .limit(10)
                .fetch();
    }
}
