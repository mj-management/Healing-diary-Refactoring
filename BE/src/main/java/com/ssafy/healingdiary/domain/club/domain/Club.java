package com.ssafy.healingdiary.domain.club.domain;

import com.ssafy.healingdiary.domain.diary.domain.Diary;
import com.ssafy.healingdiary.domain.member.domain.Member;
import com.ssafy.healingdiary.global.common.domain.BaseEntity;
import com.sun.istack.NotNull;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "club")
@Builder
@AttributeOverride(name = "id", column = @Column(name = "club_id"))
@AttributeOverride(name = "createdDate", column = @Column(name = "club_created_date"))
@AttributeOverride(name = "updatedDate", column = @Column(name = "club_updated_date"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Club extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    private Member host;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @Column(name = "club_image_url")
    private String clubImageUrl;

    @Builder.Default
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<ClubMember> clubMember = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<ClubTag> clubTag = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<Diary> diary = new ArrayList<>();

    public void setClubTag(List<ClubTag> clubTag) {
        this.clubTag = clubTag;
    }

    public void updateClub(String name, String description, List<ClubTag> clubTag,
        String clubImageUrl) {
        this.name = name;
        this.description = description;
        this.clubTag = clubTag;
        this.clubImageUrl = clubImageUrl;
    }
}
