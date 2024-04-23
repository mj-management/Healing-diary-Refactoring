package com.ssafy.healingdiary.domain.diary.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DiarySimpleResponse {
    Long diaryId;
    String imageUrl;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    LocalDateTime createdDate;
    EmotionResponse emotion;
    List<String> tags;

    @QueryProjection
    public DiarySimpleResponse(Long diaryId, String imageUrl, LocalDateTime createdDate, EmotionResponse emotion, List<String> tags) {
        this.diaryId = diaryId;
        this.imageUrl = imageUrl;
        this.createdDate = createdDate;
        this.emotion = emotion;
        this.tags = tags;
    }
}
