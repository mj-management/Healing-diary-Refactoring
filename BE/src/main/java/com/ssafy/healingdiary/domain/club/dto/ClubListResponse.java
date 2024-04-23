package com.ssafy.healingdiary.domain.club.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.util.List;
import lombok.Getter;

@Getter
public class ClubListResponse {

    Long clubId;
    String name;
    String clubImageUrl;
    List<String> tags;

    @QueryProjection
    public ClubListResponse(Long clubId, String name, String clubImageUrl) {
        this.clubId = clubId;
        this.name = name;
        this.clubImageUrl = clubImageUrl;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
