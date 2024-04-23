package com.ssafy.healingdiary.domain.diary.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.healingdiary.domain.diary.domain.Diary;
import com.ssafy.healingdiary.domain.tag.dto.TagListResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DiaryDetailResponse {
    private Long diaryId;
    private String recordUrl;
    private String imageUrl;
    private EmotionResponse emotion;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;
    List<TagListResponse> tags;


    public static DiaryDetailResponse of(Diary diary){
        return DiaryDetailResponse.builder()
            .diaryId(diary.getId())
            .recordUrl(diary.getRecordUrl())
            .imageUrl(diary.getDiaryImageUrl())
            .emotion(EmotionResponse.of(diary.getEmotion()))
            .createdDate(diary.getCreatedDate())
            .tags(diary.getDiaryTag()
                .stream()
                .map(diaryTag -> TagListResponse.of(diaryTag.getTag()))
                .collect(Collectors.toList()))
            .build();
    }
}
