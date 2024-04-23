package com.ssafy.healingdiary.domain.diary.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentUpdateRequest {
    private Long commentId;
    private String content;
}
