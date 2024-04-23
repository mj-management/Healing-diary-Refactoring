package com.ssafy.healingdiary.domain.diary.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentCreateRequest {
    private Long diaryId;
    private Long parentId;
    private String content;
}
