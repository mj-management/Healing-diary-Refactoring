package com.ssafy.healingdiary.domain.diary.repository;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static com.ssafy.healingdiary.domain.diary.domain.QDiary.diary;
import static com.ssafy.healingdiary.domain.diary.domain.QDiaryTag.diaryTag;
import static com.ssafy.healingdiary.domain.diary.domain.QEmotion.emotion;
import static com.ssafy.healingdiary.domain.member.domain.QMember.member;
import static com.ssafy.healingdiary.domain.tag.domain.QTag.tag;
import static org.springframework.util.StringUtils.hasText;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.healingdiary.domain.diary.dto.CalendarResponse;
import com.ssafy.healingdiary.domain.diary.dto.DiarySimpleResponse;
import com.ssafy.healingdiary.domain.diary.dto.EmotionStatisticResponse;
import com.ssafy.healingdiary.domain.diary.dto.QCalendarResponse;
import com.ssafy.healingdiary.domain.diary.dto.QDiarySimpleResponse;
import com.ssafy.healingdiary.domain.diary.dto.QEmotionResponse;
import com.ssafy.healingdiary.domain.diary.dto.QEmotionStatisticResponse;
import com.ssafy.healingdiary.domain.member.domain.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DiaryRepositoryImpl implements DiaryRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<DiarySimpleResponse> findByOption(Boolean all, Long memberId, Long clubId, String keyword, String tagContent, Integer year, Integer month, Integer day, Pageable pageable) {
        Set<Long> idSet = queryFactory
            .select(diary.id)
            .from(diary)
            .innerJoin(diary.emotion, emotion)
            .leftJoin(diary.diaryTag, diaryTag)
            .leftJoin(diaryTag.tag, tag)
            .where(
                all ? null : memberIdEq(memberId),
                clubIdEq(clubId),
                keywordMatch(keyword),
                tagEq(tagContent),
                dateEq(year, month, day)
            )
            .groupBy(diary.id)
            .orderBy(diary.createdDate.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize()+1)
            .fetch()
            .stream()
            .collect(Collectors.toUnmodifiableSet());

        if (idSet.isEmpty()) {
            return new SliceImpl<>(Collections.emptyList(), pageable, false);
        }

        List<DiarySimpleResponse> result = queryFactory
            .select(diary)
            .from(diary)
            .innerJoin(diary.emotion, emotion)
            .leftJoin(diary.diaryTag, diaryTag)
            .leftJoin(diaryTag.tag, tag)
            .where(
                diary.id.in(idSet)
            )
            .orderBy(diary.createdDate.desc())
            .transform(
                groupBy(diary.id).list(
                    new QDiarySimpleResponse(
                        diary.id,
                        diary.diaryImageUrl,
                        diary.createdDate,
                        new QEmotionResponse(
                            diary.emotion.emotionCode,
                            diary.emotion.value
                        ),
                        list(tag.content)
                    )
                )
            );

        boolean hasNext = false;
        if (result.size() > pageable.getPageSize()) {
            result.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(result, pageable, hasNext);
    }

    @Override
    public List<EmotionStatisticResponse> countEmotion(Long memberId, Integer year, Integer month) {
        List<EmotionStatisticResponse> result = queryFactory
            .select(
                new QEmotionStatisticResponse(
                    diary.emotion.emotionCode,
                    diary.emotion.value,
                    diary.count().as("count")
                )
            )
            .from(diary)
            .join(diary.emotion, emotion)
            .where(
                diary.member.id.eq(memberId),
                dateEq(year, month, null)
            )
            .groupBy(diary.emotion)
            .fetch();

        return result;
    }

    @Override
    public List<CalendarResponse> getEmotionByMonthOfYear(Long memberId, int year, int month) {

        List<CalendarResponse> result = queryFactory
            .select(
                diary.emotion.count(),
                diary.createdDate.max()
            )
            .from(diary)
            .where(
                diary.member.id.eq(memberId),
                dateEq(year, month, null)
            )
            .groupBy(
                diary.createdDate.year(),
                diary.createdDate.month(),
                diary.createdDate.dayOfMonth(),
                diary.emotion.emotionCode
            )
            .orderBy(
                diary.createdDate.dayOfMonth().asc(),
                diary.emotion.count().asc(),
                diary.createdDate.max().asc()
            )
            .transform(
                groupBy(
                    diary.createdDate.year(),
                    diary.createdDate.month(),
                    diary.createdDate.dayOfMonth(),
                    diary.emotion.emotionCode
                ).list(
                    new QCalendarResponse(
                        diary.createdDate.year(),
                        diary.createdDate.month(),
                        diary.createdDate.dayOfMonth(),
                        new QEmotionResponse(
                            diary.emotion.emotionCode,
                            diary.emotion.value
                        )
                    )
                )
            );

        Map<Integer, CalendarResponse> calendar = new HashMap<>();

        for(CalendarResponse cr : result){
            calendar.put(cr.getDay(), cr);
        }

        return new ArrayList<CalendarResponse>(calendar.values());
    }

    @Override
    public List<DiarySimpleResponse> findByDiseaseAndRegion(Member m, Integer num) {
        List<Long> idList = queryFactory
            .select(diary.id)
            .from(diary)
            .innerJoin(diary.member, member)
            .where(
                member.id.ne(m.getId()),
                diseaseOrRegion(m.getDisease(), m.getRegion())
            )
            .orderBy(diary.createdDate.desc())
            .limit(num)
            .fetch();

        List<DiarySimpleResponse> result = new ArrayList<>();
        if (!idList.isEmpty()) {
            result = queryFactory
                .select(diary)
                .from(diary)
                .innerJoin(diary.emotion, emotion)
                .leftJoin(diary.diaryTag, diaryTag)
                .leftJoin(diaryTag.tag, tag)
                .where(
                    diary.id.in(idList)
                )
                .orderBy(diary.createdDate.desc())
                .transform(
                    groupBy(diary.id).list(
                        new QDiarySimpleResponse(
                            diary.id,
                            diary.diaryImageUrl,
                            diary.createdDate,
                            new QEmotionResponse(
                                diary.emotion.emotionCode,
                                diary.emotion.value
                            ),
                            list(tag.content)
                        )
                    )
                );
        }

        if (result.size() < num) {
            int limit = num - result.size();
            List<Long> idList2 = queryFactory
                .select(diary.id)
                .from(diary)
                .where(
                    diary.member.id.ne(m.getId()),
                    diary.id.notIn(idList)
                )
                .orderBy(diary.createdDate.desc())
                .limit(limit)
                .fetch();

            List<DiarySimpleResponse> result2 = queryFactory
                .select(diary)
                .from(diary)
                .innerJoin(diary.emotion, emotion)
                .leftJoin(diary.diaryTag, diaryTag)
                .leftJoin(diaryTag.tag, tag)
                .where(diary.id.in(idList2))
                .orderBy(diary.createdDate.desc())
                .transform(
                    groupBy(diary.id).list(
                        new QDiarySimpleResponse(
                            diary.id,
                            diary.diaryImageUrl,
                            diary.createdDate,
                            new QEmotionResponse(
                                diary.emotion.emotionCode,
                                diary.emotion.value
                            ),
                            list(tag.content)
                        )
                    )
                );

            result.addAll(result2);
        }

        return result;
    }

    private BooleanExpression memberIdEq(Long memberId) {
        return memberId != null ? diary.member.id.eq(memberId) : null;
    }

    private BooleanExpression clubIdEq(Long clubId) {
        return clubId != null ? diary.club.id.eq(clubId) : null;
    }

    private BooleanExpression keywordMatch(String keyword) {
        return hasText(keyword) ? diary.content.contains(keyword) : null;
    }

    private BooleanExpression tagEq(String tagContent) {
        return hasText(tagContent) ? tag.content.eq(tagContent) : null;
    }

    public static BooleanExpression dateEq(Integer year, Integer month, Integer day) {
        if (year != null && month != null && day != null) {
            LocalDateTime startOfDay = LocalDateTime.of(year, month, day, 0, 0, 0);
            LocalDateTime endOfDay = LocalDateTime.of(year, month, day, 23, 59, 59);
            return diary.createdDate.between(startOfDay, endOfDay);
        }
        else if(year != null && month != null) {
            return diary.createdDate.month().eq(month);
        }
        else if(year != null) {
            return diary.createdDate.year().eq(year);
        }
        else {
            return null;
        }
    }

    private BooleanExpression diseaseOrRegion(String disease, String region) {
        return member.disease.eq(disease).or(member.region.eq(region));
    }

}
