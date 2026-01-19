package com.hyeonmusic.MySongSpace.controller;

import com.hyeonmusic.MySongSpace.dto.record.TrackPlayRequest;
import com.hyeonmusic.MySongSpace.service.TrackPlayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tracks")
public class TrackPlayController {

    private final TrackPlayService trackPlayService;
    @PostMapping("/{trackId}/plays")
    public ResponseEntity<Void> recordPlay(
            @PathVariable Long trackId,
            @RequestBody(required = false) TrackPlayRequest request,
            @AuthenticationPrincipal UserDetails userDetails
            ) {
        //로그인한 사용자
        String memberKey = (userDetails == null) ? null  : userDetails.getUsername();
        //
        String sessionId = (request == null) ? null : request.getSessionId();

        trackPlayService.recordPlay(trackId,memberKey,sessionId);
        return ResponseEntity.ok().build();
    }
}

