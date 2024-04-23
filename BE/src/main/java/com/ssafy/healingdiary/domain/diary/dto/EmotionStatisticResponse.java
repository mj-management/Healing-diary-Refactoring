package com.ssafy.healingdiary.domain.diary.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmotionStatisticResponse {
    private int emotionCode;
    private String value;
    private Long count;

    @QueryProjection
    public EmotionStatisticResponse(int emotionCode, String value, Long count) {
        this.emotionCode = emotionCode;
        this.value = value;
        this.count = count;
    }
}
