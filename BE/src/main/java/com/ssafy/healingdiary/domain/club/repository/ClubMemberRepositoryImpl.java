package com.ssafy.healingdiary.domain.club.repository;

import static com.ssafy.healingdiary.domain.club.domain.QClub.club;
import static com.ssafy.healingdiary.domain.club.domain.QClubMember.clubMember;
import static com.ssafy.healingdiary.domain.member.domain.QMember.member;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.healingdiary.domain.club.dto.ClubInvitationResponse;
import com.ssafy.healingdiary.domain.club.dto.QClubInvitationResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ClubMemberRepositoryImpl implements ClubMemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<ClubInvitationResponse> findDistinctByClubIdNot(Long clubId, Long hostId,
        Pageable pageable) {
        JPAQuery<ClubInvitationResponse> query = queryFactory
            .select(new QClubInvitationResponse(member.id, member.nickname, member.memberImageUrl))
            .from(member)
            .where(
                member.id.notIn((
                    JPAExpressions
                        .select(clubMember.member.id)
                        .from(clubMember)
                        .join(member).on(member.id.eq(clubMember.member.id))
                        .where(
                            clubMember.club.id.eq(clubId).or(member.id.eq(hostId))
                        )
                )
            ));

        List<ClubInvitationResponse> result = query
            .limit(pageable.getPageSize() + 1)
            .fetch();

        boolean hasNext = false;
        if (result.size() > pageable.getPageSize()) {
            result.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(result, pageable, hasNext);
    }
}
