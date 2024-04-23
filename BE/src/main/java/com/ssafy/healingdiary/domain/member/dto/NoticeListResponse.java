package com.ssafy.healingdiary.domain.member.dto;

import com.ssafy.healingdiary.domain.member.domain.CheckStatus;
import com.ssafy.healingdiary.domain.member.domain.Notice;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NoticeListResponse {

    private Long noticeId;
    private Long articleId;
    private LocalDateTime createdTime;
    private int noticeType;
    private boolean checkStatus;

    public static NoticeListResponse of(Notice notice) {
        return NoticeListResponse.builder()
            .noticeId(notice.getId())
            .articleId(notice.getArticleId())
            .createdTime(notice.getCreatedDate())
            .noticeType(notice.getNoticeType().getStatus())
            .checkStatus(CheckStatus.ofFlag(notice.getCheckStatus()))
            .build();
    }
}
