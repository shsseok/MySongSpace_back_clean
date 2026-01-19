package com.hyeonmusic.MySongSpace.service;

import com.hyeonmusic.MySongSpace.dto.record.TrackPlayRequest;
import com.hyeonmusic.MySongSpace.entity.Member;
import com.hyeonmusic.MySongSpace.entity.Track;
import com.hyeonmusic.MySongSpace.entity.TrackPlayDaily;
import com.hyeonmusic.MySongSpace.entity.TrackPlayLog;
import com.hyeonmusic.MySongSpace.exception.MemberNotFoundException;
import com.hyeonmusic.MySongSpace.exception.SessionNotFoundException;
import com.hyeonmusic.MySongSpace.exception.TrackNotFoundException;
import com.hyeonmusic.MySongSpace.exception.utils.ErrorCode;
import com.hyeonmusic.MySongSpace.repository.MemberRepository;
import com.hyeonmusic.MySongSpace.repository.Track.TrackRepository;
import com.hyeonmusic.MySongSpace.repository.TrackPlayDailyRepository;
import com.hyeonmusic.MySongSpace.repository.TrackPlayLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.hyeonmusic.MySongSpace.exception.utils.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TrackPlayService {
    private final TrackPlayLogRepository playLogRepository;
    private final TrackPlayDailyRepository trackPlayDailyRepository;
    private final TrackRepository trackRepository;
    private final MemberRepository memberRepository;

    public void recordPlay(Long trackId, String memberKey, String sessionId) {
        Track track = trackRepository.findById(trackId).orElseThrow(() -> new TrackNotFoundException(TRACK_NOT_FOUND));

        Member member = null;
        String viewerKey;

        boolean isLogin = memberKey != null && !memberKey.isBlank();
        boolean isAnonymous = sessionId != null && !sessionId.isBlank();

        if (isLogin) {
            member = memberRepository.findByMemberKey(memberKey)
                    .orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));
            viewerKey = "M:" + memberKey;
        } else {
            if (!isAnonymous) {
                throw new SessionNotFoundException(SESSION_NOT_FOUND);
            }
            viewerKey = "S:" + sessionId;
        }
        //5분안에 재생한적이 있다면
        if(isFiveMinuteDuplicated(track,viewerKey)){
            return;
        }
        //재생 로그 저장(로그인/비로그인 모두)
        playLogRepository.save(TrackPlayLog.createTrackPlayLog(track,member,viewerKey));

        //트랙 재생수 업
        track.increasePlayCount();
        //트랙 재생수 일 단위 합계 측정
        increaseDailyPlayCount(track,LocalDate.now());
        
    }

    private boolean isFiveMinuteDuplicated(Track track,String viewerKey){
        LocalDateTime prevFiveMinute = LocalDateTime.now().minusMinutes(5);
        return playLogRepository.isTrackPlayLogDuplicated(track,viewerKey,prevFiveMinute);
    }

    private void increaseDailyPlayCount(Track track, LocalDate date) {
        TrackPlayDaily daily = trackPlayDailyRepository.findForUpdate(date, track).orElse(null);

        if (daily == null) {
            trackPlayDailyRepository.save(TrackPlayDaily.createTrackPlayDaily(LocalDate.now(),track));
            return;
        }
        daily.increasePlayCount();
    }



}
