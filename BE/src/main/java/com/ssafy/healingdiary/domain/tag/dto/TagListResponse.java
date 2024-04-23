package com.ssafy.healingdiary.domain.tag.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.ssafy.healingdiary.domain.tag.domain.Tag;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TagListResponse {

    Long id;
    String content;

    @QueryProjection
    public TagListResponse(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    public static TagListResponse of(Tag tag){
        return TagListResponse.builder()
            .id(tag.getId())
            .content(tag.getContent())
            .build();
    }
}
