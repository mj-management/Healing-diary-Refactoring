package com.ssafy.healingdiary.domain.club.dto;

import com.ssafy.healingdiary.domain.club.domain.Club;
import com.ssafy.healingdiary.domain.club.domain.ClubTag;
import com.ssafy.healingdiary.domain.member.domain.Member;
import com.ssafy.healingdiary.domain.tag.domain.Tag;
import java.util.List;
import lombok.Getter;

@Getter
public class ClubUpdateRequest {

    String name;
    String description;
    private String imageUrl;
    List<Long> tags;
}
