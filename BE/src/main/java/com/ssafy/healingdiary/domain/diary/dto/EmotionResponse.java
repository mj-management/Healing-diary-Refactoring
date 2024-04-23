package com.ssafy.healingdiary.domain.diary.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.ssafy.healingdiary.domain.diary.domain.Emotion;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmotionResponse {
    private int emotionCode;
    private String value;

    @QueryProjection
    public EmotionResponse(int emotionCode, String value) {
        this.emotionCode = emotionCode;
        this.value = value;
    }

    @QueryProjection
    public EmotionResponse(Emotion emotion) {
        this.emotionCode = emotion.getEmotionCode();
        this.value = emotion.getValue();
    }

    public static EmotionResponse of(Emotion emotion){
        return EmotionResponse.builder()
            .emotionCode(emotion.getEmotionCode())
            .value(emotion.getValue())
            .build();
    }
}
