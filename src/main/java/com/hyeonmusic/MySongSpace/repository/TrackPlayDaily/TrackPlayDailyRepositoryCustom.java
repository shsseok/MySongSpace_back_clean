package com.hyeonmusic.MySongSpace.repository.TrackPlayDaily;

import com.hyeonmusic.MySongSpace.common.utils.DateRange;
import com.querydsl.core.Tuple;



import java.util.List;

public interface TrackPlayDailyRepositoryCustom {
    List<Tuple> findTop10TrackIdAndPlayCount(DateRange dateRange);
}
