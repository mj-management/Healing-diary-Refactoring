package com.ssafy.healingdiary.domain.club.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ClubSimpleResponse {

    Long clubId;
    String name;
    String clubImageUrl;
    List<String> tags;

    @QueryProjection
    public ClubSimpleResponse(Long clubId, String clubImageUrl, String name) {
        this.clubId = clubId;
        this.clubImageUrl = clubImageUrl;
        this.name = name;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
