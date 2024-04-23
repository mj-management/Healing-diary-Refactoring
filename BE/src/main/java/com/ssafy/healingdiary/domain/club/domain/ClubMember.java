package com.ssafy.healingdiary.domain.club.domain;

import com.ssafy.healingdiary.domain.member.domain.Member;
import com.ssafy.healingdiary.global.common.domain.BaseEntity;
import com.sun.istack.NotNull;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "club_member")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "club_member_id"))
@AttributeOverride(name = "createdDate", column = @Column(name = "club_member_created_date"))
@AttributeOverride(name = "updatedDate", column = @Column(name = "club_member_updated_date"))
public class ClubMember extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    @Column(name = "is_approved")
    private Boolean isApproved;

    public void approve() {
        this.isApproved = true;
    }

    public static ClubMember toEntityWithHost(Club club, Member member) {
        return ClubMember.builder()
            .club(club)
            .member(member)
            .isApproved(true)
            .build();
    }
}
