package com.ssafy.healingdiary.domain.club.dto;

import com.ssafy.healingdiary.domain.club.domain.Club;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClubDetailResponse {

    private Long clubId;
    private String name;
    private String description;
    private String clubImageUrl;
    private boolean isHost;
    private List<String> tags;

    public static ClubDetailResponse of(Club club, boolean isHost, List<String> tags) {
        return ClubDetailResponse.builder()
            .clubId(club.getId())
            .name(club.getName())
            .description(club.getDescription())
            .clubImageUrl(club.getClubImageUrl())
            .isHost(isHost)
            .tags(tags)
            .build();
    }
}
