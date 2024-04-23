package com.ssafy.healingdiary.domain.diary.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DiaryCreateRequest {
    String recordUrl;
    Integer emotionCode;
    Long clubId;
    List<String> tags;
}
