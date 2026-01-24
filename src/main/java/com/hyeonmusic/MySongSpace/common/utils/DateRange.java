package com.hyeonmusic.MySongSpace.common.utils;

import java.time.LocalDate;

public class DateRange {
    private LocalDate from;
    private LocalDate to;

    public DateRange(LocalDate from, LocalDate to) {
        this.from = from;
        this.to = to;
    }

    public LocalDate from() {
        return from;
    }
    public LocalDate to() {
        return to;
    }
}
