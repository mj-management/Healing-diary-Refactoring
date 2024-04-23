package com.ssafy.healingdiary.domain.diary.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponse {
    private Long commentId;
    private Long memberId;
    private Long parentId;
    private String memberImageUrl;
    private String nickname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime datetime;
    private String content;
    private List<CommentResponse> children;

}
