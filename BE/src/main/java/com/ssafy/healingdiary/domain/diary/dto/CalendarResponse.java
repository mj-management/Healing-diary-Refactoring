package com.ssafy.healingdiary.domain.diary.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CalendarResponse {
    int year;
    int month;
    int day;
    EmotionResponse emotion;

    @QueryProjection
    public CalendarResponse(int year, int month, int day, EmotionResponse emotion) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.emotion = emotion;
    }
}
