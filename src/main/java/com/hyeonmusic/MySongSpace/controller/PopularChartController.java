package com.hyeonmusic.MySongSpace.controller;

import com.hyeonmusic.MySongSpace.dto.track.TrackResponseDTO;
import com.hyeonmusic.MySongSpace.entity.ChartPeriod;
import com.hyeonmusic.MySongSpace.service.PopularChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tracks")
@RequiredArgsConstructor
public class PopularChartController {

    private final PopularChartService popularChartService;
    @GetMapping("/popular")
    public List<TrackResponseDTO> popular(
            @RequestParam(defaultValue = "WEEK") ChartPeriod period
    ) {
        return popularChartService.getTop10(period);
    }

}
